import jb.Cell;
import jb.Counter;

public class SnakeCell extends Cell {

	public SnakeCell(int x, int y) {
		super(x, y);
	}
	
	public Cell copy() {
		Cell c = new SnakeCell(xCoord, yCoord);
		for (Counter o : room) {
			c.place(o.copy());
		}
		return c;
	}
}
