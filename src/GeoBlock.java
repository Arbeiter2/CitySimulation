import java.awt.Point;

/**
 * A block of size 50m x 50m in the simulation
 * 
 * @author dwgreenidge
 *
 */
public class GeoBlock {

	private	Point location;

	public Point getLocation() {
		return location;
	}

	public GeoBlock(Point p)
	{
		location = p;
	}
}
