import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class DemolitionHandler implements EventHandler<ActionEvent> {

	LandBlock location;
	CitySimApplication citySimGame;
	
	DemolitionHandler(LandBlock block, CitySimApplication parent)
	{
		location = block;
		this.citySimGame = parent;
	}
	
	@Override
	public void handle(ActionEvent event) 
	{
		Building building = location.getConstruction();
		if (building == null)
			return;
		
		citySimGame.demolishBuilding(location);
	}

}
