import java.awt.Point;
/**
 * A block on which construction is possible.
 * 
 * @author Delano Greenidge
 * @see GeoBlock
 */
public class LandBlock extends GeoBlock {

	LandBlock(Point p, Terrain.Type v)
	{
		super(p);
		construction = null;
		terrain = v;
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
	
	public boolean getFireCover() {
		return hasFireCover;
	}

	/**
	 * Set fire station coverage status of LandBlock and any construction on it
	 * 
	 * @param hasFireCover true for covered, false otherwise
	 */
	public void setFireCover(boolean hasFireCover) {
		this.hasFireCover = hasFireCover;
		if (construction != null)
			construction.setFireCover(hasPoliceCover);
	}
	
	/**
	 * whether this block is covered by at least one (1) police station
	 */
	boolean hasPoliceCover = false;

	public boolean getPoliceCover() {
		return hasPoliceCover;
	}

	/**
	 * Set police station coverage status of LandBlock and any construction on it
	 * 
	 * @param hasPoliceCover true for covered, false otherwise
	 */
	public void setPoliceCover(boolean hasPoliceCover) {
		this.hasPoliceCover = hasPoliceCover;
		if (construction != null)
			construction.setPoliceCover(hasPoliceCover);
	}

	/**
	 * Get cost multiplier for terrain type
	 * 
	 * @return
	 */
	public double getConstructionMultiplier()
	{
		return Terrain.getConstructionMultiplier(terrain);
	}
	
	
	//public double getCrimeLevel();
	//public double getLandValue();
	//public double getWellbeingValue();
	private Terrain.Type terrain;

	/**
	 * @return terrain
	 */
	public Terrain.Type getTerrain() {
		return terrain;
	}
	
	@Override
	public String getUsage()
	{
		if (construction == null)
			return Terrain.getAbbreviation(terrain);
		else
			return construction.getAbbrev();
	}
}
