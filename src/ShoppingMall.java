/**
 * The equivalent of many stores in one place.
 * 
 */
public class ShoppingMall extends CommercialBuilding {

	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;
	public static final int CAPACITY = 600;
	public static final int CONSTRUCTION_COST = 3000000;

	public static final String LONG_NAME = "Shopping Mall";
	public static final String ABBREV = "SM";
	
	public ShoppingMall(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, ShoppingMall.HEIGHT, ShoppingMall.WIDTH,
				 ShoppingMall.CAPACITY, ShoppingMall.LONG_NAME, ShoppingMall.ABBREV);
	}

}
