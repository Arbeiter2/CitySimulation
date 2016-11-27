// package bugs;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage; 

public class CitySimApplication extends Application { 

	int height = CitySimulation.DEFAULT_HEIGHT;
	int width = CitySimulation.DEFAULT_WIDTH;
	int budget = CitySimulation.DEFAULT_INITIAL_BUDGET;
	String mapFilePath;
	
	CitySimulation engine;
	
	boolean randomiseBlocks = false;
	
	Map<String, String> blockColours = new HashMap<String, String>();
	Map<String, String> textColours = new HashMap<String, String>();
	
	ContextMenu contextMenu = new ContextMenu();

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
		
		initialise();

        StackPane stackPane = new StackPane(); 
        stackPane.setPadding(new Insets(8)); 
        stackPane.setPrefWidth(646); 
        stackPane.setPrefHeight(480); 
        stackPane.getChildren().add(setupGridPane()); 

        Scene scene = new Scene(stackPane); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }
	
	private void initialise()
	{
		blockColours.put(Terrain.getAbbreviation(Terrain.Type.FOREST), "green");
		blockColours.put(Terrain.getAbbreviation(Terrain.Type.GRASS), "lightgreen");
		blockColours.put(Terrain.getAbbreviation(Terrain.Type.ROCK), "grey");
		blockColours.put(Terrain.getAbbreviation(Terrain.Type.SWAMP), "darkgreen");
		blockColours.put(Terrain.getAbbreviation(Terrain.Type.VOLCANO), "sienna");
		blockColours.put("Wa", "lightblue");

		textColours.put(Terrain.getAbbreviation(Terrain.Type.FOREST), "#FFFFFF");
		textColours.put(Terrain.getAbbreviation(Terrain.Type.GRASS), "#000000");
		textColours.put(Terrain.getAbbreviation(Terrain.Type.ROCK), "#FFFFFF");
		textColours.put(Terrain.getAbbreviation(Terrain.Type.SWAMP), "#FFFFFF");
		textColours.put(Terrain.getAbbreviation(Terrain.Type.VOLCANO), "#FFFFFF");		
		textColours.put("Wa", "#000000");
		

		MenuItem info = new MenuItem("Info");
		info.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Info");
		    }
		});

		Menu construct = new Menu("New Building");

		// residential buildings
		Menu residential = new Menu("Residential");
		MenuItem house = new MenuItem("House");
		house.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("House");
		    }
		});
		MenuItem apartments = new MenuItem("Apartments");
		apartments.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Apartments");
		    }
		});
		residential.getItems().addAll(house, apartments);

		// commercial buildings
		Menu commercial = new Menu("Commercial");

		MenuItem store = new MenuItem("Store");
		store.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Store");
		    }
		});

		MenuItem shoppingMall = new MenuItem("Shopping Mall");
		shoppingMall.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Shopping Mall");
		    }
		});
		commercial.getItems().addAll(store, shoppingMall);

		// industrial buildings
		Menu industrial = new Menu("Industrial");

		MenuItem workshop = new MenuItem("Workshop");
		workshop.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Workshop");
		    }
		});

		MenuItem factory = new MenuItem("Factory");
		factory.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Factory");
		    }
		});
		industrial.getItems().addAll(workshop, factory);

		MenuItem demolish = new MenuItem("Demolish");
		demolish.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Demolish");
		    }
		});
		construct.getItems().addAll(residential, commercial, industrial);
		contextMenu.getItems().addAll(info, construct, demolish);
		
		
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
                String abbrev = engine.getBlock(column, row).getUsage();
                Label lb = new Label(abbrev);
                lb.setContextMenu(contextMenu);

                lb.setTextFill(Color.web(textColours.get(abbrev)));
                
                spCell.getChildren().add(lb);
                spCell.setStyle("-fx-background-color: " + blockColours.get(abbrev) +"; -fx-border-color: red"); 

                
                gridPane.add(spCell, column, row); 
            } 
        } 

        return gridPane; 
    } 

    public static void main(String[] args) { 
        launch(args); 
    } 
} 