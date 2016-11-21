/**
 * A large industrial building.
 * 
 * @author dwgreenidge
 *
 */
public class Factory extends IndustrialBuilding {
	
	public static final int HEIGHT = 10;
	public static final int WIDTH = 10;
	public static final int CAPACITY = 1000;
	public static final int CONSTRUCTION_COST = 5000000;

	public static final String TYPENAME = "Factory";
	
	public Factory(int constrMonth) 
	{
		super(constrMonth, Factory.CONSTRUCTION_COST, Factory.TYPENAME);
		height = Factory.HEIGHT;
		width = Factory.WIDTH;
		capacity = Factory.CAPACITY;
	}

}
