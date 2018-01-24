package games;

import java.util.*;

/* Created 13/6/2014
 * Last modified 13/6/2014
 */
public class Deck {

	private ArrayList<PlayingCard> deck = new ArrayList<>();

	public Deck() {

		String[] num = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
		String[] suit = { "spades", "hearts", "diamonds", "clubs" };

		for (int s = 0; s < suit.length; s++) {
			for (int n = 0; n < num.length; n++) {
				deck.add(new PlayingCard(num[n], suit[s]));
			}
		}
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}

	public ArrayList<PlayingCard> deal(int h) {

		if (!(deck.size() - h < 1)) {
			ArrayList<PlayingCard> hand = new ArrayList<>();

			for (int i = 0; i < h; i++) {
				PlayingCard d = deck.get(0);
				PlayingCard c = new PlayingCard(d.getValue(), d.getSuit());
				hand.add(c);
				deck.remove(0);
			}

			return hand;
		}

		System.out.println("Not enough cards!");
		return null;
	}

	public void replace(ArrayList<PlayingCard> hand) {

		for (PlayingCard c : hand) {
			if (!deck.contains(c)) {
				deck.add(c);
			}
		}
	}

	public boolean isFull() {
		return (deck.size() == 52);
	}

	public boolean noCards() {
		return (deck.size() == 0);
	}

	public static void main(String[] args) {

		Deck dealer = new Deck();

		dealer.shuffle();

		ArrayList<PlayingCard> hand = dealer.deal(5);

		for (PlayingCard c : hand) {
			System.out.println(c.card() + " " + c);
		}

		dealer.replace(hand);

		if (dealer.isFull())
			System.out.println("Full deck!");
	}

}
