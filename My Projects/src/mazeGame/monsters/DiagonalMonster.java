package mazeGame.monsters;

import jb.Coordinates;
import jb.Counter;
import jb.Pair;
import mazeGame.Maze;
import mazeGame.Monster;

import java.util.ArrayList;

/*
 * Created: 2/3/2015
 * Last modified: 2/3/2015
 */
public class DiagonalMonster extends Monster {

	private boolean stuck = false;

	public DiagonalMonster(int x, int y) {
		super(x, y, "Bishop");
	}

	/**
	 * Diagonal movement through walls
	 */
	public void move(Maze here) {

		Coordinates nemesis = here.getPlayer().find();

		Pair<Coordinates, Double> close = new Pair<>(new Coordinates(0,0), Double.MAX_VALUE);

		for (Coordinates co : possMoves(here)) {
			Coordinates move = co.add(find());
			
			double score = move.dist(nemesis);

			if (score < close.second()) {
				close.first(move);
				close.second(score);
			}
		}

		Coordinates best = close.first();

		if (here.getMCell(best).wall())
			stuck = true;

		move(best, here);
	}

	private ArrayList<Coordinates> possMoves(Maze m) {
		ArrayList<Coordinates> p = new ArrayList<>();

		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if ((x != 0 && y != 0) && !m.outOfRange(xCoord + x, yCoord + y)) {
					Coordinates e = new Coordinates(x,y);
					p.add(e);
				}
			}
		}
		return p;
	}

	@Override
	public boolean sleeping() {

		if (stuck) {
			stuck = false;
			return true;
		}

		return sleep;
	}

	@Override
	public Counter copy() {
		return new DiagonalMonster(this.xCoord, this.yCoord);
	}

}
