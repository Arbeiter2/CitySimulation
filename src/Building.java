import java.awt.Point;

/**
 * A building for a LandBlock
 *
 * 
 * @author dwgreenidge
 *
 */
abstract public class Building {
	public Building(int constrMonth, int basicCost)
	{
		constructionMonth = constrMonth;
		basicConstructionCost = basicCost;
		constructionCost = constrMonth * CitySimulation.MONTHLY_INFLATION_RATE * basicConstructionCost;
		height = width = 0;
	}
	
	// LandBlock containing building
	private LandBlock location;

	public LandBlock getLocation() {
		return location;
	}

	public void setLocation(LandBlock loc) {
		location = loc;
	}

	protected int height;
	public int getHeight() {
		return height;
	}


	protected int width;
	public int getWidth() {
		return width;
	}

	public void demolish()
	{
		location.addBuilding(null);
		location = null;
	}

	private int constructionMonth;
	public int getConstructionMonth() {
		return constructionMonth;
	}

	private double constructionCost;
	private double basicConstructionCost;

	public double getConstructionCost()
	{
		return constructionCost;
	}
	
	private double value;
	public double getValue()
	{
		return value;
	}
	
	public void tick(int month)
	{
		
	}

	public double getBasicConstructionCost() {
		return basicConstructionCost;
	}
}
