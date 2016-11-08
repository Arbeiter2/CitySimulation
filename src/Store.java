
public class Store extends CommercialBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int CAPACITY = 6;
	public static final int CONSTRUCTION_COST = 600000;
	
	public Store(LandBlock block, int constrMonth) 
	{
		super(block, constrMonth);
		height = Store.HEIGHT;
		width = Store.WIDTH;
		capacity = Store.CAPACITY;
	}

}
