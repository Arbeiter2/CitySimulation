/**
 * Buildings where commercial activity takes place
 * 
 * @author dwgreenidge
 *
 */
abstract public class CommercialBuilding extends OccupiedBuilding {

	public CommercialBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, String name) {
		super(constrMonth, basicCost, bheight, bWidth, capcty, CitySimulation.TaxSource.COMMERCIAL, name);
	}

}
