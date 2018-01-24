package mazeGame.monsters;

import java.util.ArrayList;
import java.util.Collections;

import jb.Board;
import jb.Coordinates;
import jb.Counter;
import mazeGame.Maze;
import mazeGame.MazeCell;
import mazeGame.Monster;

/**
 * Wanders the maze, looking for prey 
 * @author JB227
 *
 */
public class GuardianMonster extends Monster {

	private Coordinates lastMove;
	
	private boolean climbing = false;
	private Coordinates wall;
	
	public GuardianMonster(int x, int y) {
		super(x, y, "Guardian");
		lastMove = new Coordinates(x, y);
	}

	public Counter copy() {
		return new GuardianMonster(this.xCoord, this.yCoord);
	}

	public void move(Maze here) {
		
		if (!sleep) {
			
			if (climbing) {
				move(wall, here);
				lastMove = find();
				return;
			}
			
			ArrayList<Coordinates> moves = moves(here, true);
			// towards player
			Coordinates player = here.getPlayer().find();
			
			Coordinates best = find();
			double close = Double.MAX_VALUE;
			
			for (Coordinates move : moves) {
				Coordinates next = find().add(move);
				double dist = next.dist(player);
				if (dist < close) {
					close = dist;
					best = next;
				}
			}
			
			MazeCell now = here.getMCell(xCoord, yCoord);
			MazeCell next = here.getMCell(best);
			
			if (!now.wall() && next.wall()) {
				climbing = true;
				wall = best;
				return;
			}

			lastMove = find();
			move(best, here);
			return;
		}
		
		ArrayList<Coordinates> moves = moves(here, false);
		if (moves.size() == 1) {
			Coordinates move = moves.get(0);
			lastMove = find();
			this.move(move.add(find()), here);
			return;
		}
		// no idea where player is
		climbing = false;
		// random, but not back on self
		Collections.shuffle(moves);
		Coordinates rand = lastMove;
		for (Coordinates move : moves) {
			if (!find().add(move).same(lastMove)) {
				rand = move;
				break;
			}
		}
		lastMove = find();
		move(rand.add(find()), here);
	}

	public boolean sleeping() {
		return false;
	}
	
	public void sleep() {
		super.sleep();
		climbing = false;
	}
	
	public void move(int x, int y, Board board) {
		super.move(x, y, board);
		climbing = false;
	}
	
	public ArrayList<Coordinates> moves(Maze m, boolean chasing) {
		ArrayList<Coordinates> poss = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates c = new Coordinates(x, y);
					if (!m.outOfRange(find().add(c))) {
						if (!m.getMCell(find().add(c)).wall() || chasing) {
							poss.add(c);
						}
					}
				}
			}
		}
		return poss;
	}
	
}
