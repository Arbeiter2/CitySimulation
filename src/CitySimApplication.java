// package bugs;

import javafx.scene.control.Button;
import java.awt.Point;
import javafx.scene.control.TextArea;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;


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
	HBox toolbar = new HBox();
	TextField budgetDisplay = new TextField();
	TextField monthDisplay = new TextField();
	HBox statusbar = new HBox();
	TextArea statusText = new TextArea();
	Button endTurnBtn = new Button("End turn");
	
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


		BorderPane borderPane = new BorderPane();
		borderPane.setTop(toolbar);
		borderPane.setCenter(setupGridPane());
		borderPane.setBottom(statusbar);
	    statusbar.setPadding(new Insets(15, 12, 15, 12));
	    statusbar.setSpacing(10);
		statusbar.getChildren().addAll(new Label("Info"), statusText, endTurnBtn);
	    
	    statusText.setEditable(false);
		statusText.setPrefRowCount(10);
		statusText.setPrefColumnCount(30);
		statusText.setMaxWidth(330);

		budgetDisplay.setText(String.format("%.2f", engine.getBankBalance())); 
		budgetDisplay.setEditable(false);
		monthDisplay.setText(Integer.toString(engine.getCurrentMonth())); 
		monthDisplay.setEditable(false);
		monthDisplay.setMaxWidth(60);
		monthDisplay.setPrefWidth(60);
		
		toolbar.setPadding(new Insets(15, 12, 15, 12));
		toolbar.setSpacing(10);
		Button b = new Button("Loans");
		b.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	LoanDialog loanDlg = new LoanDialog(engine, budgetDisplay);
		    }
		});
		
		toolbar.getChildren().addAll(new Label("Budget"), budgetDisplay, new Label("Month"), monthDisplay, b);
		
		endTurnBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		        engine.tick();
		        budgetDisplay.setText(String.format("%.2f", engine.getBankBalance()));
				monthDisplay.setText(Integer.toString(engine.getCurrentMonth())); 
		    }
		});
		

        Scene scene = new Scene(borderPane); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }
	
	
	
	public void setStatuxText(String msg)
	{
		statusText.setText(msg);
	}
	
	private void addContextMenu(Control control, LandBlock block)
	{
		ContextMenu contextMenu = new ContextMenu();
		
		MenuItem info = new MenuItem("Info");
		info.setOnAction(new InfoHandler(block, this));

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
		demolish.setOnAction(new DemolitionHandler(block, this));

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
		budgetDisplay.setText(Double.toString(engine.getBankBalance())); 
		
	}

	
	public void demolishBuilding(LandBlock b)
	{
		if (b == null)
			return;

		Building bldg = b.getConstruction();
		if (bldg == null)
			return;
		Point p = bldg.getLocation().getLocation();
		engine.demolishBuilding(bldg);
		
		for (int column=0; column < bldg.getWidth(); column++)
			for (int row=0; row < bldg.getHeight(); row++)
			{
				String abbrev = ((LandBlock) engine.getBlock(p.x + column, p.y + row)).getUsage();
                
				StackPane spCell = (StackPane) this.getNodeByRowColumnIndex(p.x + column, p.y + row, gridPane);
                spCell.setStyle("-fx-background-color: " + blockColours.get(abbrev) +"; -fx-border-color: red"); 

                ((Label) spCell.getChildren().get(0)).setTextFill(Color.web(textColours.get(abbrev)));
				panelGrid[p.x + column][p.y + row].setText(abbrev);
			}
		budgetDisplay.setText(Double.toString(engine.getBankBalance())); 

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
    	String help = args.get("help");
    	
    	if (help != null)
    	{
    		getUsage();
    		
    	}

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
		{
			if (!engine.loadMapFile(mapFilePath))
			{
				
			}
		}
			
    }
    
    void getUsage()
    {
    	StringBuilder b = new StringBuilder();
    	b.append("\t--height=<map-height> (default: " + CitySimulation.MIN_GRID_HEIGHT  + ")\n");
    	b.append("\t--width=<map-width> (default: " + CitySimulation.MIN_GRID_WIDTH  + ")\n");
    	b.append("\t--budget=<starting-budget> (default: " + CitySimulation.DEFAULT_INITIAL_BUDGET + "\n");
    	b.append("\t--mapfile=<filepath>\n");
    	
    	System.out.println(b.toString());
    	
        Platform.exit();
        System.exit(0);    	
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