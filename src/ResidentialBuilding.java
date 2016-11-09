public class ResidentialBuilding extends OccupiedBuilding {

	public ResidentialBuilding(int constrMonth) {
		super(constrMonth, CitySimulation.TaxSource.RESIDENTIAL);
	}
}
