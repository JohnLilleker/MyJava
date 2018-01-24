import java.util.ArrayList;

import jb.Coordinates;

public class Snake {

	private ArrayList<SnakeCounter> body = new ArrayList<SnakeCounter>();
	private SnakeCounter head;
	private int dx;
	private int dy;
	
	public Snake(SnakeGame board) {
		head = new SnakeCounter(15, 15);
		for (int i = 0; i < 2; i++) {
			body.add(new SnakeCounter(0, 0));
		}
		dx = 0;
		dy = +1;
		
		board.getCell(15, 15).place(head);
		for (int i = 0; i < body.size(); i++) {
			board.getCell(15, 14-i).place(body.get(i));
		}
	}
	
	public void setMove(int dx, int dy) {
		if (this.dy == dy || this.dx == dx)
			return;
		this.dx = dx;
		this.dy = dy;
	}
	public Coordinates getD() {
		return new Coordinates(dx, dy);
	}
	public SnakeCounter getHead() {
		return head;
	}
	public void grow(SnakeGame board, int x, int y) {
		SnakeCounter c = new SnakeCounter(x, y);
		body.add(c);
		board.getCell(x, y).place(c);
	}
	public SnakeCounter getTail() {
		// get the one at the front
		// remove and add to the end
		// return it
		SnakeCounter tail = body.remove(0);
		body.add(tail);
		return tail;
	}

}
