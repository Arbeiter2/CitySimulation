import java.awt.Point;

public class Building {

	public Building(LandBlock block, int constrMonth)
	{
		constructionMonth = constrMonth;
		location = block;
		location.addBuilding(this);
		constructionCost = 0;
		height = width = 0;
	}
	

	protected int height;
	public int getHeight() {
		return height;
	}


	protected int width;
	public int getWidth() {
		return width;
	}


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
