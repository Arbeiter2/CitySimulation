// package bugs;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets; 
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.ColumnConstraints; 
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.RowConstraints; 
import javafx.scene.layout.StackPane; 
import javafx.stage.Stage; 

public class CitySimApplication extends Application { 

	int height = CitySimulation.DEFAULT_HEIGHT;
	int width = CitySimulation.DEFAULT_WIDTH;
	int budget = CitySimulation.DEFAULT_INITIAL_BUDGET;
	String mapFilePath;
	
	CitySimulation engine;
	
	boolean randomiseBlocks = false;
	
	@Override 
    public void start(Stage primaryStage) throws Exception 
    {
		try
		{
		processArgs(getParameters());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw (e);
		}

        StackPane stackPane = new StackPane(); 
        stackPane.setPadding(new Insets(8)); 
        stackPane.setPrefWidth(646); 
        stackPane.setPrefHeight(480); 
        stackPane.getChildren().add(setupGridPane()); 

        Scene scene = new Scene(stackPane); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }
    
    private void processArgs(Parameters params) throws Exception
    {
    	Map<String, String> args = params.getNamed();
    	
    	String h = args.get("height");
    	String w = args.get("width");
    	String b = args.get("budget");

    	if (h != null && w != null
    	&& Integer.parseInt(h) >= CitySimulation.MIN_GRID_HEIGHT 
    	&& Integer.parseInt(w) >= CitySimulation.MIN_GRID_WIDTH)
    	{
    		height = Integer.parseInt(h);
    		width = Integer.parseInt(w);
    	}
    	
    	if (b != null)
    	{
    		if (Integer.parseInt(b) >= CitySimulation.DEFAULT_INITIAL_BUDGET)
    			budget = Integer.parseInt(b);
    	}
    	
    	mapFilePath = args.get("mapfile");
    	if (mapFilePath == null)
    	{
			randomiseBlocks = true;
			mapFilePath = null;
		}
    	
		// create sim
    	engine = new CitySimulation(width, height, budget);
		
    	// either use random blocks or load from file path
    	if (randomiseBlocks)
			engine.randomiseBlocks();
		else
			engine.loadMapFile(mapFilePath);
    }

    @SuppressWarnings("unchecked")
	private GridPane setupGridPane() { 
        GridPane gridPane = new GridPane(); 
        gridPane.setStyle("-fx-border-color: black;"); 

        for (int column = 0; column < width; column++) { 
            final ColumnConstraints columnConstraints = new ColumnConstraints(); 
            columnConstraints.setPercentWidth(20); 
            gridPane.getColumnConstraints().add(columnConstraints); 
        } 

        for (int row = 0; row < height; row++) { 
            final RowConstraints rowConstraints = new RowConstraints(); 
            rowConstraints.setPercentHeight(20); 
            gridPane.getRowConstraints().add(rowConstraints); 
        } 

        for (int column = 0; column < width; column++) { 
            for (int row = 0; row < height; row++) { 
                final StackPane spCell = new StackPane(); 
                @SuppressWarnings("rawtypes")
				ChoiceBox cb = new ChoiceBox();
                cb.getItems().addAll("Land", "Water");

                spCell.getChildren().add(cb); 
                spCell.setStyle("-fx-background-color: lightblue; -fx-border-color: red"); 
                gridPane.add(spCell, column, row); 
            } 
        } 

        return gridPane; 
    } 

    public static void main(String[] args) { 
        launch(args); 
    } 
} 