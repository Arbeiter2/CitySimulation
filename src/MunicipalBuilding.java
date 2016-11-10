/**
 * Buildings run by the city, requiring tax revenue
 * 
 * @author dwgreenidge
 *
 */
abstract public class MunicipalBuilding extends Building {
	
	// monthly running cost
	private int monthlyCost; 
	
	public int getMonthlyCost()
	{
		return monthlyCost;
	}


	public MunicipalBuilding(int constrMonth, int basicCost, int mthlyCost) 
	{
		super(constrMonth, basicCost);
		monthlyCost = mthlyCost;
		capacity = 0;
	}
}
