import java.awt.Point;

/**
 * A building for a LandBlock
 *
 * 
 * @author dwgreenidge
 *
 */
abstract public class Building 
{
	public Building(int constrMonth, int basicCost, String name)
	{
		constructionMonth = constrMonth;
		basicConstructionCost = basicCost;
		
		// construction costs rise with inflation
		height = width = 0;
		capacity = 0;
		typeName = name;
	}

	public static final double DEMOLITION_COST_PER_BLOCK = 1000;
	
	
	// LandBlock containing building
	private LandBlock location;

	public LandBlock getLocation() {
		return location;
	}

	/**
	 * Set destination LandBlock for building
	 *  
	 * @param loc
	 * @return cost of construction
	 */
	public double construct(LandBlock loc) 
	{
		location = loc;
		if (location != null)
			constructionCost = location.getConstructionMultiplier() * 
				constructionMonth * 
				(1.0 + CitySimulation.MONTHLY_INFLATION_RATE) * 
				basicConstructionCost;
		return constructionCost;
	}

	// height of building in blocks
	protected int height;
	public int getHeight() {
		return height;
	}

	// width of building in blocks
	protected int width;
	public int getWidth() {
		return width;
	}

	
	/**
	 * Remove reference to parent LandBlock
	 * 
	 * @return cost of demolition
	 */
	public double demolish()
	{
		location = null;
		return height * width * DEMOLITION_COST_PER_BLOCK;
	}

	// month of construction, used for calculating inflation
	private int constructionMonth;
	public int getConstructionMonth() {
		return constructionMonth;
	}

	// basic cost of construction before inflation
	private double basicConstructionCost;

	public double getBasicConstructionCost() {
		return basicConstructionCost;
	}
	
	// cost of construction including inflation
	private double constructionCost;

	public double getConstructionCost()
	{
		return constructionCost;
	}

	/**
	 * whether this block is covered by at least one (1) fire station
	 */
	boolean hasFireCover = false;
	
	public boolean hasFireCover() {
		return hasFireCover;
	}

	public void setFireCover(boolean hasFireCover) {
		this.hasFireCover = hasFireCover;
	}
	
	/**
	 * whether this block is covered by at least one (1) police station
	 */
	boolean hasPoliceCover = false;

	public boolean getPoliceCover() {
		return hasPoliceCover;
	}

	public void setPoliceCover(boolean hasPoliceCover) {
		this.hasPoliceCover = hasPoliceCover;
	}
	
	
	/**
	 * For tax revenue buildings like homes or offices, maximum number
	 * of families/workers they can contain;
	 * 
	 * For municipal buildings (fire/police stations, hospitals, etc.) 
	 * maximum number of people they can serve
	 */
	protected int capacity;
	int getCapacity()
	{
		return capacity;
	}
	
	public void tick(int month)
	{
		
	}
	
	/**
	 * text description of building
	 */
	protected String typeName;
	public String getTypeName()
	{
		return typeName;
	}

}
