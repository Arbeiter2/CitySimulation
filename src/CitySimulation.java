import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;

public class CitySimulation {
	
	//
	// Tax &amp; inflation
	//
	// default tax rates
	public static final double DEFAULT_RESIDENTIAL_TAX_RATE = 0.25;
	public static final double DEFAULT_COMMERCIAL_TAX_RATE = 0.25;
	public static final double DEFAULT_INDUSTRIAL_TAX_RATE = 0.25;

	// determines rise of building costs and running costs
	public static final double MONTHLY_INFLATION_RATE = 0.001652;

	// tax revenue types
	public enum TaxSource { RESIDENTIAL, COMMERCIAL, INDUSTRIAL };
	
	// rates for all types of occupied buildings
	HashMap<TaxSource, Double> taxRates;

	public double getTaxRate(TaxSource taxClass) {
		return taxRates.get(taxClass);
	}
	
	public void setTaxRate(TaxSource taxClass, double rate) {
		if (rate > 0.00 && rate < 1.00)
			this.taxRates.put(taxClass, rate);
	}
	
	
	//
	// Health stats
	//
	// minimum number of beds per inhabitant for good health
	// translates into 10 beds per 1000, comparable to South Korea
	public static final double HOSPITAL_BED_RATIO = 0.01;

	// controls slope of health level curve
	public static final double HEALTH_SIGMOID_FACTOR = -8d;
	public static final double HEALTH_SIGMOID_OFFSET = 0.25;

	// Crime stats
	//
	// number of recorded crimes per month per capita;
	// equal to Belgium's 0.1 per capita per year
	public static final double MONTHLY_CRIMES_PER_CAPITA = 0.008;
	
	// controls slope of health level curve
	public static final double CRIME_SIGMOID_SCALAR = 8d;
	public static final double CRIME_SIGMOID_FACTOR = 8d;
	public static final double CRIME_SIGMOID_OFFSET = -0.75;	
	
	// grid of blocks representing map
	GeoBlock[][] grid;
	
	// all buildings in the game, and their grid location
	HashMap<Building, Point> buildingRegister;
	
	// width of grid
	int gridWidth;
	
	// height of grid
	int gridHeight;

	CitySimulation(int width, int height)
	{
		gridWidth = width;
		gridHeight = height;
		
		// create grid of blocks
		grid = new GeoBlock[gridWidth][gridHeight];
		
		// create taxRates, and load with default rates
		taxRates = new HashMap<TaxSource, Double>();
		
		buildingRegister = new HashMap<Building, Point>();
		
		taxRates.put(TaxSource.RESIDENTIAL, DEFAULT_RESIDENTIAL_TAX_RATE);
		taxRates.put(TaxSource.COMMERCIAL, DEFAULT_COMMERCIAL_TAX_RATE);
		taxRates.put(TaxSource.INDUSTRIAL, DEFAULT_INDUSTRIAL_TAX_RATE);
	}
	
	/**
	 * Place a block into the grid
	 * 
	 * @param g non-null GeoBlock
	 */
	public void placeBlock(GeoBlock g)
	{
		if (g == null) return;
		
		Point p = g.getLocation();
		grid[p.x][p.y] = g;
	}
	

	/**
	 * Place a building on a LandBlock.
	 * 
	 * Every surrounding block must be LandBlock 
	 * clear of buildings.
	 * 
	 * @param b Building
	 * @param p grid location of top left corner
	 * @return true successful; false failed
	 */
	public boolean placeBuilding(Building b, Point p)
	{
		if (b == null || p == null) 
			return false;
		
		// p represents upper left corner of building;
		// check whether surrounding blocks are clear LandBlocks
		for (int x=p.x; x < b.getWidth(); x++)
		{
			for (int y=p.y; y < b.getHeight(); y++)
			{
				// we can only build on a land block
				if (!(grid[x][y] instanceof LandBlock))
					return false;	
				
				// block must be clear
				if (((LandBlock) grid[x][y]).getConstruction() != null)
					return false;
			}
		}
		
		// set Building location to point p
		b.setLocation((LandBlock)  grid[p.x][p.y]);
		buildingRegister.put(b, p);

		// set every LandBlock to occupied
		for (int x=p.x; x < b.getWidth(); x++)
		{
			for (int y=p.y; y < b.getHeight(); y++)
			{
				((LandBlock) grid[x][y]).addBuilding(b);
			}
		}
		return true;
	}

	/**
	 * Demolish building on a LandBlock.
	 * 
	 * Every LandBlock occupied by building must be cleared
	 * 
	 * @param p location of block
	 * @return true successful; false failed
	 */
	public boolean demolishBuilding(Point p)
	{
		// p must be inside grid
		if (p == null || 
			p.x < 0 || p.x >= this.gridWidth || 
			p.y < 0 || p.y >= this.gridHeight)
			return false;
		
		// check whether grid is LandBlock
		if (!(grid[p.x][p.y] instanceof LandBlock))
			return false;
		
		// get the building on the block if there is one
		Building b = ((LandBlock) grid[p.x][p.y]).getConstruction();
		if (b == null)
			return false;
		
		demolishBuildingInternal(p, b);

		return true;
	}
	

	/**
	* Demolish building on a LandBlock.
	* 
	* Every LandBlock occupied by building must be cleared
	* 
	* @param b Building
	* @return true successful; false failed
	*/
	public boolean demolishBuilding(Building b)
	{
		Point p = buildingRegister.get(b);
		
		// building not found
		if (p == null)
			return false;
		
		demolishBuildingInternal(p, b);

		return true;
	}
	
	/**
	 * Erase building from grid blocks it occupies.
	 * 
	 * @param p coordinates of upper left corner of building in grid
	 * @param b target building
	 */
	private void demolishBuildingInternal(Point p, Building b)
	{
		for (int x=p.x; x < b.getWidth(); x++)
		{
			for (int y=p.y; y < b.getHeight(); y++)
			{
				((LandBlock) grid[x][y]).demolishBuilding();
				grid[x][y] = null;
			}
		}

		// building demolishes itself, and then is removed from register
		b.demolish();
		buildingRegister.remove(b);		
	}

	/**
	 * Calculate health level of city, with range (0, 1)
	 * 
	 * @param numberOfHospitals
	 * @param population
	 * @return percentage health level
	 */
	public double getCityHealthLevel(int numberOfHospitals, int population)
	{
		double bedSupply = numberOfHospitals * Hospital.CAPACITY;
		double bedDemand = population * HOSPITAL_BED_RATIO;
		
		// small populations can look after themselves
		if (bedDemand < 1d)
			return 0d;
		
		double bedDiff = (bedSupply - bedDemand)/bedDemand;
		
		// health level changes as a sigmoid
		double hlevel = Math.exp(HEALTH_SIGMOID_FACTOR * (bedDiff + HEALTH_SIGMOID_OFFSET))/(1 + Math.exp(HEALTH_SIGMOID_FACTOR * (bedDiff + HEALTH_SIGMOID_OFFSET)));
		
		return hlevel;
	}
	
	
	/**
	 * Calculate crime rate multiplier, with range (1, CRIME_SIGMOID_FACTOR).
	 * 
	 * This represents how much higher crime is than the expected average.
	 * 
	 * Based on ratio of unprotected (that is, not covered by at least one police station) 
	 * occupied LandBlocks to total number of occupied LandBlocks.
	 * 
	 * @param unprotectedRatio ratio of unprotected LandBlocks to total occupied LandBlocks
	 * @return multiplication factor for crime
	 */
	public double getCityCrimeLevel(double unprotectedRatio)
	{
		double x = Math.exp(CRIME_SIGMOID_FACTOR * (unprotectedRatio + CRIME_SIGMOID_OFFSET));
		
		return 1.0 + CRIME_SIGMOID_SCALAR * (x/(1.0 + x));
	}
	
	public int getCurrentMonth()
	{
		return 1;
	}
	
}

