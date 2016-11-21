/**
 * A fire station covers a set of blocks, and provides fire protection
 * 
 * @author dwgreenidge
 *
 */
public class FireStation extends MunicipalBuilding {
	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 8;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 2000000;
	public static final int MONTHLY_COST = 50000;
	
	public static final String TYPENAME = "Fire Station";
	
	public FireStation(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, FireStation.COVERAGE, FireStation.CONSTRUCTION_COST, 
				FireStation.MONTHLY_COST, FireStation.TYPENAME);
		height = FireStation.HEIGHT;
		width = FireStation.WIDTH;
		capacity = FireStation.CAPACITY;
	}
}
