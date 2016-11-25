
public class SmallPark extends RecreationBuilding {

	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int COVERAGE = 4;
	public static final int CAPACITY = 100;
	public static final int CONSTRUCTION_COST = 100000;
	public static final int MONTHLY_COST = 5000;
	
	public static final String LONG_NAME = "Small park";
	public static final String ABBREV = "SPk";
	
	public SmallPark(int constrMonth) 
	{
		super(constrMonth, SmallPark.CONSTRUCTION_COST, SmallPark.HEIGHT, SmallPark.WIDTH, SmallPark.CAPACITY, 
				SmallPark.COVERAGE, SmallPark.MONTHLY_COST, SmallPark.LONG_NAME, SmallPark.ABBREV);
	}

}
