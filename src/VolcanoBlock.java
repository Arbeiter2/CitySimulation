import java.awt.Point;

/**
 * A special LandBlock for volcanoes
 * 
 * @author dwgreenidge
 *
 */
public class VolcanoBlock extends LandBlock {

	VolcanoBlock(Point p)
	{
		super(p, Terrain.Type.VOLCANO);
	}
}
