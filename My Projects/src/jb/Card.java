package jb;

/* Created 21/6/2014
 * Last modified 21/6/2014
 */

/**
 * A basic Card, can become pretty much anything from a Playing card to a
 * Pokemon card
 * 
 * @author JB227
 *
 */
public abstract class Card {

	private String value;

	public Card(String n) {
		this.value = n;
	}

	public String getValue() {
		return value;
	}

	/**
	 * @return A deep copy of the card
	 */
	public abstract Card copy();

}
