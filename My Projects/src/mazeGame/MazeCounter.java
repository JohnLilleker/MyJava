package mazeGame;

import jb.Coordinates;
import jb.Counter;

public abstract class MazeCounter extends Counter {

	public MazeCounter(int x, int y, String m) {
		super(x, y, m);
	}
	public MazeCounter(Coordinates co, String m) {
		super(co, m);
	}

	public abstract boolean isMonster();
	
}
