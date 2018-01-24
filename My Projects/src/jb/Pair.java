package jb;

/*
 * Created: 25/1/2015
 * Last modified: 25/1/2015
 */

/**
 * Similar to tuples in other languages, but in the rigid Java way. Useful if
 * you want 2 objects of different classes together
 * 
 * @author JB227
 */
public class Pair<E, K> {
	private E first;
	private K second;

	public Pair(E fst, K snd) {
		this.first = fst;
		this.second = snd;
	}

	/**
	 * Gets the first object
	 */
	public E first() {
		return first;
	}

	/**
	 * Gets the second object
	 */
	public K second() {
		return second;
	}

	/**
	 * Sets the first object
	 */
	public void first(E fst) {
		this.first = fst;
	}

	/**
	 * Sets the second object
	 */
	public void second(K snd) {
		this.second = snd;
	}

	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}
}
