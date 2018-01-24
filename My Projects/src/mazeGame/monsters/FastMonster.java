package mazeGame.monsters;

import java.util.ArrayList;
import java.util.Collections;

import jb.*;
import mazeGame.Maze;
import mazeGame.Monster;

/*
 * Created: 16/1/2015
 * Last modified: 10/2/2015 (darkness)
 */
public class FastMonster extends Monster {

	private boolean dazed = false; // if it hits a wall, it freezes

	public FastMonster(int x, int y) {
		super(x, y, "Fast");
	}

	/**
	 * Moves 2 spaces at a time, but not very good at turning...
	 */
	public void move(Maze here) {

		Coordinates target = here.getPlayer().find(); // where the player is

		ArrayList<Coordinates> canMove = freeMoves(here); // the ways that aren't
													// blocked

		if (!canMove.isEmpty()) { // you can move

			Coordinates go = find(); // go is the new coordinates

			if (here.getMCell(target).dark()) {
				this.sleep = true;
				Collections.shuffle(canMove);
				go = charge(canMove.get(0), here);
			}

			// only one option
			else if (canMove.size() == 1)
				go = charge(canMove.get(0), here);

			else { // select the best

				// not too dissimilar to the search method in wargames
				Pair<Coordinates, Double> best = new Pair<>(target, Double.MAX_VALUE);

				for (Coordinates options : canMove) {
					Coordinates guess = go.add(options);

					double gDist = guess.dist(target);

					if (gDist < best.second()) {
						best.first(options);
						best.second(gDist);
					}
				}

				go = charge(best.first(), here);
			}
			move(go, here);
		}
	}

	private ArrayList<Coordinates> freeMoves(Maze maze) { // which directions aren't
													// blocked
		ArrayList<Coordinates> dirs = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates move = find().add(x, y);
					if (!maze.outOfRange(move)) {
						if (!maze.getMCell(move).wall()) {
							dirs.add(new Coordinates(x, y));
						}
					}
				}
			}
		}
		return dirs;
	}

	private Coordinates charge(Coordinates dir, Maze here) { // run!

		Coordinates nCoord = find().add(dir);
		if (!here.outOfRange(nCoord.add(dir))) {
			if (!here.getMCell(nCoord.add(dir)).wall()) {
				nCoord = nCoord.add(dir);
			}
			else {
				dazed = true;
			}
		}
		return nCoord;
	}

	public Counter copy() {
		return new FastMonster(this.xCoord, this.yCoord);
	}

	public boolean sleeping() {
		if (dazed) {
			dazed = false;
			return true;
		}
		return sleep;
	}
}
