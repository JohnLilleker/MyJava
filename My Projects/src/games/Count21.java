package games;

/* Created 19/5/2014
 * Last modified 29/7/2014 (Made the Computer smarter)
 */
public class Count21 extends MicroGame {

	private boolean win;
	private int pieces;
	private String how = "Strategy";
	private String name = "21";

	public boolean getWin() {
		return win;
	}

	public String getType() {
		return how;
	}

	public String getName() {
		return name;
	}

	public void play() {

		Reader typer = new Reader();
		win = false;
		pieces = 21;

		System.out.println("Count 21!");
		typer.read("Last peice loses, ready? ");
		int turn = 0;

		String first = typer.read("Do you want to go first or second (1 or 2)? ");
		if (first.equalsIgnoreCase("first") || first.equals("1"))
			turn = 1;

		while (pieces > 0) {

			if (turn == 1) {
				// Player

				boolean wrong = true;
				int get = 1;
				display();

				while (wrong) {

					try {

						String take = typer.read("Enter number of pieces, 1, 2 or 3 ");

						get = Integer.parseInt(take);
						if (validate(get))
							wrong = false;

						else
							System.out.println("I said 1, 2 or 3!");

					} catch (NumberFormatException e) {
						System.out.println("Not a number!");
					}
				}

				take(get);

				if (pieces < 1) {
					System.out.println("You lose...");
					break;
				}
				turn = 0;
			}

			else if (turn == 0) {
				// Ai

				display();

				int take = computer();
				System.out.println("Computer takes " + take);

				take(take);

				if (pieces < 1) {
					System.out.println("You Won! Well done");
					win = true;
					break;
				}
				turn = 1;
			}
		}
	}

	private void display() {

		String chain = "";
		for (int i = 0; i < pieces; i++) {
			chain += "+ ";
		}
		System.out.println(chain);
	}

	private int computer() {
		// Woooo! an AI I can program! F*** you COM1005! F*** you connect4!

		int i = 0;

		for (i = 1; i < 3; i++) {
			if ((((pieces - i) % 2) == 1) || (((pieces - i) % 5) == 0))
				break;
			if ((pieces - i) == 0)
				break;
		}
		return i;
	}

	private void take(int p) {
		pieces -= p;
	}

	private boolean validate(int t) {
		return ((t > 0) && (t < 4));
	}

	public static void main(String[] args) {

		Count21 c21 = new Count21();

		c21.play();

	}

}
