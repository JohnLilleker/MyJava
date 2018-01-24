package mazeGame;

import jb.Counter;

public class Repulser extends MazeCounter {

	public Repulser(int x, int y) {
		super(x, y, "Blaster");
	}

	public Counter copy() {
		return new Repulser(xCoord, yCoord);
	}

	public boolean isMonster() {
		return false;
	}

}
