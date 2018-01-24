package mazeGame;

import java.awt.Color;
import java.util.ArrayList;

import jb.Cell;
import jb.Coordinates;
import jb.Counter;
import mazeGame.monsters.*;

/*
 * Created: 16/1/2015
 * Last modified: 2/3/2015 (new monsters)
 */
public class MazeCell extends Cell {

	private boolean wall = false;
	private boolean light = false;
	private boolean exit = false;
	private boolean dark = false;

	/**
	 * 
	 * @param coord
	 *            coordinates
	 * @param contains
	 *            a char specifying what is initially in this cell
	 */
	public MazeCell(Coordinates coord, char contains) {
		super(coord);
		switch (contains) {
		case 'p':
			this.place(new Player(coord));
			break;

		case '+':
			this.wall = true;
			break;

		case 'l':
			this.light = true;
			break;

		case 'd':
			this.dark = true;
			break;

		case 'e':
			this.exit = true;
			break;

		case 'i':
			this.place(new SmartMonster(coord.getX(), coord.getY()));
			break;

		case 'q':
			this.place(new FastMonster(coord.getX(), coord.getY()));
			break;

		case 'r':
			this.place(new RandomMonster(coord.getX(), coord.getY()));
			break;

		case 'g':
			this.place(new GhostMonster(coord.getX(), coord.getY()));
			break;

		case 'b':
			this.place(new DiagonalMonster(coord.getX(), coord.getY()));
			break;

		case 'w':
			this.place(new WatcherMonster(coord.getX(), coord.getY()));
			break;
		
		case 'X':
			this.place(new GuardianMonster(coord.getX(), coord.getY()));
			break;

		case 'L':
			this.place(new LightBoost(coord.getX(), coord.getY()));
			break;
			
		case 'R':
			this.place(new Repulser(coord.getX(), coord.getY()));
			break;
		}
	}

	private MazeCell(int x, int y, boolean light, boolean wall, boolean exit, ArrayList<Counter> occupants) {
		super(x,y);
		this.light = light;
		this.wall = wall;
		this.exit = exit;
		this.room = occupants;
	}

	/**
	 * The coordinates of this Cell
	 * 
	 * @return Coordinate object
	 */
	public Coordinates loc() {
		return new Coordinates (xCoord, yCoord);
	}

	/**
	 * Is it a wall?
	 * 
	 * @return
	 */
	public boolean wall() {
		return wall;
	}

	/**
	 * either builds or breaks a wall in this cell
	 * 
	 * @param b
	 *            true, wall, false gets rid of it
	 */
	public void buildWall(boolean b) {
		this.wall = b;
	}

	/**
	 * Is it a light space?
	 * 
	 * @return
	 */
	public boolean light() {
		return light;
	}

	/**
	 * changes to a light space, or turns it off
	 * 
	 * @param l
	 *            true, light, false, switch off
	 */
	public void enlighten(boolean l) {
		this.light = l;
	}
	
	public void darken(boolean d) {
		this.dark = d;
	}

	/**
	 * Is it a darkness space?
	 * 
	 * @return
	 */
	public boolean dark() {
		return dark;
	}

	/**
	 * Is it the exit?
	 * 
	 * @return
	 */
	public boolean exit() {
		return exit;
	}

	/**
	 * sets or resets the exit to this cell;
	 * 
	 * @param e
	 *            true, exit here, false remove exit
	 */
	public void breakOut(boolean e) {
		this.exit = e;
	}

	/**
	 * Does it contain a monster?
	 * 
	 * @return
	 */
	public boolean monster() {
		if (isEmpty()) return false;
		return room.parallelStream().anyMatch(c ->((MazeCounter) c).isMonster());
	}
	
	/**
	 * Are the monsters in this cell awake?
	 * @return
	 */
	public boolean dangerous() {
		if (isEmpty()) return false;
		boolean awake = false;
		for (Counter c : room) {
			if (((MazeCounter) c).isMonster()) {
				Monster mon = (Monster) c;
				awake = awake || !mon.sleeping();
			}
		}
		return awake;
	}

	/**
	 * Gets the Monster(s) (if any) from the cell
	 */
	public ArrayList<Monster> getMonsters() {
		ArrayList<Monster> pit = new ArrayList<>();

		for (Counter c : room) {
			if (((MazeCounter) c).isMonster()) {
				pit.add(((Monster) c));
			}
		}

		return pit;
	}

	/** {@inheritDoc} */
	public Cell copy() {
		ArrayList<Counter> in = new ArrayList<>();
		for (Counter c : this.room) {
			in.add(c.copy());
		}
		return new MazeCell(xCoord, yCoord, light, wall, exit, in);
	}

	/**
	 * Is this cell ordinary? No power ups, no counters, no walls, not the way
	 * out
	 * 
	 * @return true if standard, false if special or occupied
	 */
	public boolean plain() {
		if (!isEmpty())
			return false;
		if (wall)
			return false;
		if (light)
			return false;
		if (exit)
			return false;
		if (dark)
			return false;
		return true;
	}

	public void wakeMonsters() {
		for (Monster m : getMonsters()) {
			m.wake();
		}
	}
	public void sleepMonsters() {
		for (Monster m : getMonsters()) {
			m.sleep();
		}
	}
	
	public Color getColour() {

		if (wall() && monster()) {
			return new Color(255, 50, 150);
		}
		if (wall())
			return Color.red;
		if (contains("Intel")) {
			return Color.blue;
		}
		if (contains("Fast")) {
			return new Color(0, 100, 100);
		}
		if (contains("Random")) {
			return new Color(0, 0, 150);
		}
		if (contains("Ghost")) {
			return new Color(255, 100, 100);
		}
		if (contains("Watch")) {
			return new Color(255, 215, 0);
		}
		if (contains("Bishop")) {
			return new Color(255, 128, 0);
		}
		if (contains("Guardian")) {
			return Color.orange;
		}
		if (monster()) {
			return new Color(0,0,0);
		}
		if (exit)
			return Color.cyan;
		if (light)
			return Color.yellow;
		if (dark)
			return Color.black;
		if (contains("Light")) {
			return new Color(200, 200, 50);
		}
		if (contains("Blaster")) {
			return new Color(175, 150, 150);
		}

		return Color.white;
	}

	public String toString() {

		if (wall)
			return "+";
		if (contains("Hero"))
			return "P";
		if (monster())
			return "M";
		if (light)
			return "L";
		if (exit)
			return "E";
		if (contains("Light") || contains("Blaster"))
			return "T";

		return " ";
	}
}
