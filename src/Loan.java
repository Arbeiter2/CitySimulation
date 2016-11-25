/**
 * An object representing a loan, with simple interest, and 
 * no early repayment option
 * 
 * @author dwgreenidge
 *
 */
public class Loan 
{
	public static final double ANNUAL_INTEREST_RATE = 0.1;
	
	double principal;
	double currentValue;
	int termInYears;
	int currentMonth;
	double monthlyPayment;
	
	/**
	 * Deduct monthly payment from balance
	 * 
	 * @return loan balance
	 */
	public double makeMonthlyPayment()
	{
		if (currentValue <= 0d)
			return currentValue;
		
		currentValue -= Math.max(0d, currentValue - monthlyPayment);
		currentMonth++;
		
		return currentValue;
	}

	/**
	 * Value of monthly payment
	 * 
	 * @return
	 */
	public double getMonthlyPayment()
	{
		return monthlyPayment;
	}
	
	/**
	 * Get original loan value
	 * 
	 * @return
	 */
	public double getPrincipal()
	{
		return principal;
	}
	
	public double getBalance()
	{
		return currentValue;
	}
	
	public int getTermInYears()
	{
		return termInYears;
	}

	
	public int getRemainingTerm()
	{
		return termInYears * 12 - currentMonth;
	}
	
	Loan(double value, int term) 
	{
		principal = currentValue = value;
		termInYears = term;
		currentMonth = 0;
		monthlyPayment = principal * Math.pow(1d + ANNUAL_INTEREST_RATE, termInYears)/(termInYears * 12);
	}
}
