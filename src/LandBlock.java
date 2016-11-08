import java.awt.Point;

public class LandBlock extends GeoBlock {

	LandBlock(Point p, VegetationType v)
	{
		super(p);
		construction = null;
		cover = v;
	}
	
	void addBuilding(Building b)
	{
		construction = b;
	}

	private Building construction;

	//public double getConstructionMultiplier();

	//public double getCrimeLevel();
	//public double getLandValue();
	//public double getWellbeingValue();
	private VegetationType cover;
	
	public enum VegetationType { FOREST, GRASS, ROCK };
	
}
