/**
 * Buildings where commercial activity takes place
 * 
 * @author dwgreenidge
 *
 */
public class CommercialBuilding extends OccupiedBuilding {

	public CommercialBuilding(int constrMonth) {
		super(constrMonth, CitySimulation.TaxSource.COMMERCIAL);
	}

}
