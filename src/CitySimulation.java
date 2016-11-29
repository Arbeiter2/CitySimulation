import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CitySimulation
{
	public static final int MIN_GRID_HEIGHT = 10;
	public static final int MIN_GRID_WIDTH = 10;

	public static final int DEFAULT_HEIGHT = 20;
	public static final int DEFAULT_WIDTH = 20;
	public static final int DEFAULT_INITIAL_BUDGET = 1000000;
	
	//
	// Tax &amp; inflation
	//
	// default tax rates
	public static final double DEFAULT_RESIDENTIAL_TAX_RATE = 0.25;
	public static final double DEFAULT_COMMERCIAL_TAX_RATE = 0.25;
	public static final double DEFAULT_INDUSTRIAL_TAX_RATE = 0.25;

	// determines rise of building costs and running costs
	public static final double MONTHLY_INFLATION_RATE = 0.001652;
	
	// 

	// tax revenue types
	public enum TaxSource { RESIDENTIAL, COMMERCIAL, INDUSTRIAL };
	
	//  A family has two working adults and two children, and is the 
	// standard unit for a residence.
	public static final int FAMILY_SIZE = 4;
	public static final int WORKING_FAMILY_MEMBERS = 2;
	

	// rate at which jobs are filled
	public static final double VACANCY_FILL_RATE = 0.01;
	
	//
	// Health stats
	//
	// minimum number of beds per inhabitant for good health
	// translates into 10 beds per 1000, comparable to South Korea
	public static final double HOSPITAL_BED_RATIO = 0.01;

	// controls slope of health level curve
	public static final double HEALTH_SIGMOID_FACTOR = -8d;
	public static final double HEALTH_SIGMOID_OFFSET = 0.25;
	public static final double MINIMUM_BED_DEMAND = 100d;

	// ratio of families scared off by low health
	public static final double HEALTH_ATTRITION_RATE = 0.01;
	
	// Crime stats
	//
	// number of recorded crimes per month per capita;
	// equal to Belgium's 0.1 per capita per year
	public static final double MONTHLY_CRIMES_PER_CAPITA = 0.008;

	// ratio of families scared off by crime
	public static final double CRIME_ATTRITION_RATE = 0.01;

	// ratio of families scared off by no fire cover
	public static final double FIRE_ATTRITION_RATE = 0.005;
	
	// controls slope of crimelevel curve
	public static final double CRIME_SIGMOID_SCALAR = 8d;
	public static final double CRIME_SIGMOID_FACTOR = 8d;
	public static final double CRIME_SIGMOID_OFFSET = -0.75;
	
	// rate of monthly growth corresponding to 10% per annum
	public static final double NATURAL_MONTHLY_GROWTH = 1.008;
	// rates for all types of occupied buildings
	HashMap<TaxSource, Double> taxRates;
	
	// set to true when all grid entries non-null
	boolean blocksInitialised = false;

	/**
	 * Get tax rate for specified class of building
	 * 
	 * @param taxClass class of building
	 * @return 
	 */
	public double getTaxRate(TaxSource taxClass) 
	{
		return taxRates.get(taxClass);
	}

	/**
	 * Set tax rate for specified class of building
	 * 
	 * If rate is valid, new rate is returned, otherwise old rate is returned
	 * 
	 * @param rate tax rate in range 0.0 to 1.0
	 * @param taxClass class of building
	 * @return new rate on success, old rate on failure
	 */
	public double setTaxRate(double rate, TaxSource taxClass) 
	{
		if (rate >= 0 && rate <= 1.0)
		{
			taxRates.put(taxClass, rate);
			return rate;
		}
		else
			return taxRates.get(taxClass);
	}

	
	public static double sigmoid(double x, double factor)
	{
		return Math.exp(factor * x)/(1.0 + Math.exp(factor * x));
	}
	
	
	
	/**
	 * Load a map file into the grid.
	 * 
	 * Assumes that grid of gridWidth * gridHeight cells has been allocated
	 *  
	 * @param mapFilePath
	 * @return
	 */
	public boolean loadMapFile(String mapFilePath) throws Exception
	{
		// ignore if initialisation complete
		if (blocksInitialised)
			return false;
		
		BufferedReader reader;
	    String line;
	    GeoBlock g;
	    int lineCount = 0;
		reader = new BufferedReader(new FileReader(mapFilePath));

		while ((line = reader.readLine()) != null)
	    {
			line = line.trim();
			lineCount++;
		    String[] tokens = line.split(",");
		    if (tokens.length != gridWidth || lineCount > gridHeight)
		    	break;
		    
		    for (int i=0; i < gridWidth; i++)
		    {
		    	Point p = new Point(i, lineCount-1);
		    	switch(tokens[i])
		    	{
		    	case "Wa": g= new WaterBlock(p); break;
		    	case "V": g= new VolcanoBlock(p); break;
		    	case "Fo": g= new LandBlock(p, Terrain.Type.FOREST); break;
		    	case "Sw": g= new LandBlock(p, Terrain.Type.SWAMP); break;
		    	case "Rk": g= new LandBlock(p, Terrain.Type.ROCK); break;
		    	default: g= new LandBlock(p, Terrain.Type.GRASS); break;
		    	};
		    	placeBlock(g);
		    }
	    }
		reader.close();
		
		
		
		return checkBlocksInitialised();
	}
	
	/**
	 * Fill grid with blocks of random type, and assign random terrain to 
	 * LandBlocks. 
	 * 
	 */
	public void randomiseBlocks()
	{
		if (blocksInitialised)
			return;
		
		Point p;
		GeoBlock g = null;

		
		for (int i=0; i < gridWidth; i++)
			for (int j=0; j < gridHeight; j++)
			{
				p = new Point(i, j);
				double x = Math.random();
				
				if (x <= 0.8)
				{
					Terrain.Type terrType;
					
					double t = Math.random();
					if (t <= 0.6)
						terrType = Terrain.Type.GRASS;
					else if (t <= 0.8)
						terrType = Terrain.Type.FOREST;
					else if (t <= 0.9)
						terrType = Terrain.Type.SWAMP;
					else
						terrType = Terrain.Type.ROCK;
					
					g = new LandBlock(p, terrType);
				}
				else if (x <= 0.99)
				{
					g = new WaterBlock(p);
				}
				else
				{
					g = new VolcanoBlock(p);
				}
				placeBlock(g);
			}
		
		blocksInitialised = true;
	}	
		
	// current month of simulation
	int currentMonth;
	
	/**
	 * Get current month of simulation
	 * 
	 * @return the currentMonth
	 */
	public int getCurrentMonth() 
	{
		return currentMonth;
	}

	// total population
	int totalPopulation = 0;
	
	/**
	 * Get total population of residential buildings
	 * 
	 * @return totalPopulation
	 */
	public int getTotalPopulation() 
	{
		return totalPopulation;
	}

	// grid of blocks representing map
	GeoBlock[][] grid;

	
	// all blocks on which buildings can be placed
	HashSet<LandBlock> buildableBlocks;
	
	
	// all buildings in the game, and their grid location
	HashMap<Building, Point> buildingRegister;

	// individual types of buildings
	ArrayList<MunicipalBuilding> policeStations = new ArrayList<MunicipalBuilding>();
	ArrayList<MunicipalBuilding> fireStations = new ArrayList<MunicipalBuilding>();
	ArrayList<MunicipalBuilding> hospitals = new ArrayList<MunicipalBuilding>();
	ArrayList<MunicipalBuilding> recreationBldgs = new ArrayList<MunicipalBuilding>();

	ArrayList<OccupiedBuilding> residentBldgs = new ArrayList<OccupiedBuilding>();
	ArrayList<OccupiedBuilding> industryBldgs = new ArrayList<OccupiedBuilding>();
	ArrayList<OccupiedBuilding> commerceBldgs = new ArrayList<OccupiedBuilding>();
	
	//
	// record of LandBlocks with construction 
	Map<Point, LandBlock> builtupLand;
	
	// record of LandBlocks covered by various municipal buildings
	Map<LandBlock, Integer> fireCover, policeCover, recreationCover;
	
	
	// width of grid
	int gridWidth;
	
	/**
	 * @return the gridWidth
	 */
	public int getGridWidth() 
	{
		return gridWidth;
	}

	// height of grid
	int gridHeight;
	
	/**
	 * @return the gridHeight
	 */
	public int getGridHeight() 
	{
		return gridHeight;
	}

	// available cash for construction, tax etc.
	int bankBalance;

	public int getBankBalance() 
	{
		return bankBalance;
	}

	CitySimulation(int width, int height, int startBudget) throws Exception
	{
		if (width < MIN_GRID_WIDTH || height < MIN_GRID_HEIGHT)
		{
			StringBuilder b = new StringBuilder();
			b.append("Bad width/height ");
			b.append(width);
			b.append("/");
			b.append(height);
			b.append("for CitySimulation");
			
			throw(new Exception(b.toString()));
		}
		
		gridWidth = width;
		gridHeight = height;
		bankBalance = startBudget;
		currentMonth = 0;
		
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
	 * String representation of all blocks in simulation
	 * 
	 * @return
	 */
	public String getCityMap()
	{
		StringBuilder b = new StringBuilder();
		
		if (!this.blocksInitialised)
			return b.toString();
		
		for (int i=0; i < gridWidth; i++)
		{
			for (int j=0; j < gridHeight; j++)
			{
				b.append(StringUtils.center(grid[i][j].getUsage(), 10));
			}
			b.append("\n");
		}
		return b.toString();
	}
	
	/**
	 * Check whether every block in grid has been filled
	 * 
	 * Update blocksInitialised if necessary
	 * 
	 * @return blocksInitialised
	 */
	public boolean checkBlocksInitialised()
	{
		if (blocksInitialised)
			return blocksInitialised;
		
		int count=0;
		for (int i=0; i < gridWidth; i++)
			for (int j=0; j < gridHeight; j++)
				if (grid[i][j] == null)
					break;
				else
					count++;
		
		blocksInitialised = (count == gridWidth * gridHeight);
		
		return blocksInitialised;
	}
	

	/**
	 * Get block at specified location
	 * 
	 * @param p x,y zero-based grid index
	 * @return null if invalid coordinates
	 */
	public GeoBlock getBlock(Point p)
	{
		if (p != null)
			return getBlock(p.x, p.y);
		else
			return null;
	}
	

	/**
	 * Get block at specified location
	 * 
	 * @param x
	 * @param y
	 * @return null if invalid coordinates
	 */
	public GeoBlock getBlock(int x, int y)
	{
		if (x >= 0 && x < gridWidth && y >=0 && y < gridHeight)
			return grid[x][y];
		else
			return null;
	}
	
	/**
	 * Place a building on a LandBlock.
	 * 
	 * Every surrounding block must be LandBlock 
	 * clear of buildings.
	 * 
	 * @param b Building
	 * @param p grid location of building NW corner
	 * @return true successful; false failed
	 */
	public boolean placeBuilding(Building b, Point p)
	{
		if (b == null || p == null) 
			return false;
		
		// p represents upper left corner of building; 
		// building must fit within grid
		if (p.x < 0 || p.y < 0 || p.x + b.getWidth() - 1 >= gridWidth || p.y + b.getHeight() - 1 >= gridHeight)
			return false;
		
		// check whether surrounding blocks are clear LandBlocks
		for (int x=0; x < b.getWidth(); x++)
		{
			for (int y=0; y < b.getHeight(); y++)
			{
				// we can only build on a land block
				if (!(grid[p.x + x][p.y + y] instanceof LandBlock))
					return false;	
				
				// block must be clear
				if (((LandBlock) grid[p.x + x][p.y + y]).getConstruction() != null)
					return false;
			}
		}
		
		// set Building location to point p
		b.construct((LandBlock)  grid[p.x][p.y]);
		buildingRegister.put(b, p);

		// set every LandBlock to occupied
		for (int x=0; x < b.getWidth(); x++)
		{
			for (int y=0; y < b.getHeight(); y++)
			{
				((LandBlock) grid[p.x + x][p.y + y]).addBuilding(b);
			}
		}
		
		// deduct construction cost from balance
		bankBalance -= b.getConstructionCost();
		
		// update view of LandBlocks with construction
		builtupLand = getBuiltupLand();
		
		// add to specific register
		String bldgClassName = b.getClass().getName();
		switch (bldgClassName)
		{
		case "PoliceStation": 
			policeStations.add((MunicipalBuilding) b);
			policeCover = getMuniBuildingCover(policeStations);
			break;
		case "FireStation": 
			fireStations.add((MunicipalBuilding) b); 
			fireCover = getMuniBuildingCover(fireStations);
			break;
		case "Hospital": hospitals.add((MunicipalBuilding) b); break;
		
		default:
			switch (b.getClass().getSuperclass().getName())
			{
			case "ResidentialBuilding": residentBldgs.add((ResidentialBuilding) b); break;
			case "IndustrialBuilding": industryBldgs.add((IndustrialBuilding) b); break;
			case "CommercialBuilding": commerceBldgs.add((CommercialBuilding) b); break;
			case "RecreationBuilding": 
				recreationBldgs.add((MunicipalBuilding) b); 
				recreationCover = getMuniBuildingCover(recreationBldgs);
				break;
			
			default: System.out.println("Unknown building type ["+bldgClassName+"]");
			};
			break;
		};
		
		//Set<LandBlock> builtup = new HashSet<LandBlock>(builtupLand.values());
		resetMuniCover();
		setPoliceCover(policeCover);
		setFireCover(fireCover);

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
		for (int x=0; x < b.getWidth(); x++)
		{
			for (int y=0; y < b.getHeight(); y++)
			{
				((LandBlock) grid[p.x+x][p.y+y]).demolishBuilding();
			}
		}

		// building demolishes itself
		// and deducts cost
		this.bankBalance -= b.demolish();
		
		// remove from global register
		buildingRegister.remove(b);
		
		// remove to specific register
		String bldgClassName = b.getClass().getName();
		switch (bldgClassName)
		{
		case "PoliceStation": policeStations.remove((MunicipalBuilding) b); break;
		case "FireStation": fireStations.remove((MunicipalBuilding) b); break;
		case "Hospital": hospitals.remove((MunicipalBuilding) b); break;
		
		default:
			switch (b.getClass().getSuperclass().getName())
			{
			case "ResidentialBuilding": residentBldgs.remove((ResidentialBuilding) b); break;
			case "IndustrialBuilding": industryBldgs.remove((IndustrialBuilding) b); break;
			case "CommercialBuilding": commerceBldgs.remove((CommercialBuilding) b); break;
			case "RecreationBuilding": recreationBldgs.remove((MunicipalBuilding) b); break;

			default: System.out.println("Unknown building type ["+bldgClassName+"]");
			};
			break;
		};				
	}

	/**
	 * Calculate health level of city, with range (-1, 1)
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
		double bedDiff = 0d;
		if (bedDemand >= CitySimulation.MINIMUM_BED_DEMAND)
			bedDiff = (bedSupply - bedDemand)/bedDemand;
		
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
	 * @return Set of covered LandBlocks
	 */
	Map<LandBlock, Integer> getMuniBuildingCover(List<MunicipalBuilding> buildings)
	{
		HashMap<LandBlock, Integer>covered = new HashMap<LandBlock, Integer>();
		
		int coverage, height, width;
		LandBlock block;
		Point position;
		
		for (MunicipalBuilding p : buildings)
		{
			// NW corner
			position = p.getLocation().getLocation();
			coverage = p.getCoverage();
			height = p.getHeight();
			width = p.getWidth();
	
			int startX = Math.max(position.x - coverage, 0);
			int startY = Math.max(position.y - coverage, 0);
			int endX = Math.min(position.x + width + coverage, gridWidth);
			int endY = Math.min(position.y + height + coverage, gridHeight);
			for (int i=startX; i < endX; i++)
			{
				for (int j=startY; j < endY; j++)
				{
					if (!(grid[i][j] instanceof LandBlock))
						continue;
					block = (LandBlock) grid[i][j];
					Integer count = covered.get(p);
					if (count == null)
						count = 0;
					covered.put(block, ++count);
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
	void resetMuniCover()
	{
		for (LandBlock block : buildableBlocks)
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
	void setFireCover(Map<LandBlock, Integer> covered)
	{
		if (covered == null)
			return;
		
		for (Map.Entry<LandBlock, Integer> entry : covered.entrySet())
		{
			LandBlock block = entry.getKey();
			Integer count = entry.getValue();			
			block.setFireCover(count > 0);
		}
	}
	
	/**
	 * Set blocks to police covered
	 * 
	 * @param covered Set of LandBlocks
	 */
	void setPoliceCover(Map<LandBlock, Integer> covered)
	{
		if (covered == null)
			return;
		
		for (Map.Entry<LandBlock, Integer> entry : covered.entrySet())
		{
			LandBlock block = entry.getKey();
			Integer count = entry.getValue();			
			block.setPoliceCover(count > 0);
		}
	}	
	
	/**
	 * Calculate crime rate multiplier, with range (0, CRIME_SIGMOID_FACTOR).
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
	
	
	/**
	 * Create Set of LandBlocks with construction
	 * 
	 * @return
	 */
	Map<Point, LandBlock> getBuiltupLand()
	{
		Map<Point, LandBlock> builtupLand = new HashMap<Point, LandBlock>();
		
		for (LandBlock block : buildableBlocks)
		{
			if (block.getConstruction() != null)
				builtupLand.put(block.getLocation(), block);
		}
		return builtupLand;
	}
	

	
	/**
	 * Get total capacity of collection of OccupiedBuilding
	 * 
	 * @param buildings
	 * @return
	 */
	public int getTotalCapacity(List<OccupiedBuilding> buildings)
	{
		int retVal = 0;
		for (OccupiedBuilding c : buildings)
		{
			retVal += c.getCapacity();
		}
		return retVal;
	}

	/**
	 * Get total number of occupants of collection of OccupiedBuilding
	 * 
	 * @param buildings
	 * @return
	 */
	public int getTotalOccupants(List<OccupiedBuilding> buildings)
	{
		int retVal = 0;
		for (OccupiedBuilding c : buildings)
		{
			retVal += c.getNumberOfOccupants();
		}
		return retVal;
	}
	
	/**
	 * Create map of buildings using recreation/police/fire figures
	 * 
	 * Value stored is number of recreation facilities covering building 
	 * 
	 * @param recreationMap
	 * @return
	 */
	public Map<Building, Double> getWellBeing(Map<LandBlock, Integer> recreationMap)
	{
		Map<Building, Double> wellBeingMap = new HashMap<Building, Double>();
		
		Building b;
		Double count;
		for (Map.Entry<LandBlock, Integer> entry : recreationMap.entrySet())
		{
			b = entry.getKey().getConstruction();
			count = wellBeingMap.get(b);
			wellBeingMap.put(b, Math.max(count, entry.getValue()));
		}
		
		// now adjust for missing fire/police cover
		LandBlock block;
		for (Map.Entry<Building, Double> entry : wellBeingMap.entrySet())
		{
			b = entry.getKey();
			count= wellBeingMap.get(b);
			block = b.getLocation();
			if (!block.getFireCover())
				wellBeingMap.put(b, count * 0.5);
			if (!block.getPoliceCover())
				wellBeingMap.put(b, count * 0.5);
		}
		
		return wellBeingMap;
	}
	
	
	
	/**
	 * Change the number of occupants in the supplied buildings.
	 * 
	 * Buildings with higher well-being scores are more likely to get new occupants,
	 * and lower-scoring buildings are more likely to lose occupants first.
	 * 
	 * @param delta number of occupants to add (can be negative)
	 * @param vacancies number of empty spaces available
	 * @param totalCapacity total number of spaces
	 * @param buildings OccupiedBuildings to change
	 * @param wellBeingMap aggregate well-being value for each building
	 */
	void applyOccupancyChange(int delta, int vacancies, int totalCapacity, 
			List<OccupiedBuilding> buildings,
			Map<Building, Double> wellBeingMap)
	{
		List<OccupiedBuilding> availableBldgs = new ArrayList<OccupiedBuilding>();
		double wellBeing, totalWb = 0d;
		
		// do nothing if no change required, or no residential space available
		if (delta == 0 || delta > vacancies)
			return;
		
		for (OccupiedBuilding bldg : buildings)
		{
			// ignore buildings that are full, or empty
			if (delta > 0 && bldg.getNumberOfOccupants() == bldg.getCapacity())
				continue;
			if (delta < 0 && bldg.getNumberOfOccupants() == 0)
				continue;
			
			availableBldgs.add(bldg);
			wellBeing = wellBeingMap.get(bldg);
			totalWb += (delta > 0 ? wellBeing : (wellBeing == 0 ? 999 : 1.0/wellBeing));
		}
		
		
		// upper and lower bounds of ranges
		double lBound[] = new double[availableBldgs.size()];
		double uBound[] = new double[availableBldgs.size()];
		double last = 0d;
		
		int i = 0;
		for (OccupiedBuilding bldg : availableBldgs)
		{
			lBound[i] = last;
			uBound[i] = last + wellBeingMap.get(bldg)/totalWb;
			last = uBound[i++]; 
		}
		
		int incr = (delta > 0 ? 1 : -1);
		
		double rnd;
		boolean successful;
		while (delta != 0)
		{
			rnd = Math.random();
			i=0;
			successful = false;
			for (OccupiedBuilding bldg : availableBldgs)
			{
				// if this building is chosen and can add/remove occupants, make it happen
				if (rnd >= lBound[i] && rnd < uBound[i])
				{
					if (delta > 0 && bldg.getNumberOfOccupants() < bldg.getCapacity() || 
						delta < 0 && bldg.getNumberOfOccupants() > 0)
					{
						bldg.addOccupants(incr);
						vacancies = vacancies - incr;
						delta = delta - incr;

						successful = true;
					}
					break;
				}
				else if (uBound[i] < rnd)
				{
					i++;
				}
				else
				{
					break;
				}
			}
			
			// if occupant change was unsuccessful, check whether it is 
			// even possible anymore
			if (!successful)
			{
				if (incr < 0 && vacancies == totalCapacity || incr > 0 && vacancies == 0)
					break;
			}
		}
	}
	
	/**
	 * Calculate total tax revenue for collection of buildings
	 * 
	 * @param month game simulation month
	 * @param taxClass residential/commercial/industrial
	 * @param buildings List of OccupiedBuilding
	 * @return total tax revenue
	 */
	double getTaxRevenue(int month, CitySimulation.TaxSource taxClass, List<OccupiedBuilding> buildings)
	{
		double rate = getTaxRate(taxClass);

		// calculate tax revenue
		double revenue = 0d;
		for (OccupiedBuilding bldg : buildings)
		{
			revenue += bldg.getTaxRevenue(rate, month);
		}
		return revenue;
	}
	
	/**
	 * Calculate total tax revenue for all taxable buildings
	 * 
	 * @param month game simulation month
	 * @return
	 */
	public double getTotalTaxRevenue(int month)
	{
		double total = 0d;
		
		total += getTaxRevenue(month, TaxSource.RESIDENTIAL, residentBldgs);
		total += getTaxRevenue(month, TaxSource.COMMERCIAL, commerceBldgs);
		total += getTaxRevenue(month, TaxSource.INDUSTRIAL, industryBldgs);
		
		return total;
	}

	/**
	 * Calculate total tax spend for collection of municipal buildings
	 * 
	 * @param month game simulation month
	 * @param buildings List of MunicipalBuilding
	 * @return total tax spend
	 */	
	double getTaxSpend(int month, List<MunicipalBuilding> buildings)
	{
		// calculate tax revenue
		double cost = 0d;
		for (MunicipalBuilding bldg : buildings)
		{
			cost += bldg.getAdjustedMonthlyCost(month);
		}
		return cost;
	}
	
	
	/**
	 * Calculate total tax spend for all municipal buildings
	 * 
	 * @param month
	 * @return
	 */
	public double getTotalTaxSpend(int month)
	{
		double total = 0d;
		
		total += getTaxSpend(month, fireStations);
		total += getTaxSpend(month, policeStations);
		total += getTaxSpend(month, hospitals);
		total += getTaxSpend(month, recreationBldgs);
		
		return total;		
	}
	
	public void tick()
	{
		// advance by one month
		this.currentMonth++;
		
		// for finding police/fire coverage
		double unprotectedRatio, cityCrimeLevel, cityFireCover, cityHealthLevel;
		double totalBlockCount = (double) builtupLand.size();
		
		int numberOfHouseholds = getTotalOccupants(residentBldgs);
		totalPopulation = numberOfHouseholds * FAMILY_SIZE;
		int residentialCapacity = getTotalCapacity(residentBldgs);
		int residentialVacancies = residentialCapacity - numberOfHouseholds;
		
		int workingPopulation = numberOfHouseholds * WORKING_FAMILY_MEMBERS;
		int commerceCapacity = getTotalCapacity(commerceBldgs);
		int industrialCapacity = getTotalCapacity(industryBldgs);

		int workVacancies = (commerceCapacity + industrialCapacity) - workingPopulation;
		
		// find police cover
		unprotectedRatio = (totalBlockCount - policeCover.size())/totalBlockCount; 
		cityCrimeLevel = getCityCrimeLevel(unprotectedRatio);

		// find fire cover
		cityFireCover = (totalBlockCount - fireCover.size())/totalBlockCount; 

		// find health level
		cityHealthLevel = getCityHealthLevel(hospitals.size(), totalPopulation);
		
		Map<Building, Double> wellBeingMap = getWellBeing(recreationCover);
		
		double change = 0;
		change -= cityCrimeLevel * CRIME_ATTRITION_RATE * numberOfHouseholds;
		change -= cityFireCover * FIRE_ATTRITION_RATE * numberOfHouseholds;
		change += cityHealthLevel * HEALTH_ATTRITION_RATE * numberOfHouseholds;
		change += NATURAL_MONTHLY_GROWTH * numberOfHouseholds;
	
		// people arrive to fill jobs
		change += workVacancies * VACANCY_FILL_RATE;
		
		int populationDelta = (int) change;
		applyOccupancyChange(populationDelta, residentialVacancies, residentialCapacity, residentBldgs, wellBeingMap);
		
		int workforceDelta = (int) (workVacancies * VACANCY_FILL_RATE);
		applyOccupancyChange(workforceDelta/2, workVacancies, commerceCapacity, commerceBldgs, wellBeingMap);
		applyOccupancyChange(workforceDelta/2, workVacancies, industrialCapacity, industryBldgs, wellBeingMap);
		
	
		// update bank balance
		this.bankBalance += this.getTotalTaxRevenue(currentMonth);
		this.bankBalance -= this.getTotalTaxSpend(currentMonth);
		
	}
	
}

