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
		if (construction == null && b != null)
			construction = b;
	}
	
	void demolishBuilding()
	{
		if (construction != null)
		{
			construction.demolish();
			construction = null;
		}
	}	

	/**
	 * A block can contain at most one (1) building
	 */
	private Building construction;

	public Building getConstruction() {
		return construction;
	}

	/**
	 * whether this block is covered by at least one (1) fire station
	 */
	boolean hasFireCover = false;
	
	public boolean hasFireCover() {
		return hasFireCover;
	}

	public void setHasFireCover(boolean hasFireCover) {
		this.hasFireCover = hasFireCover;
	}
	
	/**
	 * whether this block is covered by at least one (1) police station
	 */
	boolean hasPolceCover = false;

	public boolean hasPolceCover() {
		return hasPolceCover;
	}

	public void setHasPolceCover(boolean hasPolceCover) {
		this.hasPolceCover = hasPolceCover;
	}

	//public double getConstructionMultiplier();
	//public double getCrimeLevel();
	//public double getLandValue();
	//public double getWellbeingValue();
	private VegetationType cover;
	
	public enum VegetationType { FOREST, GRASS, ROCK };
	
}
