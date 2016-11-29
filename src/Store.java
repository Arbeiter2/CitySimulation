
public class Store extends CommercialBuilding {

	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int CAPACITY = 6;
	public static final int CONSTRUCTION_COST = 600000;

	public static final String LONG_NAME = "Store";
	public static final String ABBREV = "St";
	
	public Store(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, Store.HEIGHT, Store.WIDTH,
				 Store.CAPACITY, Store.LONG_NAME, Store.ABBREV);
	}

}
