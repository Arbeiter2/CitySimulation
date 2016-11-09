import java.awt.Point;
import java.util.HashMap;

public class CitySimulation {
	// default tax rates
	public static double DEFAULT_RESIDENTIAL_TAX_RATE = 0.25;
	public static double DEFAULT_COMMERCIAL_TAX_RATE = 0.25;
	public static double DEFAULT_INDUSTRIAL_TAX_RATE = 0.25;

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
	
	// grid of blocks representing map
	GeoBlock[][] grid;
	
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
		taxRates.put(TaxSource.RESIDENTIAL, DEFAULT_RESIDENTIAL_TAX_RATE);
		taxRates.put(TaxSource.COMMERCIAL, DEFAULT_COMMERCIAL_TAX_RATE);
		taxRates.put(TaxSource.INDUSTRIAL, DEFAULT_INDUSTRIAL_TAX_RATE);
	}
	
	public void placeBlock(GeoBlock g)
	{
		if (g == null) return;
		
		Point p = g.getLocation();
		grid[p.x][p.y] = g;
	}
	
	// place a building on a LandBlock
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
				if (((LandBlock) grid[x][y]).getConstruction() != null)
					return false;
			}
		}
		
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
}
