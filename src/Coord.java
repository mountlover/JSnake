/**
 * <b>Coord</b> - Stores coordinates.
 * @author Yama H
 * @version 1.0
 */
public class Coord
{
	private int x;
	private int y;
	/**
	 * Constructor for objects of class Coord
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Coord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	/**
	 * Sets X coordinate
	 * @param x new X value
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * Sets Y coordinate
	 * @param y new Y value
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	/**
	 * Returns X coordinate
	 * @return X coordinate
	 */
	public int getX()
	{
		return x;
	}
	/**
	 * Returns Y coordinate
	 * @return Y coordinate
	 */
	public int getY()
	{
		return y;
	}
	public String toString()
	{
	    return "(" + getX() + ", " + getY() + ")";
	}
}