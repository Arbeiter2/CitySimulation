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
	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	public static final int CAPACITY = 16;
	public static final int CONSTRUCTION_COST = 600000;

	public static final String TYPENAME = "Apartments";
	
	public ApartmentBuilding(int constrMonth) 
	{
		super(constrMonth, CONSTRUCTION_COST, ApartmentBuilding.TYPENAME);
		height = ApartmentBuilding.HEIGHT;
		width = ApartmentBuilding.WIDTH;
		capacity = ApartmentBuilding.CAPACITY;
	}
}
