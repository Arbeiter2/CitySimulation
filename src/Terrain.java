/**
 * Different terrain types for LandBlocks
 * 
 * @author dwgreenidge
 *
 */
public class Terrain 
{
	// construction cost multiplier for each terrain type
	public static final double FOREST_MULTIPLIER = 1.15;
	public static final double GRASS_MULTIPLIER = 1.0;
	public static final double ROCK_MULTIPLIER = 1.5;
	public static final double SWAMP_MULTIPLIER = 2.0;
	public static final double VOLCANO_MULTIPLIER = 3.0;
	
	public static enum Type 
	{ 
		FOREST, GRASS, ROCK, SWAMP, VOLCANO 
	};
	
	/**
	 * Get construction cost multiplier for given terrain type
	 * 
	 * @param t terrain type
	 * @return cost multiplier
	 */
	public static double getConstructionMultiplier(Type t)
	{
		double m = 1d;
		switch (t)
		{
		case FOREST:
			m = FOREST_MULTIPLIER;
			break;
		case GRASS:
			m = GRASS_MULTIPLIER;
			break;
		case ROCK:
			m = ROCK_MULTIPLIER;
			break;
		case SWAMP:
			m = SWAMP_MULTIPLIER;
			break;
		case VOLCANO:
			m = VOLCANO_MULTIPLIER;
			break;
		};
		return m;
	}
}