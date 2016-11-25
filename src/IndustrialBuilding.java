
abstract public class IndustrialBuilding extends OccupiedBuilding {

	public IndustrialBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, String name, String abbrev) 
	{
		super(constrMonth, basicCost, bheight, bWidth, capcty, CitySimulation.TaxSource.INDUSTRIAL, name, abbrev);
	}

}
