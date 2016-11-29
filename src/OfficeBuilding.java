
public class OfficeBuilding extends CommercialBuilding {

	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	public static final int CAPACITY = 240;
	public static final int CONSTRUCTION_COST = 1600000;

	public static final String LONG_NAME = "Offices";
	public static final String ABBREV = "Of";
	
	public OfficeBuilding(int constrMonth) 
	{
		super(constrMonth, OfficeBuilding.CONSTRUCTION_COST, OfficeBuilding.HEIGHT, OfficeBuilding.WIDTH,
				OfficeBuilding.CAPACITY, OfficeBuilding.LONG_NAME, OfficeBuilding.ABBREV);
	}
}
