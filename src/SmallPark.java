
public class SmallPark extends MunicipalBuilding {

	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int COVERAGE = 4;
	public static final int CAPACITY = 100;
	public static final int CONSTRUCTION_COST = 100000;
	public static final int MONTHLY_COST = 5000;
	
	
	public SmallPark(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, SmallPark.COVERAGE, SmallPark.CONSTRUCTION_COST, SmallPark.MONTHLY_COST);
		height = SmallPark.HEIGHT;
		width = SmallPark.WIDTH;
		capacity = SmallPark.CAPACITY;
	}

}
