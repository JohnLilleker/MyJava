package games;

/* Created 18/5/2014
 * Last modified 21/5/2014
 */
public class NumberGuess extends MicroGame {

	private boolean winner;
	private String type = "Strategy";
	private String name = "Number Guess";

	public boolean getWin() {
		return winner;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void play() {

		Reader typer = new Reader();
		int answer = random.nextInt(25) + 1;
		int lives = 10;
		winner = false;

		System.out.println("I'm thinking of a number between 1 and 25...");

		while (lives > 0) {
			int attempt = 0;

			boolean wrong = true;
			while (wrong) {

				try {
					String guess = typer.read("What is it? (You have " + lives + " guesses left) ");
					attempt = Integer.parseInt(guess);
					if (validate(attempt))
						wrong = false;
					else
						System.out.println("I'll give you a clue, it's between 1 and 25!");
				} catch (NumberFormatException e) {
					System.out.println("Not a number!");
				}
			}

			if (attempt == answer) {
				System.out.println("You got it, you won with " + lives + " lives remaining!");
				winner = true;
				break;
			}

			if (attempt > answer) {
				System.out.println("Lower!");
				lives--;
			}

			if (attempt < answer) {
				System.out.println("Higher");
				lives--;
			}
		}

		if (!winner)
			System.out.println("You ran out of lives, the anwer was " + answer);
	}

	private boolean validate(int g) {
		return ((g > 0) && (g < 26));
	}

	public static void main(String[] args) {
		NumberGuess ng = new NumberGuess();
		ng.play();
	}
}
