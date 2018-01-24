package games;

/* Created 21/5/2014
 * Last Modified 21/5/2014
 */
public class SayWhat extends MicroGame {

	private boolean win;
	private String doodah = "Strategy";
	private String name = "Say What";
	private char answer;

	public boolean getWin() {
		return win;
	}

	public String getType() {
		return doodah;
	}

	public String getName() {
		return name;
	}

	public void play() {

		Reader typer = new Reader();
		int lives = 5;
		int attempts = 20;
		win = false;

		String alpha = "abcdefghijklmnopqrstuvwxyz";
		int in = random.nextInt(25);

		answer = alpha.charAt(in);

		System.out.println("Say What?");
		System.out.println("I'm bored, and I have forgotten a certain letter...");
		System.out.println("For a laugh, can you tell me what it was?");
		System.out.println("Lets make it harder, you have 5 guesses and 20 letters. Good luck!");

		while (lives > 0) {

			// prompt "guess or try?"
			String go = typer
					.read("Guess(try a character, final answer) or\nTry(Use some Strings to find discrepancies)? ");

			if (go.equalsIgnoreCase("try")) {
				if (attempts > 0) {
					String query;
					do {
						query = typer.read("Enter attempt (5 letters max " + attempts + " letters left): ");
					} while (query.length() > 5);

					String reply = query.replace(answer, '?');

					attempts -= query.length();

					System.out.println(reply);
				} else
					System.out.println("No, you used all your available letters. Ha ");
			}

			if (go.equalsIgnoreCase("guess")) {
				char idea = typer.readChar("Enter your answer " + lives + " guesses left: ");

				if (idea == answer) {
					System.out.println("You did it!");
					win = true;
					break;
				} else {
					System.out.println("You got it wrong...");
					lives--;
				}
			}
		}
		if (lives < 1)
			System.out.println("You ran out of lives...\nI remember now! It was " + answer + "!");
	}

	public static void main(String[] args) {

		SayWhat sw = new SayWhat();
		sw.play();
	}
}
