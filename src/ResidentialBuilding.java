abstract public class ResidentialBuilding extends OccupiedBuilding 
{

	public ResidentialBuilding(int constrMonth, int basicCost, String name) 
	{
		super(constrMonth, basicCost, CitySimulation.TaxSource.RESIDENTIAL, name);
	}
}
