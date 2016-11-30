import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class InfoHandler implements EventHandler<ActionEvent> {

	LandBlock block;
	CitySimApplication engine;
	
	public InfoHandler(LandBlock b, CitySimApplication app) 
	{
		block = b;
		engine = app;
	}

	@Override
	public void handle(ActionEvent event) 
	{
		StringBuilder b = new StringBuilder();
		b.append("Grid location = (" + block.getLocation().x + ", " + block.getLocation().y +")\n");
		b.append("Terrain = " + block.getTerrain().toString() + "\n");
		b.append("Police cover = " + (block.getPoliceCover()? "Yes" : "No") + "\n");
		b.append("Fire cover = " + (block.getFireCover() ? "Yes" : "No") + "\n");
		Building bldg = block.getConstruction();
		b.append("Building type = " );
		if (bldg == null)
			b.append("Unoccupied\n");
		else
		{
			b.append(bldg.getLongName() + "\n");
			b.append("Construction month = " + bldg.getConstructionMonth() + "\n");
			b.append("Construction cost = " + bldg.getConstructionCost() + "\n");
			b.append("Capacity = " + bldg.getCapacity() + "\n");
			if (bldg instanceof OccupiedBuilding)
				b.append("Occupants = " + ((OccupiedBuilding) bldg).getNumberOfOccupants()+ "\n");

			if (bldg instanceof MunicipalBuilding)
			{
				b.append("Monthly cost (unadjusted) = " + ((MunicipalBuilding) bldg).getMonthlyCost()+ "\n");
			}
		}
		System.out.println(b.toString() + "\n");
		engine.setStatuxText(b.toString());
	}

}
