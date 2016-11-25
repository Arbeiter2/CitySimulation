import java.awt.Point;

/**
 * A block of size 50m x 50m in the simulation
 * 
 * @author dwgreenidge
 *
 */
abstract public class GeoBlock {

	private	Point location;

	public Point getLocation() {
		return location;
	}

	public GeoBlock(Point p)
	{
		location = p;
	}
	
	/**
	 * Short (max 2 characters) string representation of block or contents
	 *  
	 * @return
	 */
	public abstract String getUsage();
}
