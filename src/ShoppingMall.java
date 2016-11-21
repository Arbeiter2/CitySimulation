/**
 * The equivalent of many stores in one place.
 * 
 */
public class ShoppingMall extends CommercialBuilding {

	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;
	public static final int CAPACITY = 600;
	public static final int CONSTRUCTION_COST = 3000000;

	public static final String TYPENAME = "Shopping Mall";
	
	public ShoppingMall(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, ShoppingMall.TYPENAME);
		height = ShoppingMall.HEIGHT;
		width = ShoppingMall.WIDTH;
		capacity = ShoppingMall.CAPACITY;
	}

}
