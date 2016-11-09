public class House extends ResidentialBuilding {
	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int CAPACITY = 4;
	public static final int CONSTRUCTION_COST = 80000;
	
	public House(int constrMonth) {
		super(constrMonth);
		height = House.HEIGHT;
		width = House.WIDTH;
		capacity = CAPACITY;
	}
}
