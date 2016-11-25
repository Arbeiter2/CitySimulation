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
	
	public static final String LONG_NAME = "Fire Station";
	public static final String ABBREV = "F";
	
	public FireStation(int constrMonth)
	{
		super(constrMonth, FireStation.CONSTRUCTION_COST, FireStation.HEIGHT, FireStation.WIDTH, FireStation.CAPACITY, 
				FireStation.COVERAGE, FireStation.MONTHLY_COST, FireStation.LONG_NAME, FireStation.ABBREV);
	}
}
