import java.awt.Point;

public class Building {

	public Building(LandBlock block, int constrMonth)
	{
		constructionMonth = constrMonth;
		location = block;
		location.addBuilding(this);
		constructionCost = 0;
		length = width = 0;
	}
	
	
	private int length;
	private int width;
	private LandBlock location;
	
	public void demolish()
	{
		location.addBuilding(null);
		location = null;
	}

	private int constructionMonth;
	public int getConstructionMonth() {
		return constructionMonth;
	}

	public double getConstructionCost()
	{
		return constructionCost;
	}
	
	private double constructionCost;
	private double value;
	public double getValue()
	{
		return value;
	}
	
	public void tick(int month)
	{
		
	}
	
}
