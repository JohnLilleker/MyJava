package mazeGame.monsters;

import java.util.ArrayList;
import java.util.Collections;

import jb.Coordinates;
import jb.Counter;
import mazeGame.Maze;
import mazeGame.Monster;

/*
 * Created: 2/2/2015
 * Last modified: 3/2/2015 (darkness)
 */
public class RandomMonster extends Monster {

	public RandomMonster(int x, int y) {
		super(x, y, "Random");
	}

	/**
	 * Highly unpredictable, moves 3 random positions but stops if it lands on
	 * you
	 */
	public void move(Maze here) {

		Coordinates hero = here.getPlayer().find();

		Coordinates moveOne = randomMove(find(), here);

		if (moveOne != null) { // this need only be checked once

			if (moveOne.same(hero)) {
				move(moveOne, here);
			} 
			else {
				Coordinates moveTwo = randomMove(moveOne, here);
				if (moveTwo.same(hero)) {
					move(moveTwo, here);
				} else {
					Coordinates finalMove = randomMove(moveTwo, here);
					move(finalMove, here);
				}

			}
		}
	}

	private Coordinates randomMove(Coordinates co, Maze here) {
		// selects a random move and gives you the new coordinates
		ArrayList<Coordinates> moves = freeMoves(here, co);

		if (moves.size() > 0) {
			Collections.shuffle(moves);
			Coordinates move = co.add(moves.get(0));
			return move;
		}
		return null;
	}

	private ArrayList<Coordinates> freeMoves(Maze maze, Coordinates coord) { // which
																			// directions
																			// aren't
																			// blocked
		ArrayList<Coordinates> dirs = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates work = coord.add(x, y);
					if (!maze.outOfRange(work)) {
						if (!maze.getMCell(work).wall()) {
							Coordinates move = new Coordinates(x,y);
							dirs.add(move);
						}
					}
				}
			}
		}

		return dirs;
	}

	public Counter copy() {
		return new RandomMonster(this.xCoord, this.yCoord);
	}

}
