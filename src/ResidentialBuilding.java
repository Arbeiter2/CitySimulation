public class ResidentialBuilding extends OccupiedBuilding {

	public ResidentialBuilding(int constrMonth, int basicCost) {
		super(constrMonth, basicCost, CitySimulation.TaxSource.RESIDENTIAL);
	}
}
