import java.awt.Point;
/**
 * A block on which construction is possible.
 * 
 * @author Delano Greenidge
 * @see GeoBlock
 */
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
	
	void demolishBuilding()
	{
		if (construction != null)
		{
			construction.demolish();
		}
	}	

	public Building getConstruction() {
		return construction;
	}

	/**
	 * A block can contain at most one (1) building
	 */
	private Building construction;

	//public double getConstructionMultiplier();

	//public double getCrimeLevel();
	//public double getLandValue();
	//public double getWellbeingValue();
	private VegetationType cover;
	
	public enum VegetationType { FOREST, GRASS, ROCK };
	
}
