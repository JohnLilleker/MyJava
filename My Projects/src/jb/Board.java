package jb;

/*
 * Created: 4/9/2014
 * Last Modified: 8/9/2017 (clear)
 */
/**
 * A 2d array of Cells, can be diversified to many different games such as chess
 * or snakes-and-ladders
 * 
 * @author JB227
 */
public abstract class Board {

	protected Cell[][] board;
	protected int height;
	protected int width;

	/**
	 * Empty 2d array of Cells
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 */
	public Board(int h, int w) {
		board = new Cell[h][w];
		height = h;
		width = w;
	}

	/**
	 * Useful for copy(), directly sets the board
	 * 
	 * @param b
	 *            the board
	 */
	protected Board(Cell[][] b) {
		this.board = b;
		height = b.length;
		width = b[0].length;
	}

	/**
	 * The size of the Board
	 * 
	 * @return [0] = height, [1] = width
	 */
	public int[] getSize() {
		return new int[] {height, width};
	}
	
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}

	/**
	 * Gives access to a Cell using it's coordinates
	 * 
	 * @param x
	 * @param y
	 *            the coordinates
	 * @return the Cell
	 */
	public Cell getCell(int x, int y) {
		return board[y][x];
	}
	
	public Cell getCell(Coordinates co) {
		return getCell(co.getX(), co.getY());
	}

	/**
	 * Tests coordinates are out of bounds
	 * 
	 * @param x
	 * @param y
	 *            the trial coordinates
	 * @return true if the point is off the board
	 */
	public boolean outOfRange(int x, int y) {
		boolean ret = false;

		try { // clever, right?
			getCell(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
			ret = true;
		}
		return ret;
	}
	
	public boolean outOfRange(Coordinates co) {
		return outOfRange(co.getX(), co.getY());
	}

	/**
	 * Creates a carbon copy of the Board
	 * 
	 * @return the copy
	 */
	public abstract Board copy();

	/**
	 * Calls the toString of each cell, so best to implement it in the Cell
	 * extension
	 */
	public String toString() {
		String mega = "";
		for (Cell[] row : board) {
			for (Cell c : row) {
				mega += c;
			}
			mega += "\n";
		}
		return mega;
	}
	
	/**
	 * Clears the board of all counters
	 */
	public void clear() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				getCell(x,y).clear();
			}
		}
	}
}
