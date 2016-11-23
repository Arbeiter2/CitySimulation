/**
 * @author dwgreenidge
 *
 */
public class Loan 
{
	public static final double ANNUAL_INTEREST_RATE = 0.1;
	
	double initialValue;
	double currentValue;
	int term;
	int currentMonth;
	double monthlyPayment;
	
	public void makeMonthlyPayment()
	{
		if (currentValue <= 0d)
			return;
		
		currentValue -= monthlyPayment;
		currentMonth++;
	}

	public double getMonthlyPayment()
	{
		return monthlyPayment;
	}
	
	public double getInitialValue()
	{
		return initialValue;
	}
	
	public double getBalance()
	{
		return currentValue;
	}
	
	public int getTerm()
	{
		return term;
	}
	
	Loan(double value, int termInYears) 
	{
		initialValue = currentValue = value;
		term = termInYears;
		currentMonth = 0;
		monthlyPayment = initialValue * Math.pow(1d + ANNUAL_INTEREST_RATE, term)/(term * 12);
	}
}
