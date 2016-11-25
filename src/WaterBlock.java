import java.awt.Point;

/**
 * A water block on which nothing can be built
 * 
 * @author dwgreenidge
 * @see GeoBlock
 *
 */
public class WaterBlock extends GeoBlock {
	WaterBlock(Point p)
	{
		super(p);
	}
	
	@Override
	public String getUsage()
	{
		return "Wa";
	}
}
