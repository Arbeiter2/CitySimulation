/**
 * Buildings where commercial activity takes place
 * 
 * @author dwgreenidge
 *
 */
public class CommercialBuilding extends OccupiedBuilding {

	public CommercialBuilding(int constrMonth, int basicCost, String name) {
		super(constrMonth, basicCost, CitySimulation.TaxSource.COMMERCIAL, name);
	}

}
