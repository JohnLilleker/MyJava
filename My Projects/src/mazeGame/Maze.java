package mazeGame;

import java.util.ArrayList;

import jb.Board;
import jb.Cell;
import jb.Coordinates;

/*
 * Created: 16/1/2015
 * Last modified: 12/2/2015 (new constructor)
 */
public class Maze extends Board {

	private String name;

	public Maze(int h, int w, String n) {
		super(h, w);

		this.name = n;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Coordinates co = new Coordinates(x, y);
				this.board[y][x] = new MazeCell(co, ' ');
			}
		}

		placePlayer();
		placeExit();
	}

	public Maze(String[] data, String n) {

		super(data.length, data[0].length());

		this.name = n;

		for (int y = 0; y < data.length; y++) {

			for (int x = 0; x < data[y].length(); x++) {
				Coordinates co = new Coordinates(x, y);
				this.board[y][x] = new MazeCell(co, data[y].charAt(x));
			}
		}

		// check for player and exit, all others not as important
		if (!right(true)) {
			if (!placePlayer()) {
				System.out.println("Invalid Maze\n" + this);
			}
		}
		if (!right(false)) {
			if (!placeExit()) {
				System.out.println("Invalid Maze\n" + this);
			}
		}
	}

	private Maze(Cell[][] b, String n) {
		super(b);

		this.name = n;
		if (!right(true)) {
			placePlayer();
		}
		if (!right(false)) {
			placeExit();
		}
	}

	public String getName() {
		return name;
	}

	/**
	 * Casts getCell from Board
	 * 
	 * @param x
	 * @param y
	 *            coordinates
	 * @return the MazeCell
	 */
	public MazeCell getMCell(int x, int y) {
		return (MazeCell) this.getCell(x, y);
	}
	
	public MazeCell getMCell(Coordinates co) {
		return (MazeCell) this.getCell(co);
	}

	/**
	 * Finds the Player in the maze
	 * 
	 * @return the Player/hero
	 */
	public Player getPlayer() {
		for (Cell[] row : board) {
			for (Cell here : row) {
				if (here.contains("Hero"))
					return (Player) here.getCounter("Hero");
			}
		}
		return null;
	}

	/**
	 * Locates the exit
	 * 
	 * @return {x,y} coordinates
	 */
	public Coordinates goal() {
		Coordinates ex = null;

		for (Cell[] row : board) {
			for (Cell here : row) {
				if (((MazeCell) here).exit()) {
					ex = ((MazeCell) here).loc();
					break;
				}
			}
		}
		return ex;

	}

	/**
	 * Finds the Pack of monsters within the maze
	 * 
	 * @return an arrayList of all monsters within the maze
	 */
	public ArrayList<Monster> getMonsters() {

		ArrayList<Monster> pack = new ArrayList<>();
		for (Cell[] row : board) {
			for (Cell here : row) {
				if (((MazeCell) here).monster()) {
					pack.addAll(((MazeCell) here).getMonsters());
				}
			}
		}
		return pack;
	}

	/**
	 * {@inheritDoc}
	 */
	public Board copy() {
		Cell[][] copy = new Cell[board.length][board[0].length];
		int[] size = this.getSize();

		for (int y = 0; y < size[0]; y++) {
			for (int x = 0; x < size[1]; x++) {
				copy[y][x] = this.getMCell(x, y).copy();
			}
		}
		return new Maze(copy, this.name);
	}

	// fixer functions, if you forget to put a player or an exit in the maze
	private boolean right(boolean player) { // true, player? false, exit?

		if (player) {
			if (this.getPlayer() != null)
				return true;
		} else {
			if (this.goal() != null)
				return true;
		}

		return false;
	}

	private boolean placePlayer() {
		// any empty, non special space

		int attempt = 0; // 50 tries, then gives up

		while (attempt < 50) {
			Coordinates a = randCoords();

			MazeCell m = this.getMCell(a);

			if (m.plain()) {
				board[a.getY()][a.getX()] = new MazeCell(a, 'p');
				return true;
			}
			attempt++;
		}

		return false;
	}

	private boolean placeExit() {
		// any accessable(empty on at least 1 side) non-player space

		int attempt = 0; // 50 tries, then gives up

		while (attempt < 50) {
			Coordinates a = randCoords();

			MazeCell m = this.getMCell(a);

			Coordinates h = this.getPlayer().find();

			if (a.dist(h) > 3) {
				if (m.plain()) {

					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							if (x == 0 ^ y == 0 && !outOfRange(a.getX() + x, a.getY() + y)) {
								if (!this.getMCell(a.getX() + x, a.getY() + y).wall()) {
									board[a.getY()][a.getX()] = new MazeCell(a, 'e');
									return true;
								}
							}
						}
					}
				}
			}

			attempt++;
		}

		return false;
	}

	private Coordinates randCoords() { // random coordinates generator

		int[] d = this.getSize();

		int x = -1;
		int y = -1;

		while (outOfRange(x, y)) {

			x = (int) ((Math.random() * d[1]));
			y = (int) ((Math.random() * d[0]));

		}

		Coordinates c = new Coordinates(x, y);

		return c;
	}
}
