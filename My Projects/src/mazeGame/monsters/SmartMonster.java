package mazeGame.monsters;

import java.util.ArrayList;
import java.util.Collections;

import jb.Coordinates;
import jb.Counter;
import jb.Pair;
import mazeGame.Maze;
import mazeGame.Monster;

/*
 * Created: 16/1/2015
 * Last modified: 19/2/2015 (MathToolkit)
 */
public class SmartMonster extends Monster {

	public SmartMonster(int x, int y) {
		super(x, y, "Intel");
	}

	/**
	 * Moves one at a time, but sprints when close
	 */
	public void move(Maze here) {

		Coordinates nemesis = here.getPlayer().find();

		Coordinates st = this.find();
		ArrayList<Coordinates> possMoves = freeMoves(here, st);

		if (!possMoves.isEmpty()) {

			if (here.getMCell(nemesis).dark()) {
				this.sleep = true;
				Collections.shuffle(possMoves);
				Coordinates go = possMoves.get(0);
				move(st.add(go), here);

			} else if (possMoves.size() == 1) { // only one option
				Coordinates go = possMoves.get(0);
				move(st.add(go), here);
			} else { // choose wisely

				Pair<ArrayList<Coordinates>, Integer> best = new Pair<>(new ArrayList<Coordinates>(), Integer.MAX_VALUE);

				for (Coordinates go : possMoves) {
					Coordinates tryMove = st.add(go);
					ArrayList<Coordinates> path = new ArrayList<>();
					path.add(go);
					Pair<ArrayList<Coordinates>, Integer> attempt = moveCount(here, nemesis, tryMove, st, 1, path);

					if (attempt.second() < best.second()) {
						best = attempt;
					}
				}
				// now move
				Coordinates move = best.first().get(0);

				if (best.second() < 6) { // pounce 3 steps
					for (int run = 1; run < 3 && run < best.first().size(); run++) {
						Coordinates step = best.first().get(run);
						move = move.add(step);
					}
				}

				if (best.second() > 10) { // in general, crap so find the one
											// that takes you the closest
											// euclidean-ly
					Pair<Coordinates, Double> alright = new Pair<>(new Coordinates(0,0), Double.MAX_VALUE);
					for (Coordinates pm : possMoves) {
						Coordinates m = st.add(pm);
						double score = m.dist(nemesis);
						if (score < alright.second()) {
							alright.first(pm);
							alright.second(score);
						}
					}
					move = alright.first();
				}
				move(st.add(move), here);
			}
		}
	}

	private Pair<ArrayList<Coordinates>, Integer> moveCount(Maze here, Coordinates target, Coordinates now, Coordinates previous, int depth,
			ArrayList<Coordinates> path) {
		// ^ hero ^ current ^ before ^ path so far
		// thank you Haskell for your glorious recursion design

		if (now.same(target))
			return new Pair<ArrayList<Coordinates>, Integer>(path, depth); // found
																		// 'em
		if (depth > 10)
			return new Pair<ArrayList<Coordinates>, Integer>(path, depth); // too far

		// filter the step back
		ArrayList<Coordinates> init = freeMoves(here, now);
		ArrayList<Coordinates> pos = new ArrayList<>();
		for (Coordinates m : init) {
			Coordinates fut = now.add(m);
			if (!(fut.same(previous)))
				pos.add(m);
		}

		if (pos.isEmpty()) {
			return new Pair<ArrayList<Coordinates>, Integer>(path, 20); // no more
																	// moves
																	// from this
																	// position
		}

		ArrayList<Coordinates> old = new ArrayList<>();
		Pair<ArrayList<Coordinates>, Integer> best = new Pair<>(old, Integer.MAX_VALUE);

		for (Coordinates next : pos) {
			Coordinates future = now.add(next);
			ArrayList<Coordinates> newPath = new ArrayList<>();
			newPath.addAll(path);
			newPath.add(next);
			Pair<ArrayList<Coordinates>, Integer> recurse = moveCount(here, target, future, now, depth + 1, newPath); // recursion
																												// step

			if (recurse.second() < best.second()) {
				best = recurse;
			}
		}

		return best;
	}

	private ArrayList<Coordinates> freeMoves(Maze maze, Coordinates here) { // which
																// directions
																// aren't
																// blocked
		ArrayList<Coordinates> dirs = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates work = here.add(x, y);
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
		return new SmartMonster(this.xCoord, this.yCoord);
	}
}
