
/*
CLASS: Point
DESCRIPTION: Ah, if only real-life classes were this straight-forward. We'll
             use 'Point' throughout the program to store and access 
             coordinates.
Original code by Dan Leyzberg and Art Simon
 */

public class Point implements Cloneable {
	/**
	 * The x and y coordinates of the Point object.
	 *
	 * Ordinarily, these would be private, but it makes for cleaner code
	 * by allowing the (x,y) coordinates to be accessed in other classes
	 */

	double x;
	double y;

	public Point(double x, double y) 
	{ 
		this.x = x; this.y = y; 
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Point clone() {
		return new Point(x, y);
	}
}