import jb.Counter;

public class SnakeCounter extends Counter {

	public SnakeCounter(int x, int y) {
		super(x, y, "Snake");
	}

	@Override
	public Counter copy() {
		return new SnakeCounter(xCoord, yCoord);
	}

}
