package jb;

/*
 * Created: 4/9/2014
 * Last Modified: 22/1/2015 (move verifies itself)
 */
/**
 * A basic piece for a game, can be extended to owt really
 * 
 * @author JB227
 */
public abstract class Counter {

	protected int xCoord;
	protected int yCoord;
	protected String marker;
	
	public Counter(String m) {
		this(0, 0, m);
	}

	public Counter(int x, int y, String m) {
		this.xCoord = x;
		this.yCoord = y;
		this.marker = m;
	}
	
	public Counter(Coordinates co, String m) {
		this.xCoord = co.getX();
		this.yCoord = co.getY();
		this.marker = m;
	}

	/**
	 * Changes the coordinates of the Counter, if the move doesn't leave the
	 * board.
	 * 
	 * @param x
	 *            new x
	 * @param y
	 *            new y
	 * @param game
	 *            the board, the Counter is updated on it.
	 */
	public void move(int x, int y, Board game) {

		if (!game.outOfRange(x, y)) {
			game.getCell(xCoord, yCoord).remove(marker);
			game.getCell(x, y).place(this);
		}
	}
	
	public void move(Coordinates co, Board game) {
		move(co.getX(), co.getY(), game);
	}

	/**
	 * Should return the coordinates in an object
	 * 
	 * @return the coordinates object with the location
	 */
	public Coordinates find() {
		return new Coordinates(xCoord, yCoord);
	}

	/**
	 * The Cell should have a String to identify it
	 * 
	 * @return the String
	 */
	public String identify() {
		return marker;
	}

	public abstract Counter copy();
	
	public void setLocation(int x, int y) {
		xCoord = x;
		yCoord = y;
	}
}
