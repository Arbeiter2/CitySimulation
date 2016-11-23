
public class Store extends CommercialBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int CAPACITY = 6;
	public static final int CONSTRUCTION_COST = 600000;

	public static final String TYPENAME = "Store";
	
	public Store(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, Store.HEIGHT, Store.WIDTH,
				 Store.CAPACITY, Store.TYPENAME);
	}

}
