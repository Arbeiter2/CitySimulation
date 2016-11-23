/**
 * A small industrial building.
 * 
 * @author dwgreenidge
 *
 */
public class Workshop extends IndustrialBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int CAPACITY = 10;
	public static final int CONSTRUCTION_COST = 500000;

	public static final String TYPENAME = "Workshop";
	
	public Workshop(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, Workshop.HEIGHT, Workshop.WIDTH,
				 Workshop.CAPACITY, Workshop.TYPENAME);
	}

}
