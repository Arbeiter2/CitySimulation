
abstract public class IndustrialBuilding extends OccupiedBuilding {

	public IndustrialBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, String type) 
	{
		super(constrMonth, basicCost, bheight, bWidth, capcty, CitySimulation.TaxSource.INDUSTRIAL, type);
	}

}
