package mazeGame.monsters;

import java.util.ArrayList;

import jb.*;
import mazeGame.Maze;
import mazeGame.Monster;

/*
 * Created: 9/2/2015
 * Last modified: 11/2/2015 (MathToolkit)
 */
public class GhostMonster extends Monster {

	private boolean stuck = false;

	public GhostMonster(int x, int y) {
		super(x, y, "Ghost");
	}

	/**
	 * Can move through walls, but takes a turn to get through
	 */
	public void move(Maze maze) {

		Coordinates nemesis = maze.getPlayer().find();

		Pair<Coordinates, Double> close = new Pair<>(new Coordinates(0,0), Double.MAX_VALUE);

		for (Coordinates co : moves(maze)) {
			Coordinates next = find().add(co);

			double score = nemesis.dist(next);

			if (score < close.second()) {
				close.first(next);
				close.second(score);
			}
		}

		Coordinates best = close.first();

		if (maze.getMCell(best).wall()) {
			stuck = true;
		}

		move(best, maze);
	}

	public ArrayList<Coordinates> moves(Maze m) {
		ArrayList<Coordinates> poss = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates c = new Coordinates(x, y);
					if (!m.outOfRange(xCoord + x, yCoord + y)) {
						poss.add(c);
					}
				}
			}
		}
		return poss;
	}

	public boolean sleeping() {
		if (stuck) {
			stuck = false;
			return true;
		}
		return sleep;
	}

	public Counter copy() {
		return new GhostMonster(this.xCoord, this.yCoord);
	}

}
