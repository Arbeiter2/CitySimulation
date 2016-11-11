
public class PoliceStation extends MunicipalBuilding {
	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 8;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 2000000;
	public static final int MONTHLY_COST = 50000;
	
	
	public PoliceStation(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, PoliceStation.COVERAGE, PoliceStation.CONSTRUCTION_COST, PoliceStation.MONTHLY_COST);
		height = PoliceStation.HEIGHT;
		width = PoliceStation.WIDTH;
		capacity = PoliceStation.CAPACITY;
	}
}
