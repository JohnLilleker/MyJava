package games;

/* Created 18/5/2014
 * Last modified 21/5/2014
 */
public class RockPaperScissors extends MicroGame {

	private final String[] options = { "Rock", "Paper", "Scissors" };
	private boolean win;
	private String catagory = "Luck";
	private String name = "Rock, Paper, Scissors";

	public boolean getWin() {
		return win;
	}

	public String getType() {
		return catagory;
	}

	public String getName() {
		return name;
	}

	public void play() {

		int player = 0;
		int computer = 0;
		Reader typer = new Reader();
		win = false;

		System.out.println("Rock, Paper, Scissors!");
		System.out.print("Best of 5, ready? ");
		typer.read();

		while (player < 3 || computer < 3) {

			System.out.println("Player " + player + " : Computer " + computer);
			String human;
			String ai;

			do {
				human = typer.readName("Rock, Paper or Scissors? ");
				if (!validate(human))
					System.out.println("Seriously?");
			} while (!validate(human));

			ai = computer();

			try {
				Thread.sleep(1000);
				System.out.println("\nRock!");
				Thread.sleep(1000);
				System.out.println("Paper!");
				Thread.sleep(1000);
				System.out.println("Scissors!\n");
			} catch (InterruptedException e) {
				System.out.println("It crashed...");
			}

			System.out.println(human + " vs " + ai);
			char winner = compare(human, ai);

			if (winner == 'w') {
				player++;
				System.out.println("You won!");
				if (player == 3)
					break;
			} else if (winner == 'l') {
				computer++;
				System.out.println("You lost...");
				if (computer == 3)
					break;
			}
		}
		if (player == 3) {
			System.out.println("You won the game, yay!");
			win = true;
		} else
			System.out.println("You lost, better luck next time.");
	}

	private char compare(String p, String c) {

		if (p.equals(c))
			return 'd';

		switch (p) {

		case "Rock":
			if (c.equals("Paper"))
				return 'l';
			if (c.equals("Scissors"))
				return 'w';

		case "Paper":
			if (c.equals("Scissors"))
				return 'l';
			if (c.equals("Rock"))
				return 'w';

		case "Scissors":
			if (c.equals("Rock"))
				return 'l';
			if (c.equals("Paper"))
				return 'w';
		}
		return ' ';
	}

	private String computer() {

		int choice = random.nextInt(2);

		return options[choice];
	}

	private boolean validate(String p) {

		return p.equals(options[0]) || p.equals(options[1]) || p.equals(options[2]);
	}

	public static void main(String[] args) {
		RockPaperScissors rps = new RockPaperScissors();
		rps.play();
	}
}
