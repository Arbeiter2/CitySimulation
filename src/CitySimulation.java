import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CitySimulation implements GameClock
{
	
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
	
	public static double sigmoid(double x, double factor)
	{
		return Math.exp(factor * x)/(1.0 + Math.exp(factor * x));
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
	HashSet<LandBlock> buildableBlocks;
	
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
		
		buildableBlocks = new HashSet<LandBlock>();

		
		// create taxRates, and load with default rates
		taxRates = new HashMap<TaxSource, Double>();
		
		buildingRegister = new HashMap<Building, Point>();
		
		taxRates.put(TaxSource.RESIDENTIAL, DEFAULT_RESIDENTIAL_TAX_RATE);
		taxRates.put(TaxSource.COMMERCIAL, DEFAULT_COMMERCIAL_TAX_RATE);
		taxRates.put(TaxSource.INDUSTRIAL, DEFAULT_INDUSTRIAL_TAX_RATE);
	}
	
	/**
	 * Place a block into the grid.
	 * 
	 * If block is a LandBlock, add it to the Set of buildables
	 * 
	 * @param g non-null GeoBlock
	 */
	public void placeBlock(GeoBlock g)
	{
		if (g == null) return;
		
		Point p = g.getLocation();

		// remove block from buildables just in case
		buildableBlocks.remove(grid[p.x][p.y]);
		
		grid[p.x][p.y] = g;
		if (g instanceof LandBlock)
			buildableBlocks.add((LandBlock) g);
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

		// building demolishes itself
		b.demolish();
		
		// remove from register
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
		double hlevel = CitySimulation.sigmoid(HEALTH_SIGMOID_FACTOR, (bedDiff + HEALTH_SIGMOID_OFFSET));
		
		return hlevel;
	}
	
	/**
	 * Create Set of occupied LandBlocks covered by MunicipalBuildings in provided list.
	 * 
	 * Uses supplied Map of occupied LandBlocks.
	 * 
	 * Updates all 
	 * 
	 * @param buildings list of buildings (should all be same class)
	 * @param builtupLand Map of occupied LandBlocks 
	 * @return Set of covered LandBlocks
	 */
	Set<LandBlock> getMuniBuildingCover(List<MunicipalBuilding> buildings, Map<Point, LandBlock> builtupLand)
	{
		HashSet<LandBlock> covered = new HashSet<LandBlock>();
		
		int coverage, height, width;
		LandBlock block;
		Point position, testPos = new Point(0,0);
		
		for (MunicipalBuilding p : buildings)
		{
			// NW corner
			position = p.getLocation().getLocation();
			coverage = p.getCoverage();
			height = p.getHeight();
			width = p.getWidth()
			;		
			int startX = (position.x - coverage> 0 ? position.x - coverage: position.x);
			int startY = (position.y - coverage > 0 ? position.y - coverage: position.y);
			int endX = (position.x + width + coverage < gridWidth ? position.x + width + coverage : gridWidth );
			int endY = (position.y + height + coverage < gridHeight ? position.y + height+ coverage : gridHeight );
			for (int i=startX; i < endX; i++)
			{
				for (int j=startY; j < endY; j++)
				{
					testPos.x = i;
					testPos.y = j;
					block = builtupLand.get(testPos);
					
					// block is covered, but not LandBlock
					if (block == null)
						continue;
					
					// final check that block is occupied
					if (block.getConstruction() != null)
						covered.add(block);
				}
			}
		}
		
		return covered;
	}
	
	/**
	 * Turn off fire and police station cover for all blocks in set
	 * 
	 * @param land Set of LandBlocks
	 */
	void resetMuniCover(Set<LandBlock> land)
	{
		for (LandBlock block : land)
		{
			block.setFireCover(false);
			block.setPoliceCover(false);
		}
	}
	
	/**
	 * Set blocks to fire covered
	 * 
	 * @param covered Set of LandBlocks
	 */
	void setFireCover(Set<LandBlock> covered)
	{
		for (LandBlock block : covered)
		{
			block.setFireCover(true);
		}
	}
	
	/**
	 * Set blocks to police covered
	 * 
	 * @param covered Set of LandBlocks
	 */	
	void setPoliceCover(Set<LandBlock> covered)
	{
		for (LandBlock block : covered)
		{
			block.setPoliceCover(true);
		}
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
		double x = CitySimulation.sigmoid(CRIME_SIGMOID_FACTOR, (unprotectedRatio + CRIME_SIGMOID_OFFSET));
		
		return 1.0 + CRIME_SIGMOID_SCALAR * x;
	}
	
	public void tick(int month)
	{
		ArrayList<MunicipalBuilding> policeStations = new ArrayList<MunicipalBuilding>();
		ArrayList<MunicipalBuilding> fireStations = new ArrayList<MunicipalBuilding>();
		ArrayList<MunicipalBuilding> hospitals = new ArrayList<MunicipalBuilding>();

		ArrayList<OccupiedBuilding> revenueBldgs = new ArrayList<OccupiedBuilding>();
		
		
		// get police stations, fire stations, hospitals
		// and taxable buildings from the register
		for (Building bldg : this.buildingRegister.keySet())
		{
			switch (bldg.getClass().getName())
			{
			case "PoliceStation": policeStations.add((MunicipalBuilding) bldg); break;
			case "FireStation": fireStations.add((MunicipalBuilding) bldg); break;
			case "Hospital": hospitals.add((MunicipalBuilding) bldg); break;
			
			default:
				revenueBldgs.add((OccupiedBuilding) bldg);
				break;
				
			};
		}
		
	}
	
}

