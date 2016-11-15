
public class SportsCenter extends MunicipalBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 12;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 750000;
	public static final int MONTHLY_COST = 15000;
	
	
	public SportsCenter(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, SportsCenter.COVERAGE, SportsCenter.CONSTRUCTION_COST, SportsCenter.MONTHLY_COST);
		height = SportsCenter.HEIGHT;
		width = SportsCenter.WIDTH;
		capacity = SportsCenter.CAPACITY;
	}
}
