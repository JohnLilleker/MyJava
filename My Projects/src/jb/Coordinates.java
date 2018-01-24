package jb;

/**
 * A set of Coordinates, or a point in 2d integer space to be exact. Used in SimpleImage and the Board game components
 * @author JB227
 *
 */
/*
 * Created 25/11/2017
 * Updated 27/11/2017 (same(), empty constructor and JavaDoc)
 */
public class Coordinates {
	private int x;
	private int y;
	
	/**
	 * Creates a (0,0) Coordinates object
	 */
	public Coordinates() {
		this(0, 0);
	}
	
	/**
	 * A Simple Coordinates object
	 * @param x x Coordinate
	 * @param y y Coordinate
	 */
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Accessor for the x coordinate
	 * @return The x coordinate
	 */
	public int getX() { return x; }
	/**
	 * Accessor for the y coordinate
	 * @return The y coordinate
	 */
	public int getY() { return y; }
	
	/**
	 * Mutator for the x coordinate
	 * @param x The new x coordinate
	 */
	public void setX(int x) { this.x = x; }
	/**
	 * Mutator for the y coordinate
	 * @param y The new y coordinate
	 */
	public void setY(int y) { this.y = y; }
	
	/**
	 * In place Mathematical addition of 2 coordinates
	 * @param co The other coordinate
	 * @return A reference to this object
	 */
	public Coordinates addTo(Coordinates co) {
		this.x += co.getX();
		this.y += co.getY();
		return this;
	}
	/**
	 * In place Mathematical addition of 2 coordinates, but the other coordinate is defined by 2 integers
	 * @param x The x coordinate of the other Coordinates 'object'
	 * @param y The y coordinate of the other Coordinates 'object'
	 * @return A reference to this object
	 */
	public Coordinates addTo(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * In place Mathematical Subtraction of 2 coordinates
	 * @param co The other coordinate
	 * @return a reference to this object
	 */
	public Coordinates subFrom(Coordinates co) {
		this.x -= co.getX();
		this.y -= co.getY();
		return this;
	}
	/**
	 * In place Mathematical subtraction of 2 coordinates, but the other coordinate is defined by 2 integers
	 * @param x The x coordinate of the other Coordinates 'object'
	 * @param y The y coordinate of the other Coordinates 'object'
	 * @return a reference to this object
	 */
	public Coordinates subFrom(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Multiplies the Coordinates object by the given scalar value in place
	 * @param m An integer to multiply with
	 * @return a reference to this object
	 */
	public Coordinates mulTo(int m) {
		this.x *= m;
		this.y *= m;
		return this;
	}
	/**
	 * Divides the Coordinates object by the given scalar value in place<br>Note it is integer division, the remainder is lost
	 * @param d An integer to divide by
	 * @return a reference to this object
	 */
	public Coordinates divFrom(int d) {
		this.x /= d;
		this.y /= d;
		return this;
	}
	
	/**
	 * Mathematical addition of 2 coordinates
	 * @param co The other coordinate
	 * @return New Coordinates object such that <code>{x -> getX()+co.getX(), y -> getY()+co.getY()}</code>
	 */
	public Coordinates add(Coordinates co) {
		return new Coordinates(x + co.getX(), y + co.getY());
	}
	/**
	 * Mathematical addition of 2 coordinates, but the other coordinate is defined by 2 integers
	 * @param x The x coordinate of the other Coordinates 'object'
	 * @param y The y coordinate of the other Coordinates 'object'
	 * @return New Coordinates object such that <code>{x -> getX()+x, y -> getY()+y)}</code>
	 */
	public Coordinates add(int x, int y) {
		return new Coordinates(x + getX(), y + getY());
	}
	
	/**
	 * Mathematical Subtraction of 2 coordinates
	 * @param co The other coordinate
	 * @return New Coordinates object such that <code>{x -> getX()-co.getX(), y -> getY()-co.getY()}</code>
	 */
	public Coordinates sub(Coordinates co) {
		return new Coordinates(x - co.getX(), y - co.getY());
	}
	/**
	 * Mathematical subtraction of 2 coordinates, but the other coordinate is defined by 2 integers
	 * @param x The x coordinate of the other Coordinates 'object'
	 * @param y The y coordinate of the other Coordinates 'object'
	 * @return New Coordinates object such that <code>{x -> getX()-x, y -> getY()-y)}</code>
	 */
	public Coordinates sub(int x, int y) {
		return new Coordinates(getX() - x, getY() - y);
	}
	
	/**
	 * Multiplies the Coordinates object by the given scalar value
	 * @param m An integer to multiply with
	 * @return New Coordinates object such that <code>{x -> getX()*m, y -> getY()*m)}</code>
	 */
	public Coordinates mul(int m) {
		return new Coordinates(x * m, y * m);
	}
	/**
	 * Divides the Coordinates object by the given scalar value<br>Note it is integer division, the remainder is lost
	 * @param d An integer to divide by
	 * @return New Coordinates object such that <code>{x -> getX()/d, y -> getY()/d)}</code>
	 */
	public Coordinates div(int d) {
		return new Coordinates(x / d, y / d);
	}
	
	/**
	 * Compares 2 Coordinates to check if they are equal
	 * @param co The other Coordinates object
	 * @return true iff <code>getX() == co.getX() && getY() == co.getY()</code> false otherwise
	 */
	public boolean same(Coordinates co) {
		return x == co.getX() && y == co.getY();
	}
	
	/**
	 * Calculates Euclidian distance between this Coordinates object and another
	 * @param co The other Coordinates object
	 * @return The Euclidean distance as a double
	 */
	public double dist(Coordinates co) {
		int sqx = (this.x - co.getX()) * (this.x - co.getX());
		int sqy = (this.y - co.getY()) * (this.y - co.getY());
		return Math.sqrt((double) (sqx + sqy));
	}
	
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}
