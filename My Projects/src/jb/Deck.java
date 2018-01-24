package jb;

import java.util.*;

/* Created 13/6/2014
 * Last modified 21/6/2014 (Added to jb, converted to abstract and changed a couple of methods)
 */
/**
 * A Deck of Cards, can be extended to hold any number or type of cards
 * 
 * @author JB227
 *
 */
public abstract class Deck {

	private ArrayList<Card> deck = new ArrayList<>();
	private int full;

	public Deck(int f) {
		this.full = f;
	}

	/**
	 * Exactly what it looks like
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}

	/**
	 * Removes a number of Cards from the Deck
	 * 
	 * @param h
	 *            how many Cards are required
	 * @return An ArrayList of Cards from the Deck
	 */
	public ArrayList<Card> deal(int h) {

		if (!(deck.size() - h < 1)) {
			ArrayList<Card> hand = new ArrayList<>();

			for (int i = 0; i < h; i++) {
				Card d = deck.get(0);
				Card c = d.copy();
				hand.add(c);
				deck.remove(0);
			}

			return hand;
		}

		System.out.println("Not enough cards!");
		return null;
	}

	/**
	 * Puts the Cards back in the Deck
	 * 
	 * @param hand
	 *            an ArrayList of Cards to be placed back in the Deck
	 */
	public void replace(ArrayList<Card> hand) {

		for (Card c : hand) {
			deck.add(c);
		}
	}

	/**
	 * The deck is at it's capacity
	 * 
	 * @return
	 */
	public boolean isFull() {
		return (deck.size() == full);
	}

	/**
	 * 
	 * @return true if the Deck is empty, no more cards
	 */
	public boolean noCards() {
		return (deck.size() == 0);
	}
}
