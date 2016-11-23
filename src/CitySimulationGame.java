
public class CitySimulationGame {

	CitySimulation citySim;
	
	private static final int DEFAULT_HEIGHT = 20;
	private static final int DEFAULT_WIDTH = 20;
	private static final double DEFAULT_INITIAL_BUDGET = 1000000;
	
	public CitySimulationGame(int width, int height, int startBudget) {
		citySim = new CitySimulation(width, height, startBudget);
	}

	public static void main(String[] args) {
		CitySimulationGame sim = new CitySimulationGame(0, 0, 0);

	}

}
