/**
 * A building containing the homes of multiple families.
 * 
 * Capacity is number of resident families. A family has 
 * two working adults and two children.
 * 
 * @author dwgreenidge
 *
 */
public class ApartmentBuilding extends ResidentialBuilding {
	public static final int HEIGHT = 3;
	public static final int WIDTH = 3;
	public static final int CAPACITY = 16;
	public static final int CONSTRUCTION_COST = 600000;

	public static final String LONG_NAME = "Apartments";
	public static final String ABBREV= "Ap";
	
	public ApartmentBuilding(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, ApartmentBuilding.HEIGHT, ApartmentBuilding.WIDTH,
				 ApartmentBuilding.CAPACITY, ApartmentBuilding.LONG_NAME, ApartmentBuilding.ABBREV);
	}
}
