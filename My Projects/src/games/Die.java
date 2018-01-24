package games;

/* Created 21/6/2014
 * Last modified 21/6/2014
 */
public class Die {

	private int value = 0;

	public void roll() {

		do {
			value = (int) ((Math.random() * 6) + 1);
		} while (value == 7);

	}

	public int show() {
		return value;
	}

	public String toString() {

		String die = "|       |\n" + "|       |\n" + "|       | Not rolled";

		switch (value) {

		case 1:
			die = "|       |\n" + "|   o   |\n" + "|       |";
			break;

		case 2:
			die = "| o     |\n" + "|       |\n" + "|     o |";
			break;

		case 3:
			die = "| o     |\n" + "|   o   |\n" + "|     o |";
			break;

		case 4:
			die = "| o   o |\n" + "|       |\n" + "| o   o |";
			break;

		case 5:
			die = "| o   o |\n" + "|   o   |\n" + "| o   o |";
			break;

		case 6:
			die = "| o   o |\n" + "| o   o |\n" + "| o   o |";
			break;
		}

		return die;
	}

	public static void main(String[] args) {

		Die dice = new Die();
		Die dice2 = new Die();

		dice.roll();
		dice2.roll();
		System.out.println(dice + "\n\n" + dice2);
		System.out.println("Total: " + (dice.show() + dice2.show()));

	}
}
