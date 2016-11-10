/**
 * Buildings where commercial activity takes place
 * 
 * @author dwgreenidge
 *
 */
public class CommercialBuilding extends OccupiedBuilding {

	public CommercialBuilding(int constrMonth, int basicCost) {
		super(constrMonth, basicCost, CitySimulation.TaxSource.COMMERCIAL);
	}

}
