
public class Hospital extends MunicipalBuilding {
	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 6;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 2000000;
	public static final int MONTHLY_COST = 50000;
	
	
	public Hospital(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, Hospital.COVERAGE, Hospital.CONSTRUCTION_COST, Hospital.MONTHLY_COST);
		height = Hospital.HEIGHT;
		width = Hospital.WIDTH;
		capacity = Hospital.CAPACITY;
	}

}
