
public class CitySimulationGame {

	CitySimulation citySim;
	
	public CitySimulationGame(int width, int height, int startBudget) {
		citySim = new CitySimulation(width, height, startBudget);
	}

	public static void main(String[] args) {
		CitySimulationGame sim = new CitySimulationGame(0, 0, 0);

	}

}
