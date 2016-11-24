// package bugs;

import javafx.application.Application;
import javafx.geometry.Insets; 
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.ColumnConstraints; 
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.RowConstraints; 
import javafx.scene.layout.StackPane; 
import javafx.stage.Stage; 

public class GridPaneTest extends Application { 

    @Override 
    public void start(Stage primaryStage) { 

        StackPane stackPane = new StackPane(); 
        stackPane.setPadding(new Insets(8)); 
        stackPane.setPrefWidth(646); 
        stackPane.setPrefHeight(480); 
        stackPane.getChildren().add(setupGridPane()); 

        Scene scene = new Scene(stackPane); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }

    @SuppressWarnings("unchecked")
	private GridPane setupGridPane() { 
        GridPane gridPane = new GridPane(); 
        gridPane.setStyle("-fx-border-color: black;"); 

        for (int column = 0; column < 20; column++) { 
            final ColumnConstraints columnConstraints = new ColumnConstraints(); 
            columnConstraints.setPercentWidth(20); 
            gridPane.getColumnConstraints().add(columnConstraints); 
        } 

        for (int row = 0; row < 20; row++) { 
            final RowConstraints rowConstraints = new RowConstraints(); 
            rowConstraints.setPercentHeight(20); 
            gridPane.getRowConstraints().add(rowConstraints); 
        } 

        for (int column = 0; column < 20; column++) { 
            for (int row = 0; row < 20; row++) { 
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