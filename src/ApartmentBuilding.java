public class ApartmentBuilding extends ResidentialBuilding {
	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	public static final int CAPACITY = 96;
	public static final int CONSTRUCTION_COST = 600000;
	
	public ApartmentBuilding(LandBlock block, int constrMonth) 
	{
		super(block, constrMonth);
		height = ApartmentBuilding.HEIGHT;
		width = ApartmentBuilding.WIDTH;
		capacity = ApartmentBuilding.CAPACITY;
	}
}