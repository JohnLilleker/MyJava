package mazeGame.monsters;

import jb.Coordinates;
import jb.Counter;
import mazeGame.Maze;
import mazeGame.Monster;

/*
 * Created: 2/3/2015
 * Last modified: 2/9/2017 (Code made better)
 */
public class WatcherMonster extends Monster {

	// it looks up, right, down, left, up... until it sees you
	private Coordinates[] direction = { new Coordinates(-1, 0), new Coordinates(0, 1), new Coordinates(1, 0), new Coordinates(0, -1)};
	private int now = 0; // toggles look index
	// the last known location of the player
	private Coordinates target = new Coordinates(xCoord, yCoord);
	// how far it can 'see', half this for quick view
	private final int LIMIT = 10;
	
	// should it return to it's post after so long without a spot?
	
	public WatcherMonster(int x, int y) {
		super(x, y, "Watch");
	}

	/**
	 * It doesn't sleep, but looks for you. It spins on the spot, oblivious to
	 * light and dark searching for you... It has sharper eyes than you
	 */
	public void move(Maze maze) {
		
		// immediate spot
		if (look(maze)) {
			moveToTarget(maze);
			return;
		}
		
		// you have seen the player before and know where they were, so move towards it
		if (!this.find().same(target)) {
			moveToTarget(maze);
			look(maze);
			return;
		}
		
		// absolutely no idea, so see as far as possible in a direction
		if (watch(maze)) {
			moveToTarget(maze);
			return;
		}
	}

	// immediate area 360* search, limit 5
	// returns true if player found 
	private boolean look(Maze m) {

		for (int l = 0; l < 4; l++) { // loops over all directions
			Coordinates d = direction[l];
			for (int t = 0; t < LIMIT/2; t++) {
				int x = xCoord + ((t+1) * d.getX());
				int y = yCoord + ((t+1) * d.getY());
				if (m.outOfRange(x, y))
					break;
				if (m.getMCell(x, y).wall())
					break;
				if (m.getMCell(x, y).contains("Hero")) {
					this.target = new Coordinates(x, y);
					return true;
				}
			}
		}

		return false;
	}

	// the stationary spinning motion, limit 10
	// returns true if player found
	private boolean watch(Maze m) {

		Coordinates d = direction[now];
		Coordinates here = find();
		
		// stop monster from looking straight at a wall...
		int attempt = 0;
		while((m.outOfRange(here.add(d)) || m.getMCell(here.add(d)).wall()) && attempt < 4 ) {
			attempt++;
			now = (now + 1) % direction.length;
			d = direction[now];
		}

		boolean spot = false;
		for (int loop = 0; loop < LIMIT; loop ++) {
			int focusX = this.xCoord + (d.getX() * (loop+1)), focusY = this.yCoord + (d.getY() * (loop+1));
			
			if (m.outOfRange(focusX, focusY))
				break;
			
			if (m.getMCell(focusX, focusY).wall())
				break;
			
			if (m.getMCell(focusX, focusY).contains("Hero")) {
				this.target = new Coordinates(focusX, focusY);
				spot = true;
				break;
			}
		}
		
		now = (now + 1) % direction.length;

		return spot;
	}
	
	private void moveToTarget(Maze maze) {
		// do movements towards the target location
		
		// direction towards the target
		Coordinates dirs = target.sub(find());
		

		// one of the above should be 0
		int d = (dirs.getX() == 0) ? 1 : 0;
		
		if (d == 1) {
			dirs.setY(dirs.getY() / Math.abs(dirs.getY()));
		}
		else {
			dirs.setX(dirs.getX() / Math.abs(dirs.getX()));
		}
		

		move(find().add(dirs), maze);
	}

	public void wake() {
		// doesn't sleep, so no need to wake
	}
	
	public void sleep() {
		// can't be put to sleep
	}
	
	public boolean sleeping() {
		return false;
	}

	public Counter copy() {
		return new WatcherMonster(this.xCoord, this.yCoord);
	}

}
