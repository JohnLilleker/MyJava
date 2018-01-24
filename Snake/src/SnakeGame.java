import java.awt.Color;

import jb.Board;
import jb.Cell;
import jb.Coordinates;
import jb.SimpleImage;

public class SnakeGame extends Board {
	
	public enum Move{UP, DOWN, LEFT, RIGHT}
	
	private int score = 0;
	private boolean dead = false;
	private Snake snake;
	
	public SnakeGame() {
		super(30, 30);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board[y][x] = new SnakeCell(x, y);
			}
		}
		placeFood();
		snake = new Snake(this);
	}
	private SnakeGame(Cell[][] cells) {
		super(cells);
	}
	
//	public void restart() {
//		score = 0;
//		dead = false;
//		clear();
//		snake = new Snake(this);
//		placeFood();
//	}
	
	public void update() {
		Coordinates d = snake.getD();
		SnakeCounter head = snake.getHead();
		Coordinates pos = head.find();
		
		Coordinates n = pos.add(d);
		
		if (this.outOfRange(n) || getCell(n.getX(), n.getY()).contains("Snake")) {
			dead = true;
			return;
		}
		head.move(n.getX(), n.getY(), this);
		if (getCell(n).contains("Food")) {
			getCell(n).remove("Food");
			score++;
			snake.grow(this, pos.getX(), pos.getY());
			placeFood();
			return;
		}
		
		SnakeCounter tail = snake.getTail();
		tail.move(pos.getX(), pos.getY(), this);
	}
	public SimpleImage view() {
		SimpleImage pic = new SimpleImage(width, height, 20);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Cell sc = getCell(x, y);
				
				if (sc.contains("Food")) {
					
					SnakeFood berry = (SnakeFood) sc.getCounter("Food"); 
					Color c = berry.getColour();
					if (dead) 
						pic.colourIn(x, y, c.getRed()/2, c.getGreen()/2, c.getBlue()/2);
					else
						pic.colourIn(x, y, c);
				}

				else if (sc.contains("Snake")) {
					if (dead)
						pic.colourIn(x, y, 175, 0, 0);
					else
						pic.colourIn(x, y, 144, 8, 238);
				}
				else {
					if (dead)
						pic.colourIn(x, y, 175, 175, 175);
					else
						pic.colourIn(x, y, 255, 255, 255);
				}
			}
		}
		
		return pic;
	}
	public boolean gameOver() {
		return dead;
	}
	public int points() {
		return score;
	}
	public void control(Move move) {
		switch(move) {
		case DOWN:
			snake.setMove(0, -1);
			break;
		case LEFT:
			snake.setMove(-1, 0);
			break;
		case RIGHT:
			snake.setMove(+1, 0);
			break;
		case UP:
			snake.setMove(0, +1);
			break;
		
		}
	}
	private void placeFood() {
		int x = (int) Math.round(Math.random() * (width-1));
		int y = (int) Math.round(Math.random() * (height-1));
		getCell(x, y).place(new SnakeFood(x, y));
	}
	
	
	public Board copy() {
		Cell[][] m = new Cell[this.height][this.width];
		for( int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				m[y][x] = this.board[y][x].copy();
			}
		}
		return new SnakeGame(m);
	}

}
