// package bugs;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Control;
import javafx.stage.Stage; 

public class CitySimApplication extends Application { 

	int height = CitySimulation.DEFAULT_HEIGHT;
	int width = CitySimulation.DEFAULT_WIDTH;
	int budget = CitySimulation.DEFAULT_INITIAL_BUDGET;
	String mapFilePath;
	
	CitySimulation engine;
	
	public CitySimulation getEngine() 
	{
		return engine;
	}

	boolean randomiseBlocks = false;
	
	Map<String, String> blockColours = new HashMap<String, String>();
	Map<String, String> textColours = new HashMap<String, String>();
	
	Label[][] panelGrid = null;
	GridPane gridPane;

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
	
	private void addContextMenu(Control control, LandBlock block)
	{
		ContextMenu contextMenu = new ContextMenu();
		
		MenuItem info = new MenuItem("Info");
		info.setOnAction(new InfoHandler(block));

		Menu construct = new Menu("New Building");

		// residential buildings
		Menu residential = new Menu("Residential");
		MenuItem house = new MenuItem("House");
		house.setOnAction(new LabelHandler((Label) control, block, "House", this));
		MenuItem apartments = new MenuItem("Apartments");
		apartments.setOnAction(new LabelHandler((Label) control, block, "ApartmentBuilding", this));
		residential.getItems().addAll(house, apartments);

		// commercial buildings
		Menu commercial = new Menu("Commercial");
		MenuItem store = new MenuItem("Store");
		store.setOnAction(new LabelHandler((Label) control, block, "Store", this));
		MenuItem shoppingMall = new MenuItem("Shopping Mall");
		shoppingMall.setOnAction(new LabelHandler((Label) control, block, "ShoppingMall", this));
		commercial.getItems().addAll(store, shoppingMall);

		// industrial buildings
		Menu industrial = new Menu("Industrial");
		MenuItem factory = new MenuItem("Factory");
		factory.setOnAction(new LabelHandler((Label) control, block, "Factory", this));	
		MenuItem workshop = new MenuItem("Workshop");
		workshop.setOnAction(new LabelHandler((Label) control, block, "Workshop", this));
		industrial.getItems().addAll(workshop, factory);

		// municipal buildings
		Menu municipal = new Menu("Municipal");
		MenuItem policeStation = new MenuItem("Police Station");
		policeStation.setOnAction(new LabelHandler((Label) control, block, "PoliceStation", this));	
		MenuItem fireStation = new MenuItem("Fire Station");
		fireStation.setOnAction(new LabelHandler((Label) control, block, "FireStation", this));	

		MenuItem hospital = new MenuItem("Hospital");
		hospital.setOnAction(new LabelHandler((Label) control, block, "Hospital", this));	

		MenuItem smallPark = new MenuItem("Small park");
		smallPark.setOnAction(new LabelHandler((Label) control, block, "SmallPark", this));	
		MenuItem largePark = new MenuItem("Large park");
		largePark.setOnAction(new LabelHandler((Label) control, block, "LargePark", this));	
		MenuItem sportsCenter = new MenuItem("Sports center");
		sportsCenter.setOnAction(new LabelHandler((Label) control, block, "SportsCenter", this));	
		municipal.getItems().addAll(policeStation, fireStation, new SeparatorMenuItem(), hospital,
				new SeparatorMenuItem(), smallPark, largePark, sportsCenter);

		MenuItem demolish = new MenuItem("Demolish");
		demolish.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Demolish");
		    }
		});
		construct.getItems().addAll(residential, commercial, industrial, municipal);
		contextMenu.getItems().addAll(info, new SeparatorMenuItem(), construct, demolish);
		
        control.setContextMenu(contextMenu);
		
	}
	
	private void initialise()
	{
		panelGrid = new Label[this.width][this.height];
		
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
	}
	
	public void placeBuilding(Building b)
	{
		Point p = b.getLocation().getLocation();
		for (int column=0; column < b.getWidth(); column++)
			for (int row=0; row < b.getHeight(); row++)
			{
				StackPane spCell = (StackPane) this.getNodeByRowColumnIndex(p.x + column, p.y + row, gridPane);
                spCell.setStyle("-fx-background-color: blue; -fx-border-color: blue;"); 
                ((Label) spCell.getChildren().get(0)).setTextFill(Color.web("#FFFFFF"));

				panelGrid[p.x + column][p.y + row].setText(b.getAbbrev());
			}
		
	}
	
	@SuppressWarnings("static-access")
	public Node getNodeByRowColumnIndex (final int column, final int row, GridPane gridPane) {
	    Node result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();

	    for (Node node : childrens) {
	        if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
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
        gridPane = new GridPane(); 
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
            for (int row = 0; row < height; row++) 
            { 
                StackPane spCell = new StackPane();
                String abbrev = engine.getBlock(column, row).getUsage();
                Label lb = new Label(abbrev);
                
                panelGrid[column][row] = lb;
                
                if (!abbrev.equals("Wa"))
                	addContextMenu(lb, (LandBlock) engine.getBlock(column, row));

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