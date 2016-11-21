
abstract public class IndustrialBuilding extends OccupiedBuilding {

	public IndustrialBuilding(int constrMonth, int basicCost, String type) 
	{
		super(constrMonth, basicCost, CitySimulation.TaxSource.INDUSTRIAL, type);
	}

}
