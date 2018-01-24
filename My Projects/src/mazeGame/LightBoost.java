package mazeGame;

import jb.Counter;

public class LightBoost extends MazeCounter {

	public LightBoost(int x, int y) {
		super(x, y, "Light");
	}

	public Counter copy() {
		return new LightBoost(xCoord, yCoord);
	}

	public boolean isMonster() {
		return false;
	}
}
