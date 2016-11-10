/**
 * A large industrial building.
 * 
 * @author dwgreenidge
 *
 */
public class Factory extends OccupiedBuilding {
	
	public static final int HEIGHT = 10;
	public static final int WIDTH = 10;
	public static final int CAPACITY = 1000;
	public static final int CONSTRUCTION_COST = 5000000;
	
	public Factory(int constrMonth) {
		super(constrMonth, CONSTRUCTION_COST, CitySimulation.TaxSource.INDUSTRIAL);
		height = House.HEIGHT;
		width = House.WIDTH;
		capacity = CAPACITY;
	}

}
