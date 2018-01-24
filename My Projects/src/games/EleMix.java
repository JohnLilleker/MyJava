package games;

import java.util.ArrayList;

/* Created 18/5/2014
 * Last modified 21/6/2014 (More elements, deal 3 to deal 5)
 */
public class EleMix extends MicroGame {

	private boolean win;
	private final String[] elements = { "Fire", "Water", "Earth", "Air" };
	private String thing = "Luck";
	private String name = "Elemix";

	public boolean getWin() {
		return win;
	}

	public String getType() {
		return thing;
	}

	public String getName() {
		return name;
	}

	public void play() {

		System.out.println("EleMix!");
		System.out.println("You are given five elemental forces, match them to your opponent and emerge the victor!");
		System.out.println("Key: Air > Water > Fire > Earth > Air (> means beats)");
		System.out.println("3 points for every victory, 1 for every stalemate");
		System.out.println("Play until you win or lose, no loose ends");

		gameLoop();
	}

	private void gameLoop() {

		ArrayList<String> player = deal();
		ArrayList<String> enemy = deal();
		Reader typer = new Reader();
		int playerPoints = 0;
		int compPoints = 0;
		win = false;

		int rounds = 0;
		while (rounds < 5) {

			String ele = player.get(0);
			for (int i = 1; i < player.size(); i++) {
				ele += ", " + player.get(i);
			}
			boolean valid = false;
			String choice = "";

			while (!valid) {
				choice = typer.readName("\nPlayer, choose your attack: You have " + ele + ": ");
				if (player.contains(choice))
					valid = true;
				else
					System.out.println("You don't have that element...");
			}

			int random = this.random.nextInt((enemy.size() - 1));
			String opponent = enemy.get(random);

			System.out.println(choice + " vs " + opponent);
			char result = rules(choice, opponent);

			if (result == 'w') {
				System.out.println("You won!");
				playerPoints += 3;
			}

			if (result == 'l') {
				System.out.println("You were defeated");
				compPoints += 3;
			}

			if (result == 'd') {
				System.out.println("It's a draw!");
				playerPoints++;
				compPoints++;
			}
			rounds++;
			player.remove(choice);
			enemy.remove(opponent);
		}

		if (playerPoints > compPoints) {
			System.out.println("You were Victorious!!!");
			win = true;
		} else if (playerPoints == compPoints) {
			System.out.println("It's a draw... Rematch!");
			gameLoop();
		}

		else
			System.out.println("You have been defeated...");
	}

	private char rules(String e1, String e2) {

		switch (e1) {

		case "Fire":
			if (e2.equals("Water"))
				return 'l';
			if (e2.equals("Earth"))
				return 'w';
			return 'd';

		case "Water":
			if (e2.equals("Air"))
				return 'l';
			if (e2.equals("Fire"))
				return 'w';
			return 'd';

		case "Earth":
			if (e2.equals("Fire"))
				return 'l';
			if (e2.equals("Air"))
				return 'w';
			return 'd';

		case "Air":
			if (e2.equals("Earth"))
				return 'l';
			if (e2.equals("Water"))
				return 'w';
			return 'd';
		}

		return ' ';

	}

	private ArrayList<String> deal() {

		ArrayList<String> ele = new ArrayList<>();

		for (int i = 0; i < 5; i++) {

			int index = (int) Math.round(Math.random() * (elements.length - 1));
			ele.add(elements[index]);
		}
		return ele;
	}

	public static void main(String[] args) {
		EleMix em = new EleMix();

		em.play();
	}
}
