/**
 * Sole health care source. 
 * 
 * @author Delano Greenidge
 *
 */
public class Hospital extends MunicipalBuilding {
	public static final int HEIGHT = 2;
	public static final int WIDTH = 2;
	public static final int COVERAGE = 6;
	public static final int CAPACITY = 500;
	public static final int CONSTRUCTION_COST = 2000000;
	public static final int MONTHLY_COST = 50000;
	
	public static final String TYPENAME = "Hospital";
	
	public Hospital(int constrMonth) 
	{
		super(constrMonth, Hospital.CONSTRUCTION_COST, Hospital.HEIGHT, Hospital.WIDTH, Hospital.CAPACITY, 
				Hospital.COVERAGE, Hospital.MONTHLY_COST, Hospital.TYPENAME);
	}

}
