/**
 * A building in which people live or work, and a source of
 * tax revenue.
 * 
 * @author dwgreenidge
 *
 */
public class OccupiedBuilding extends Building {
	
	// all occupied buildings have a tax revenue type
	CitySimulation.TaxSource taxClass;

	public OccupiedBuilding(int constrMonth, CitySimulation.TaxSource tax_class) 
	{
		super(constrMonth);
		capacity = 0;
		numberOfOccupants = 0;
		taxClass = tax_class;
	}
	
	int numberOfOccupants;
	int getNumberOfOccupants()
	{
		return numberOfOccupants;
	}
	
	int capacity;
	int getCapacity()
	{
		return capacity;
	}
}
