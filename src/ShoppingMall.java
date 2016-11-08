
public class ShoppingMall extends CommercialBuilding {

	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;
	public static final int CAPACITY = 600;
	public static final int CONSTRUCTION_COST = 3000000;
	
	public ShoppingMall(LandBlock block, int constrMonth) 
	{
		super(block, constrMonth);
		height = ShoppingMall.HEIGHT;
		width = ShoppingMall.WIDTH;
		capacity = ShoppingMall.CAPACITY;
	}

}
