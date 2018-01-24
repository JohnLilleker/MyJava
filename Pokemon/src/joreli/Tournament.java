package joreli;

import java.util.*;

/* Created: 22/4/2014
 * Last modified: 13/7/2017 (burn computer player construction)
 */
public class Tournament {

	// Names for the Computers
	private String[] names = { "Nathan", "Mike", "Rebecca", "Jim", "John", "Jack", "Jill", "Lauren", "Emily",
			"Jonathan", "Morgan", "Arthur", "Donna", "Natalie", "Rachel", "Bruce", "Alex", "Jade", "James", "Brittany",
			"Amber", "William", "Kate", "Jessica", "Emma", "Amy", "Chris", "Summer", "Molly", "Chloe", "Samuel",
			"Benjamin", "Liam", "Leah", "Olivia", "Oliver", "Ollie", "Aaran", "Eleanor", "Harry", "Ronald", "Mortimer",
			"Elizabeth", "Brian", "Guy", "Charlie", "Sarah", "Mimi", "Tyson", "Luke", "Peter", "Dale", "Ash", "Misty",
			"Brock", "Jacob", "Katie", "Clark" };

	// Witty headlines to inspire on each round
	private String[][] titles = { { "Qualifier, lets see who falls at the first Hurdle",
			"The first round, time to sort the wheat from the chaff", "The first round, there's too many of you!",
			"Round 1, lets get ready to rumble!!!", "Welcome to round 1, only half will make it to round 2..." },
			{ "Second round, only the strongest will make it...", "It's only going to be harder from here on in...",
					"I do beleive the champion will win in this round :D" },
			{ "Quarter-Finals, nearly there!", "It's the Quarter-finals, who can make it further?",
					"Halfway there, let's do this!" },
			{ "Semis, so close!", "Only four left... lets half it!", "Don't fall at the final hurdle, you can do it!",
					"Seeing as you made it this far, surely you deserve to win, right?" },
			{ "Its the Final! Who is the Champion...", "Its the battle you've been waiting for, the Final!",
					"The winner is the Champion... well get on with it then!" }

	};

	private Random rand = new Random();

	// TODO no player's teams are actually set yet due to refactoring elsewhere...
	// wait until a seeding system is in place, then fix this mess

	/**
	 * Creates a list of 32 Trainers, a mixture of Human players, random Computers,
	 * regional Computers and type-specific Trainers
	 * 
	 * @param p
	 *            the number of Human Players
	 * @return the List
	 */
	public ArrayList<Trainer> call(int p) {
		Scanner typer = new Scanner(System.in);
		ArrayList<Trainer> tributes = new ArrayList<>();

		for (int c = 0; c < p; c++) {

			String name = "";

			while (name.equals("")) {
				System.out.print("Player " + (c + 1) + ": Enter name- ");
				name = typer.nextLine();
			}
			Trainer human = new HumanTrainer(name);

			System.out.println(human.getName());
			tributes.add(human);
		}
		int coms = 32 - p;

		int rest = 0;
		while (rest < (coms)) {
			String name = null;
			int index = rand.nextInt(names.length);
			while ((name = names[index]) == null) {
				index++;
				if (index == names.length) {
					index = 0;
				}
			}

			tributes.add(new ComputerTrainer(name));
			rest++;
		}

		return tributes;
	}

	/**
	 * Allows a single Human to enter a Tournament, similar with call(1) but with
	 * the player already instantiated
	 * 
	 * @param p
	 *            the Single player
	 * @return the rest of the players
	 */
	public ArrayList<Trainer> join(HumanTrainer p) {

		ArrayList<Trainer> competitors = new ArrayList<>();
		competitors.add(p);

		for (int i = 0; i < 31; i++) {
			String name = null;
			int index = rand.nextInt(names.length);
			while ((name = names[index]) == null) {
				index++;
				if (index == names.length) {
					index = 0;
				}
			}

			competitors.add(new ComputerTrainer(name));
		}

		return competitors;
	}

	/**
	 * Tournament! 32 Trainers, only 1 can win TODO Needs heavy refactoring...
	 * 
	 * @param t
	 *            the list of competitors
	 */
	public Trainer runTournament(ArrayList<Trainer> t) {

		Battle royale = new Battle(new Location("The Tournament arena", Location.Terrain.BUILDING));
		Scanner typer = new Scanner(System.in);

		Collections.shuffle(t);
		// If you watch the first round carefully i.e who plays who, you can
		// work out which winner
		// battles which and how far each person is from each other. The order
		// is only mixed once

		// how to refactor...
		// we have a contenders, and a victors

		/*
		 * A round is
		 * 
		 * do this in a method? take ArrayList and round number return ArrayList in a
		 * loop (either on size or number of rounds), call method with fighters, return
		 * winners, set winners as fighters
		 * 
		 * print the headline print who fights who have an ArrayList for winners
		 * 
		 * for every two trainers battle what to do if no winner? player 1 wins by
		 * default? announce winner store in winner for next round
		 * 
		 * next round, winners become fighters
		 * 
		 * continue until only one remains
		 */

		try {
			ArrayList<Trainer> victors2 = new ArrayList<>();
			// Round 1
			System.out.println(titles[0][rand.nextInt(titles[0].length)]);
			pairings(t);

			System.out.println("Are you ready? ");
			typer.nextLine();

			for (int i = 0; i < t.size(); i += 2) {

				Trainer pl1 = t.get(i);
				Trainer pl2 = t.get(i + 1);
				System.out.println("\n" + pl1.getName() + " vs " + pl2.getName());
				if (pl1.isHuman() || pl2.isHuman()) {
					royale.setLogMethod(Battle.PRINT);
				} else {
					royale.setLogMethod(Battle.NONE);
				}
				Trainer victor = royale.battle(pl1, pl2);
				System.out.println("\n" + victor.getName() + " won!");
				victor.restore();
				victors2.add(victor);
				Thread.sleep(500);

			}

			ArrayList<Trainer> victors3 = new ArrayList<>();
			// Round 2
			System.out.println(titles[1][rand.nextInt(titles[1].length)]);
			pairings(victors2);

			System.out.print("Are you ready? ");
			typer.nextLine();

			for (int i = 0; i < victors2.size(); i += 2) {

				Trainer pl1 = victors2.get(i);
				Trainer pl2 = victors2.get(i + 1);
				System.out.println("\n" + pl1.getName() + " vs " + pl2.getName());
				if (pl1.isHuman() || pl2.isHuman()) {
					royale.setLogMethod(Battle.PRINT);
				} else {
					royale.setLogMethod(Battle.NONE);
				}
				Trainer victor = royale.battle(pl1, pl2);
				System.out.println("\n" + victor.getName() + " won!");
				victor.restore();
				victors3.add(victor);
				Thread.sleep(500);
			}

			ArrayList<Trainer> victors4 = new ArrayList<>();
			// Quarter-Final
			System.out.println(titles[2][rand.nextInt(titles[2].length)]);
			pairings(victors3);

			System.out.print("Are you ready? ");
			typer.nextLine();

			for (int i = 0; i < victors3.size(); i += 2) {

				Trainer pl1 = victors3.get(i);
				Trainer pl2 = victors3.get(i + 1);
				System.out.println("\n" + pl1.getName() + " vs " + pl2.getName());
				if (pl1.isHuman() || pl2.isHuman()) {
					royale.setLogMethod(Battle.PRINT);
				} else {
					royale.setLogMethod(Battle.NONE);
				}
				Trainer victor = royale.battle(pl1, pl2);
				System.out.println("\n" + victor.getName() + " won!");
				victor.restore();
				victors4.add(victor);
				Thread.sleep(500);
			}

			ArrayList<Trainer> victors5 = new ArrayList<>();
			// Semi-final
			System.out.println(titles[3][rand.nextInt(titles[3].length)]);
			pairings(victors4);

			System.out.print("Are you ready? ");
			typer.nextLine();

			for (int i = 0; i < victors4.size(); i += 2) {

				Trainer pl1 = victors4.get(i);
				Trainer pl2 = victors4.get(i + 1);
				System.out.println("\n" + pl1.getName() + " vs " + pl2.getName());
				if (pl1.isHuman() || pl2.isHuman()) {
					royale.setLogMethod(Battle.PRINT);
				} else {
					royale.setLogMethod(Battle.NONE);
				}
				Trainer victor = royale.battle(pl1, pl2);
				System.out.println("\n" + victor.getName() + " won!");
				victor.restore();
				victors5.add(victor);
				Thread.sleep(500);
			}

			// Final
			System.out.println(titles[4][rand.nextInt(titles[4].length)]);

			royale.setLogMethod(Battle.NONE);
			for (Trainer c : victors5) {
				c.revive(royale);
			}

			royale.setLogMethod(Battle.PRINT);
			Trainer pl1 = victors5.get(0);
			Trainer pl2 = victors5.get(1);

			System.out.println("\n" + pl1.getName() + " vs " + pl2.getName());
			System.out.print("Are you ready? ");
			typer.nextLine();
			Trainer champion = royale.battle(pl1, pl2);
			System.out.println("\n" + champion.getName() + " is the Champion!");
			System.out.println(champion);
			return champion;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void pairings(ArrayList<Trainer> competitors) {

		for (int i = 0; i < competitors.size(); i += 2) {
			System.out.println(competitors.get(i).getName() + " vs " + competitors.get(i + 1).getName());
		}
		System.out.println("\n");
	}

	public static void main(String[] args) {

		Tournament hungerGames = new Tournament();
		Scanner typer = new Scanner(System.in);
		int numplayers = 0;

		do {
			System.out.print("Enter number of Humans(1-32): ");
			numplayers = typer.nextInt();
		} while ((numplayers < 0) || (numplayers > 32));

		hungerGames.runTournament(hungerGames.call(numplayers));

	}
}
