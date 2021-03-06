/**
 * A building for a LandBlock
 *
 * 
 * @author dwgreenidge
 *
 */
abstract public class Building 
{
	/**
	 * @param constrMonth month building was constructed
	 * @param basicCost fixed year-0 cost of construction
	 * @param bheight x-axis extent of building in blocks
	 * @param bWidth y-axis extent of building in blocks
	 * @param capcty max number of occupants/users building can handle
	 * @param name building type name (immutable)
	 */
	public Building(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, String name, String abbrev)
	{
		constructionMonth = constrMonth;
		basicConstructionCost = basicCost;
		
		// construction costs rise with inflation
		height = bheight;
		width = bWidth;
		capacity = capcty;
		longName = name;
		abbrevName = abbrev;
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
				Math.pow(1.0 + CitySimulation.MONTHLY_INFLATION_RATE, constructionMonth) * 
				basicConstructionCost;
		return constructionCost;
	}

	// height of building in blocks
	protected final int height;
	public int getHeight() {
		return height;
	}

	// width of building in blocks
	protected final int width;
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
		return getDemolitionCost();
	}

	/**
	 * cost of demolition
	 * 
	 * @return cost of demolition
	 */
	public double getDemolitionCost()
	{
		return height * width * Building.DEMOLITION_COST_PER_BLOCK;
	}
	
	// month of construction, used for calculating inflation
	private final int constructionMonth;
	public int getConstructionMonth() {
		return constructionMonth;
	}

	// basic cost of construction before inflation
	private final double basicConstructionCost;

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
	private boolean hasFireCover = false;
	
	public boolean getFireCover() {
		return hasFireCover;
	}

	public void setFireCover(boolean hasFireCover) {
		this.hasFireCover = hasFireCover;
	}
	
	/**
	 * whether this block is covered by at least one (1) police station
	 */
	private boolean hasPoliceCover = false;

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
	protected final int capacity;
	public int getCapacity()
	{
		return capacity;
	}
	
	public void tick(int month)
	{
		
	}
	
	/**
	 * text description of building
	 */
	protected final String longName;
	public String getLongName()
	{
		return longName;
	}

	protected final String abbrevName;
	public String getAbbrev()
	{
		return abbrevName;
	}	
}
