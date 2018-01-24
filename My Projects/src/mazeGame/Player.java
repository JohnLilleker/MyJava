package mazeGame;

import jb.Coordinates;
import jb.Counter;

/*
 * Created: 16/1/2015
 * Last modified: 29/1/2015(move)
 */
public class Player extends MazeCounter {

	private int view = 2;
	private int repulser = 1;
	
	public Player(int x, int y) {
		super(x, y, "Hero");
	}

	public Player(Coordinates coord) {
		super(coord, "Hero");
	}

	public void move(int direction, Maze here) {
		
		int newX = this.xCoord;
		int newY = this.yCoord;

		switch (direction) {
		case 0: // up
			newY++;
			break;
		case 1: // left
			newX--;
			break;
		case 2: // down
			newY--;
			break;
		case 3: // right
			newX++;
			break;
		}

		if (!here.outOfRange(newX, newY)) { // you are trying to leave the maze,
											// nowt happens
			if (!here.getMCell(newX, newY).wall()) { // you trying to walk
														// through a wall
				move(newX, newY, here); // the actual move, inherited from
										// Counter
			}
		}

	}
	
	public boolean isMonster() {
		return false;
	}
	
	public boolean dropBooster(Maze maze) {
		if (view > 2) {
			maze.getCell(xCoord, yCoord).place(new LightBoost(xCoord, yCoord));
			view --;
			return true;
		}
		return false;
	}
	public boolean hit() {
		if (repulser > 0) {
			repulser--;
			return true;
		}
		return false;
	}
	public boolean pickupRepulser() {
		if (repulser < 3) {
			repulser++;
			return true;
		}
		return false;
	}
	public boolean pickupBooster(Counter counter) {
		if (view < 5) {
			view++;
			return true;
		}
		return false;
	}
	public int getLight() {
		return view;
	}
	public int getBursts() {
		return repulser;
	}

	public Counter copy() {
		return new Player(xCoord, yCoord);
	}
}
