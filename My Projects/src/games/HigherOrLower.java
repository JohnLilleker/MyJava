package games;

import java.util.*;

/* Created 13/6/2014
 * Last modified 13/6/2014
 */
public class HigherOrLower extends MicroGame {

	private String name = "Higher or Lower";
	private String watsit = "Luck";
	private boolean win = false;

	public boolean getWin() {
		return win;
	}

	public String getType() {
		return watsit;
	}

	public String getName() {
		return name;
	}

	public void play() {

		// geddit?
		Deck brucie = new Deck();
		Reader typer = new Reader();

		System.out.println("Higher or lower, with cards!");
		System.out.println("8 cards to go through, can you make it?");
		System.out.println("For each, decide wether the next card will be higher or lower");
		System.out.println("If they are the same you win anyway!");
		System.out.println("Ace is low, king is high, ready?");

		typer.read();
		brucie.shuffle();

		ArrayList<PlayingCard> board = brucie.deal(8);
		boolean kickedout = false;

		for (int c = 0; c < (board.size() - 1); c++) {

			String choice;

			do {
				choice = typer.read("Card " + (c + 1) + ", Higher or Lower? " + board.get(c).card() + " ");

			} while (!valid(choice));

			choice = choice.substring(0, 3);
			boolean high;

			if (choice.equalsIgnoreCase("hig"))
				high = true;
			else
				high = false;

			System.out.println(board.get(c + 1).card() + "\n");

			boolean winner = correct(board.get(c), board.get(c + 1), high);

			if (!winner) {
				System.out.println("Noooooo...\nWrong! You lost...");
				kickedout = true;
				break;
			} else {
				System.out.print("Yes!");
				if (c < (board.size() - 2))
					System.out.println(" Next card!\n");
				else
					System.out.println("");
			}

		}

		if (!kickedout) {
			System.out.println("Well done! You beat the cards!");
			win = true;
		}
	}

	private int cardValue(PlayingCard c) {

		int score = 0;

		try {

			score = Integer.valueOf(c.getValue());
		} catch (NumberFormatException n) {
			switch (c.getValue()) {

			case "Ace":
				return 1;

			case "Jack":
				return 11;

			case "Queen":
				return 12;

			case "King":
				return 13;
			}
		}

		return score;
	}

	private boolean correct(PlayingCard o, PlayingCard n, boolean high) {

		if (high) {
			return (cardValue(n) >= cardValue(o));
		}

		return (cardValue(n) <= cardValue(o));
	}

	private boolean valid(String c) {
		if (c.length() < 3)
			return false;

		String compare = c.substring(0, 3);

		return ((compare.equalsIgnoreCase("low")) || (compare.equalsIgnoreCase("hig")));
	}

	public static void main(String[] args) {
		HigherOrLower hol = new HigherOrLower();
		hol.play();
	}
}