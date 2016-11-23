
public class SportsCenter extends RecreationBuilding {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 12;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 750000;
	public static final int MONTHLY_COST = 15000;
	
	public static final String TYPENAME = "Sports center";
	
	public SportsCenter(int constrMonth) 
	{
		super(constrMonth, SportsCenter.CONSTRUCTION_COST, SportsCenter.HEIGHT, SportsCenter.WIDTH, SportsCenter.CAPACITY, 
				SportsCenter.COVERAGE, SportsCenter.MONTHLY_COST, SportsCenter.TYPENAME);
	}
}
