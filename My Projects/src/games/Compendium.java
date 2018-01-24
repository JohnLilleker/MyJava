package games;

import java.util.*;

/* Created 21/5/2014
 * Last Modified 13/6/2014 (Higher or Lower)
 */
public class Compendium {

	private ArrayList<MicroGame> games = new ArrayList<>();

	public Compendium() {

		games.add(new EleMix());
		games.add(new Count21());
		games.add(new NumberGuess());
		games.add(new RockPaperScissors());
		games.add(new SayWhat());
		games.add(new HigherOrLower());
		// More to come...
	}

	public ArrayList<MicroGame> selectAll() {
		return games;
	}

	public int gamePopulation() {
		return games.size();
	}

	public MicroGame randomGame() {

		int index;
		do {
			index = (int) (Math.random() * games.size());
		} while (index == games.size());

		return games.get(index);
	}

	public MicroGame choose(String t) {

		Collections.shuffle(games);

		for (MicroGame g : games) {
			if (t.equalsIgnoreCase(g.getType()))
				return g;
		}

		System.out.println("What are you after?\nHave this instead");
		return randomGame();
	}

	public int playAll() {

		int points = 0;
		Collections.shuffle(games);
		for (MicroGame m : games) {
			m.play();
			System.out.println("\n");
			if (m.getWin())
				points++;
		}
		return points;
	}

	public MicroGame search(String n) {

		for (MicroGame m : games) {
			if (n.equalsIgnoreCase(m.getName()))
				return m;
		}
		System.out.println("Not found, maybe not implemented yet ;-)");
		return null;
	}

	public static void main(String[] args) {

		Compendium c = new Compendium();
		int wins = c.playAll();
		System.out.println("You won " + wins + " out of " + c.gamePopulation());
	}
}