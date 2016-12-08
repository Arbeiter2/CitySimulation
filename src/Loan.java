/**
 * An object representing a loan, with simple interest, and 
 * no early repayment option
 * 
 * @author dwgreenidge
 *
 */
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Loan 
{
	public static final double ANNUAL_INTEREST_RATE = 0.1;
	
	SimpleDoubleProperty principal;
	SimpleDoubleProperty currentValue;
	SimpleIntegerProperty termInYears;
	SimpleIntegerProperty termRemaining;
	SimpleIntegerProperty currentMonth;
	SimpleDoubleProperty monthlyPayment;
	
	/**
	 * Deduct monthly payment from balance
	 * 
	 * @return loan balance
	 */
	public double makeMonthlyPayment()
	{
		if (currentValue.get() <= 0d)
			return currentValue.get();
		
		currentValue.set(Math.max(0d, currentValue.get() - monthlyPayment.get()));
		currentMonth.set(currentMonth.get() + 1);
		termRemaining.set(termRemaining.get() - 1);
		
		return currentValue.get();
	}

	/**
	 * Value of monthly payment
	 * 
	 * @return
	 */
	public double getMonthlyPayment()
	{
		return monthlyPayment.get();
	}
	
	/**
	 * Get original loan value
	 * 
	 * @return
	 */
	public double getPrincipal()
	{
		return principal.get();
	}
	
	public double getBalance()
	{
		return currentValue.get();
	}
	
	public int getTermInYears()
	{
		return termInYears.get();
	}

	
	public int getTermRemaining()
	{
		return termRemaining.get();
	}
	
	public static double calculateMonthlyPayment(double loanValue, int term)
	{
		return loanValue * Math.pow(1d + ANNUAL_INTEREST_RATE, term)/(term * 12);
	}
	
	Loan(double value, int term) 
	{
		principal = new SimpleDoubleProperty(value); 
		currentValue = new SimpleDoubleProperty(value);
		termInYears = new SimpleIntegerProperty(term);
		termRemaining = new SimpleIntegerProperty(term * 12);
		currentMonth = new SimpleIntegerProperty(0);
		monthlyPayment = new SimpleDoubleProperty(Loan.calculateMonthlyPayment(principal.get(), termInYears.get()));
	}
}
