
public class OfficeBuilding extends CommercialBuilding {

	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	public static final int CAPACITY = 240;
	public static final int CONSTRUCTION_COST = 1600000;
	
	public OfficeBuilding(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST);
		height = OfficeBuilding.HEIGHT;
		width = OfficeBuilding.WIDTH;
		capacity = OfficeBuilding.CAPACITY;
	}
}
