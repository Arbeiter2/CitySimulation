import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.scene.control.TextInputControl;

import javafx.scene.control.cell.PropertyValueFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.stage.Modality;

public class LoanDialog {

	TextField repayment = new TextField();
	CitySimulation citySim;
	
	public LoanDialog(CitySimulation sim, TextInputControl text) 
	{
		citySim = sim;
		Dialog<Loan> dialog = new Dialog<>();
		final TextInputControl target = text;
		
		dialog.setTitle("New Loan");
		dialog.setHeaderText("Select loan value and term");
		dialog.setResizable(true);
		dialog.initModality(Modality.APPLICATION_MODAL);

		Label label0 = new Label("Current loans");
		Label label1 = new Label("Loan value: ");
		Label label2 = new Label("Term (years): ");
		Label label3 = new Label("Monthly repayment: ");


		TableView<Loan> table = new TableView<Loan>();
		table.setMaxHeight(115);
		
		TableColumn<Loan, Double> loanPrincipal = new TableColumn<Loan, Double>("Principal");
		TableColumn<Loan, Integer> loanTerm = new TableColumn<Loan, Integer>("Term\n(Yrs)");
		TableColumn<Loan, Double> loanBalance = new TableColumn<Loan, Double>("Outstanding");
		TableColumn<Loan, Integer> loanRemaining = new TableColumn<Loan, Integer>("Months\nleft");
		TableColumn<Loan, Double> loanPayment = new TableColumn<Loan, Double>("Repayment");

		loanPrincipal.setCellValueFactory(
			    new PropertyValueFactory<Loan,Double>("principal")
			);
		loanTerm.setCellValueFactory(
			    new PropertyValueFactory<Loan,Integer>("termInYears")
			);
		loanBalance.setCellValueFactory(
			    new PropertyValueFactory<Loan,Double>("balance")
			);		
		loanRemaining.setCellValueFactory(
			    new PropertyValueFactory<Loan,Integer>("termRemaining")
			);		
		loanPayment.setCellValueFactory(
			    new PropertyValueFactory<Loan,Double>("monthlyPayment")
			);

		loanPrincipal.setCellFactory(new Callback<TableColumn<Loan,Double>, TableCell<Loan,Double>>() {
            public TableCell<Loan,Double> call(TableColumn<Loan,Double> p) {
                TableCell<Loan,Double> cell = new DecimalTableCell<Loan, Double>();
                cell.setStyle("-fx-alignment: top-right;");
                return cell;
            }
        });

		loanBalance.setCellFactory(new Callback<TableColumn<Loan,Double>, TableCell<Loan,Double>>() {
            public TableCell<Loan,Double> call(TableColumn<Loan,Double> p) {
                TableCell<Loan,Double> cell = new DecimalTableCell<Loan, Double>();
                cell.setStyle("-fx-alignment: top-right;");
                return cell;
            }
        });
		
		loanPayment.setCellFactory(new Callback<TableColumn<Loan,Double>, TableCell<Loan,Double>>() {
            public TableCell<Loan,Double> call(TableColumn<Loan,Double> p) {
                TableCell<Loan,Double> cell = new DecimalTableCell<Loan, Double>();
                cell.setStyle("-fx-alignment: top-right;");
                return cell;
            }
        });
		
		table.getColumns().addAll(loanPrincipal, loanTerm, loanBalance, loanRemaining, loanPayment);

		ObservableList<Loan> loanList = FXCollections.observableArrayList(citySim.getLoans()); 
		table.setItems(loanList); 
		
		
		final ComboBox<String> loanValues = new ComboBox<String>();
		loanValues.getItems().addAll(
			"1,000,000", 
			"2,000,000", 
			"3,000,000", 
			"4,000,000", 
			"5,000,000", 
			"10,000,000",
			"12,000,000",
			"15,000,000"
		);

		final ComboBox<String> loanTerms = new ComboBox<String>();
		loanTerms.getItems().addAll(
			"1", 
			"2", 
			"3", 
			"5",
			"6",
			"7",
			"8",
			"9",
			"10"
		);		

		repayment.setEditable(false);
		repayment.setMaxWidth(60);
		repayment.setPrefWidth(60);
		
		loanValues.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	updatePaymentValue(loanValues.getValue(), loanTerms.getValue());
	        }    
	    });

		loanTerms.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) 
	        {
	        	updatePaymentValue(loanValues.getValue(), loanTerms.getValue());
	        }    
	    });
		
		GridPane grid = new GridPane();
		
		grid.add(label0, 1, 1, 1, 2);
		grid.add(table, 2, 1);
		grid.add(label1, 1, 2);
		grid.add(loanValues, 2, 2);
		grid.add(label2, 1, 3);
		grid.add(loanTerms, 2, 3);
		grid.add(label3, 1, 4);
		grid.add(repayment, 2, 4);

		dialog.getDialogPane().setContent(grid);
				
		final ButtonType buttonTypeOk = new ButtonType("Accept", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		dialog.setResultConverter(new Callback<ButtonType, Loan>() {
		    @Override
		    public Loan call(ButtonType b) {

		        if (b == buttonTypeOk) 
		        {
		    		double val = Double.parseDouble(loanValues.getValue().replaceAll(",", ""));
		    		int term = Integer.parseInt(loanTerms.getValue());
		    		citySim.startLoan(val, term);
		    		target.setText(String.format("%.2f", citySim.getBankBalance()));
		        }

		        return null;
		    }
		});
		dialog.show();
	}



	void updatePaymentValue(String valString, String termString)
	{
		if (valString == null || termString == null)
		{
			repayment.setText("");
			return;
		}
		double val = Double.parseDouble(valString.replaceAll(",", ""));
		int term = Integer.parseInt(termString);
		
		if (val > 0d && term > 0)
			repayment.setText(Integer.toString((int)Loan.calculateMonthlyPayment(val, term)));
	}
	
	class DecimalTableCell<S, T> extends TableCell<S, T>
	{
		@Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }

        private String getString() {
            String ret = "";
            if (getItem() != null) {
                String gi = getItem().toString();
                NumberFormat df = DecimalFormat.getInstance();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                df.setRoundingMode(RoundingMode.DOWN);

                ret = df.format(Double.parseDouble(gi));
            } else {
                ret = "0.00";
            }
            return ret;
        }
    };
}