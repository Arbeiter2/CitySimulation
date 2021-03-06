/**
 * A police station covers a set of blocks, and reduces the levels of crime
 * 
 * @author dwgreenidge
 *
 */
public class PoliceStation extends MunicipalBuilding {
	public static final int HEIGHT = 1;
	public static final int WIDTH = 1;
	public static final int COVERAGE = 8;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 2000000;
	public static final int MONTHLY_COST = 50000;
	
	public static final String LONG_NAME = "Police";
	public static final String ABBREV = "P";
	
	public PoliceStation(int constrMonth) 
	{
		super(constrMonth, PoliceStation.CONSTRUCTION_COST, PoliceStation.HEIGHT, PoliceStation.WIDTH, PoliceStation.CAPACITY, 
				PoliceStation.COVERAGE, PoliceStation.MONTHLY_COST, PoliceStation.LONG_NAME, PoliceStation.ABBREV);
		setPoliceCover(true);
	}
}
