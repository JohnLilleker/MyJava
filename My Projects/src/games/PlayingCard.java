package games;

/* Created 13/6/2014
 * Last modified 21/6/2014 (colour())
 */
public class PlayingCard {

	private String value;
	private String suit;

	public PlayingCard(String v, String s) {
		this.value = v;
		this.suit = s;
	}

	public String getValue() {
		return value;
	}

	public String getSuit() {
		return suit;
	}

	public String toString() {
		return value + " of " + suit;
	}

	// Simple cards e.g "|A -8>|"
	public String card() {

		String card;

		if (!value.equals("10"))
			card = "|" + value.charAt(0) + "  ";
		else
			card = "|" + value + " ";

		switch (this.suit) {

		case "hearts":
			card += "<3 ";
			break;

		case "spades":
			card += "-8>";
			break;

		case "diamonds":
			card += "<> ";
			break;

		case "clubs":
			card += "-8o";
			break;
		}
		card += "|";

		return card;
	}

	public String colour() {
		if (this.suit.equals("clubs") || this.suit.equals("spades"))
			return "black";
		return "red";
	}

}
