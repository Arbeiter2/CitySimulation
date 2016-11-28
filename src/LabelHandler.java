import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class LabelHandler implements EventHandler<ActionEvent> {

	Label label;
	LandBlock location;
	String buildingClassName;
	CitySimApplication citySimGame;
	
	LabelHandler(Label lb, LandBlock block, String building, CitySimApplication parent)
	{
		label = lb;
		location = block;
		buildingClassName = building;
		this.citySimGame = parent;
	}
	
	@Override
	public void handle(ActionEvent event) 
	{
		Building building = null;
		try {
			Class<?> c = Class.forName(buildingClassName);
			Constructor<?> cons = c.getConstructor(int.class);
			building = (Building) cons.newInstance(citySimGame.getEngine().getCurrentMonth());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (building == null)
			return;
		
    	if (!citySimGame.getEngine().placeBuilding(building, location.getLocation()))
    		System.out.println("!" + building.getLongName());
    	else
    	{
    		citySimGame.placeBuilding(building);
    	}
		
	}

}
