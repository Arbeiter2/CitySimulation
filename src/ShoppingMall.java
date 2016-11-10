/**
 * The equivalent of many stores in one place.
 * 
 */
public class ShoppingMall extends CommercialBuilding {

	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;
	public static final int CAPACITY = 600;
	public static final int CONSTRUCTION_COST = 3000000;
	
	public ShoppingMall(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST);
		height = ShoppingMall.HEIGHT;
		width = ShoppingMall.WIDTH;
		capacity = ShoppingMall.CAPACITY;
	}

}
