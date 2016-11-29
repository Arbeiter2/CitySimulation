/**
 * A large industrial building.
 * 
 * @author dwgreenidge
 *
 */
public class Factory extends IndustrialBuilding {
	
	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	public static final int CAPACITY = 1000;
	public static final int CONSTRUCTION_COST = 5000000;

	public static final String LONG_NAME = "Factory";
	public static final String ABBREV = "Fa";
	
	public Factory(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, Factory.HEIGHT, Factory.WIDTH,
				 Factory.CAPACITY, Factory.LONG_NAME, Factory.ABBREV);
	}

}
