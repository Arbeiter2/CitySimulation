
public class IndustrialBuilding extends OccupiedBuilding {

	public IndustrialBuilding(int constrMonth, int basicCost) {
		super(constrMonth, basicCost, CitySimulation.TaxSource.INDUSTRIAL);
	}

}
