package jb;

/* Created 21/6/2014
 * Last modified 30/1/2015 (finally, a toString)
 */
/**
 * A die, for use with other games
 * 
 * @author JB227
 */
public class Die {

	private int value = 0;
	private int sides;

	/**
	 * Standard die with 6 sides
	 */
	public Die() {
		this(6);
	}

	/**
	 * 
	 * @param s
	 *            the number of sides
	 */
	public Die(int s) {
		this.sides = s;
	}

	/**
	 * Changes the value of the Die
	 */
	public void roll() {

		do {
			value = (int) ((Math.random() * sides) + 1);
		} while (value == (sides + 1));
	}

	/**
	 * Reveals the value of the Die
	 * 
	 * @return a number 1-the number of sides, or 0 if not rolled
	 */
	public int show() {
		return value;
	}

	public String toString() {

		String basic = "A Die with " + sides + " sides,";
		if (value == 0) {
			return basic + " unrolled";
		}
		return basic + " current value " + value;
	}

	public static void main(String[] args) {

		Die dice = new Die(10);
		Die dice2 = new Die(8);

		dice.roll();
		dice2.roll();
		System.out.println("Total: " + (dice.show() + dice2.show()));

	}
}
