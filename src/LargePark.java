/**
 * @author Delano Greenidge
 *
 */
public class LargePark extends RecreationBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 8;
	public static final int CAPACITY = 100;
	public static final int CONSTRUCTION_COST = 400000;
	public static final int MONTHLY_COST = 10000;
	
	
	public LargePark(int constrMonth, int basicCost, int mthlyCost) {
		super(constrMonth, LargePark.COVERAGE, LargePark.CONSTRUCTION_COST, LargePark.MONTHLY_COST);
		height = LargePark.HEIGHT;
		width = LargePark.WIDTH;
		capacity = LargePark.CAPACITY;
	}

}
