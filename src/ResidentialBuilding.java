abstract public class ResidentialBuilding extends OccupiedBuilding 
{

	public ResidentialBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, String name, String abbrev) 
	{
		super(constrMonth, basicCost, bheight, bWidth, capcty, CitySimulation.TaxSource.RESIDENTIAL, name, abbrev);
	}
}
