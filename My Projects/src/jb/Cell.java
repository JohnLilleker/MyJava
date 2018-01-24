package jb;

import java.util.ArrayList;

/*
 * Created: 4/9/2014
 * Last Modified: 8/9/2017 (clear)
 */
/**
 * An object which may contain 1 or more Counter.
 * 
 * @author JB227
 *
 */
public abstract class Cell {

	protected ArrayList<Counter> room = new ArrayList<>(); // multiple counters
	protected int xCoord;
	protected int yCoord;
	
	public Cell(int x, int y) {
		xCoord = x;
		yCoord = y;
	}
	
	public Cell(Coordinates co) {
		xCoord = co.getX();
		yCoord = co.getY();
	}
	
	/**
	 * Gets a specific counter from the cell
	 * 
	 * @param id
	 *            the identification String
	 * @return the Counter if present, null otherwise
	 */
	public Counter getCounter(String id) {
		for (Counter c : room) {
			if (c.identify().equalsIgnoreCase(id))
				return c;
		}
		return null;
	}

	/**
	 * Adds a counter to the Cell
	 * 
	 * @param c
	 *            the Counter
	 */
	public void place(Counter c) {
		room.add(c);
		c.setLocation(xCoord, yCoord);
	}

	/**
	 * Removes a Counter from a cell
	 * 
	 * @param id
	 *            the Counter id
	 * @return the Counter
	 */
	public Counter remove(String id) {
		for (Counter c : room) {
			if (c.identify().equalsIgnoreCase(id)) {
				room.remove(c);
				return c;
			}
		}
		return null;
	}

	/**
	 * Is the cell empty?
	 * 
	 * @return true if there's nowt in it
	 */
	public boolean isEmpty() {
		return room.isEmpty();
	}

	/**
	 * Checks the cell has a particular counter
	 * 
	 * @param id
	 *            the marker
	 * @return true if yes, false otherwise
	 */
	public boolean contains(String id) {
		for (Counter c : room) {
			if (c.identify().equalsIgnoreCase(id))
				return true;
		}
		return false;
	}
	
	/**
	 * Get an array of the ids of every Counter in this Cell
	 * @return an array of strings, each is a description of a Counter
	 */
	public String[] counterIDs() {
		String[] ids = new String[room.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = room.get(i).identify();
		}
		return ids;
	}
	
	/**
	 * Completely empties the cell
	 */
	public void clear() {
		room.clear();
	}

	/**
	 * Creates a carbon copy of the cell and whatever is inside it
	 * 
	 * @return the copy
	 */
	public abstract Cell copy();
}
