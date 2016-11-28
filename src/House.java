/**
 * A residence for a single family
 * 
 * A family has two working adults and two children.
 *  
 * @author dwgreenidge
 *
 */
public class House extends ResidentialBuilding 
{
	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int CAPACITY = 1;
	public static final int CONSTRUCTION_COST = 80000;

	public static final String LONG_NAME = "House";
	public static final String ABBREV = "Ho";
	
	public House(int constrMonth) {
		super(constrMonth, House.CONSTRUCTION_COST, House.HEIGHT, House.WIDTH,
				 House.CAPACITY, House.LONG_NAME, House.ABBREV);
	}
}
