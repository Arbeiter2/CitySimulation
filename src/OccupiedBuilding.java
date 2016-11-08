public class OccupiedBuilding extends Building {

	public OccupiedBuilding(LandBlock block, int constrMonth) 
	{
		super(block, constrMonth);
		capacity = 0;
		numberOfOccupants = 0;
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
	
	double getTaxRevenue()
	{
		return CitySimulation.TAX_RATE * this.getNumberOfOccupants();
	}

}
