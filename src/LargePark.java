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
	
	public static final String TYPENAME = "Large park";

	
	
	public LargePark(int constrMonth) 
	{
		super(constrMonth, LargePark.CONSTRUCTION_COST, LargePark.HEIGHT, LargePark.WIDTH, LargePark.CAPACITY, 
				LargePark.COVERAGE, LargePark.MONTHLY_COST, LargePark.TYPENAME);
	}

}
