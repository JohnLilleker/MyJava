package mazeGame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import jb.*;

/*
 * Created: 16/1/2015
 * Last modified: 27/8/2017 (old moves are still shown + updates to message board)
 */
@SuppressWarnings("serial")
public class MazeGame extends JFrame {

	private GPanel veiw;
	private Maze maze;
	private Maze start;
	private boolean[][] viewed;
	private JButton[] controls = new JButton[4];
	private JButton exit = new JButton("Exit");
	private JLabel movement = new JLabel();
	private JLabel repulsers = new JLabel();
	private JLabel messages = new JLabel();
	private int moves = 0;
	private int scale = 30;

	/**
	 * Random Maze
	 * 
	 */
	public MazeGame() {
		this(MazeGen.generate(20, 20));
	}

	/**
	 * @param spec
	 *            a specific maze name
	 */
	public MazeGame(Maze spec) {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		maze = spec;
		start = (Maze) maze.copy();
		int[] size = maze.getSize();
		
		this.viewed = new boolean[size[0]][size[1]];
		for (int y = 0; y < viewed.length; y++) {
			for (int x = 0; x < viewed[0].length; x++) {
				viewed[y][x] = false;
			}
		}

		setTitle("The Maze, " + maze.getName());
		// messages.setHorizontalTextPosition(arg0); // centre text?

		// centre the frame
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();

		Insets in = getInsets();
		int exW = size[1] * scale + in.left + in.right;
		int exH = size[0] * scale + 105 + in.top + in.bottom;
		setSize(exW, exH);

		while (this.getWidth() >= screen.width || this.getHeight() >= screen.height) {

			scale -= 5;
			exW = size[1] * scale + in.left + in.right;
			exH = size[0] * scale + 100 + in.top + in.bottom;
			setSize(exW, exH);
		}

		setLocation(new Point(screen.width / 2 - getWidth() / 2, (int) (screen.height / 2 - getHeight() / 1.9)));

		veiw = new GPanel(size[1] * scale, size[0] * scale);

		// {up, left, down, right}
		controls[0] = new JButton("Up");
		controls[1] = new JButton("Left");
		controls[2] = new JButton("Down");
		controls[3] = new JButton("Right");

		Container holder = getContentPane();
		holder.setLayout(null);

		holder.add(veiw);
		for (JButton d : controls) {
			holder.add(d);
		}
		holder.add(movement);
		holder.add(exit);
		holder.add(messages);
		holder.add(repulsers);

		int x = (size[1] * scale / 2) - 50;
		int y = size[0] * scale + 25;
		controls[0].setBounds(x, y, 100, 20); // top
		controls[1].setBounds(x - 105, y + 25, 100, 20); // to the left
		controls[2].setBounds(x, y + 25, 100, 20); // underneath
		controls[3].setBounds(x + 105, y + 25, 100, 20); // to the right

		exit.setBounds(x - 105, y, 100, 20);
		exit.setVisible(false);
		messages.setBounds(0, 0, size[1] * scale, 20);

		veiw.setBounds(0, 20, size[1] * scale, size[0] * scale);

		movement.setBounds(x - 65, y, 50, 20);
		repulsers.setBounds(x + 115, y, 150, 20);
		movement.setText(String.valueOf(moves));
		repulsers.setText("Repulsers: " + maze.getPlayer().getBursts() + "/3");
		messages.setText("Escape the maze. Good luck!");
	}

	/**
	 * Adds the controls to the game
	 */
	public void activate() {

		setVisible(true);

		drawMaze();

		// move
		// up
		controls[0].addActionListener(e -> {
			boolean[] res = turn(0);

			veiw.clear();
			drawMaze();

			veiw.requestFocusInWindow();

			if (res[0]) {
				result(res[1]);
			}
		});

		// left
		controls[1].addActionListener(e -> {

			boolean[] res = turn(1);

			veiw.clear();
			drawMaze();

			veiw.requestFocusInWindow();

			if (res[0]) {
				result(res[1]);
			}
		});

		// down
		controls[2].addActionListener(e -> {

			boolean[] res = turn(2);

			veiw.clear();
			drawMaze();

			veiw.requestFocusInWindow();

			if (res[0]) {
				result(res[1]);
			}
		});

		// right
		controls[3].addActionListener(e ->  {
			boolean[] res = turn(3);

			veiw.clear();
			drawMaze();

			veiw.requestFocusInWindow();

			if (res[0]) {
				result(res[1]);
			}
		});

		veiw.requestFocusInWindow();

		// keyboard input
		veiw.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				messages.setText("");
				boolean[] res = { false, false };

				switch (e.getExtendedKeyCode()) {

				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					res = turn(0);
					break;

				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					res = turn(3);
					break;

				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					res = turn(2);
					break;

				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					res = turn(1);
					break;
					
				case KeyEvent.VK_SPACE :
					res = turn(4);
					break;
					
				case KeyEvent.VK_B :
					res = turn(5);
					break;
					
				case KeyEvent.VK_ESCAPE:
					dispose();
					break;
				}

				veiw.clear();
				drawMaze();

				if (res[0]) {
					result(res[1]);
				}
			}

		});

	}

	private void result(boolean win) {
		// Draw a String

		int[] size = maze.getSize();
		int[] s = { size[0] * scale, size[1] * scale };
		String message;
		if (!win) {
			veiw.setColour(new Color(150, 50, 200));
			message = "You were eaten, but you lasted " + moves + " turns";
		} else {
			veiw.setColour(new Color(0, 0, 100));
			message = "You escaped in " + moves + " moves!";
		}

		veiw.drawString(message, 10, s[0] / 2 - 15, 30);

		for (JButton c : controls) {
			c.setEnabled(false);
		}

		messages.setText("To exit, press E. To restart, press R.");

		veiw.setEnabled(false);

		exit.setVisible(true);

		exit.requestFocusInWindow();

		exit.addActionListener( a -> {
			dispose();
		});

		exit.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getExtendedKeyCode() == KeyEvent.VK_R) {

					maze = start;
					start = (Maze) maze.copy();
					for (int y = 0; y < viewed.length; y++) {
						for (int x = 0; x < viewed[0].length; x++) {
							viewed[y][x] = false;
						}
					}
					veiw.clear();
					exit.setVisible(false);
					drawMaze();

					for (JButton c : controls) {
						c.setEnabled(true);
					}
					veiw.setEnabled(true);
					moves = 0;
					movement.setText(String.valueOf(moves));

					messages.setText("Try again. Good luck!");
					veiw.requestFocusInWindow();
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_E) {
					dispose();
				}
			}
		});
	}

	private boolean[] turn(int d) {

		Player you = maze.getPlayer();
		ArrayList<Monster> pack = maze.getMonsters();

		if (d < 4)
			you.move(d, maze);
		
		Coordinates pos = you.find();
		
		// pickups
		Cell cell = maze.getCell(pos);
		if (cell.contains("Light") && d < 4) {
			if (you.pickupBooster(cell.getCounter("Light"))) {
				cell.remove("Light");
				messages.setText("More Light!");
			}
			else {
				messages.setText("You have enough light for now");
			}
		}
		if (((MazeCell)cell).dark()) {
			((MazeCell)cell).sleepMonsters();
		}
		if (cell.contains("Blaster") && d < 4) {
			if (you.pickupRepulser()) {
				cell.remove("Blaster");
				messages.setText("A weapon, use it wisely...");
			}
			else {
				messages.setText("You can only carry 3 repulsers");
			}
		}
		// none moving events
		// lose a light
		if (d == 4 && you.dropBooster(maze)) {
			messages.setText("You dropped a light token");
		}
		// fight back!
		if (d == 5 && you.hit()) {
			messages.setText("BOOM!!!");
			burst(pos.getX(), pos.getY());
		}
		
		moves++;
		movement.setText(String.valueOf(moves));
		repulsers.setText(String.format("Repulsers: %d/3", you.getBursts()));

		// print a helpful message if close to goal
		Coordinates end = maze.goal();
		if (pos.dist(end) <= 5) {
			messages.setText("I think you're nearly there, keep going!");
		}
		
		if (gameOver()[0]) {
			return gameOver();
		} else { // their turn
			for (Monster m : pack) {
				if (!m.paralysed() && !m.sleeping())
					m.move(maze);
			}
			return gameOver();
		}
	}
	
	private void burst(int x, int y) {
		repulse(x+1, y, 1, 0, new ArrayList<Counter>());
		repulse(x, y+1, 0, 1, new ArrayList<Counter>());
		repulse(x-1, y, -1, 0, new ArrayList<Counter>());
		repulse(x, y-1, 0, -1, new ArrayList<Counter>());
		
		repulse(x+1, y+1, 1, 1, new ArrayList<Counter>());
		repulse(x-1, y-1, -1, -1, new ArrayList<Counter>());
		repulse(x+1, y-1, 1, -1, new ArrayList<Counter>());
		repulse(x-1, y+1, -1, 1, new ArrayList<Counter>());
	}
	
	private void repulse(int x, int y, int dx, int dy, ArrayList<Counter> carried) {
		if (maze.outOfRange(x, y)) {
			return;
		}
		
		MazeCell cell = maze.getMCell(x, y);
		int x2 = x + dx, y2 = y + dy;
		// knock back any sneaky nasties
		if (cell.wall()) {
			for (String id : cell.counterIDs()) {
				cell.getCounter(id).move(x2, y2, maze);
			}
			if (!maze.outOfRange(x2, y2)) maze.getMCell(x2, y2).getMonsters().forEach(mon -> mon.sparkOut());
			else maze.getMCell(x, y).getMonsters().forEach(mon -> mon.sparkOut());
			return;
		}
		if (maze.outOfRange(x2, y2) || maze.getMCell(x2, y2).wall()) {
			// split the arrayList and move half dx,0, half 0,dy
			if (dx != 0 && dy != 0) {
				
				if (maze.getMCell(x2, y).wall()) {
					repulse(x, y, 0, dy, carried);
					return;
				}
				if (maze.getMCell(x, y2).wall()) {
					repulse(x, y, dx, 0, carried);
					return;
				}
				
				ArrayList<Counter> xs = new ArrayList<>(), ys = new ArrayList<>();
				for (int i = 0; i < carried.size(); i++) {
					if (i % 2 == 0) {
						xs.add(carried.get(i));
					}
					else {
						ys.add(carried.get(i));
					}
				}
				repulse(x, y, dx, 0, xs);
				repulse(x, y, 0, dy, ys);
				return;
			}
			
			
			carried.forEach(c -> c.move(x, y, maze));
			cell.getMonsters().forEach(mon -> mon.sparkOut());
			repulse(x2, y2, dx, dy, null);
			return;
		}
		
		for (String id : cell.counterIDs()) {
			carried.add(cell.getCounter(id));
		}
		repulse(x2, y2, dx, dy, carried);
	}

	private boolean[] gameOver() {

		boolean[] results = { false, false }; // [0], game over? [1], win or
												// lose?
		// default - still in play, lose

		Coordinates heroLoc = maze.getPlayer().find();

		Coordinates end = maze.goal();

		ArrayList<Monster> bane = maze.getMonsters();

		if (heroLoc.same(end)) { // win!
			results[0] = true;
			results[1] = true;
		}

		else {
			for (Monster boo : bane) {
				Coordinates booLoc = boo.find();
				if (heroLoc.same(booLoc)) {
					results[0] = true;
				}
			}
		}

		return results;
	}

	private void drawMaze() {

		for (Monster w : maze.getMonsters()) {
			if (!w.sleeping() && messages.getText().equals(""))
				messages.setText("I feel movement...");
		}

		SimpleImage arena = new SimpleImage(maze.getWidth(), maze.getHeight(), scale);

		int h = arena.getHeight();
		int w = arena.getWidth();
		Player hero = maze.getPlayer();
		Coordinates pos = hero.find();

		if (maze.getMCell(pos).dark()) {

			messages.setText("Darkness everywhere!");

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					arena.colourIn(x, y, new Color(25, 25, 25));
					viewed[y][x] = false;
				}
			}

			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					Coordinates look = pos.add(x, y);
					if ((x != 0 || y != 0) && !maze.outOfRange(look) && (x == 0 ^ y == 0)) {
						arena.colourIn(look, maze.getMCell(look).getColour());
						viewed[look.getY()][look.getX()] = true;
					}
				}
			}
		}

		else if (!maze.getMCell(pos).light()) {
			// normal, can only see so far in all directions

			// shadows of darkness
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (viewed[y][x]) {
						Color c = maze.getMCell(x, y).getColour();
						Color fade = new Color(c.getRed()/2, c.getGreen()/2, c.getBlue()/2);
						arena.colourIn(x, y, fade);
					}
					else if (maze.getMCell(x, y).dangerous()) 
						arena.colourIn(x, y, new Color(100, 50, 50));
					else
						arena.colourIn(x, y, new Color(50, 50, 50));
				}
			}
			
			// recursive function to show light
			this.viewColour(pos.getX(), pos.getY(), hero.getLight(), arena);
		}

		else { // light space!
			messages.setText("LET THERE BE LIGHT!!!");
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					MazeCell hereM = maze.getMCell(x, y);
					arena.colourIn(x, y, hereM.getColour());;
					maze.getMCell(x, y).wakeMonsters();
				}
			}
		}

		if (maze.getMCell(pos).monster()) {
			arena.colourIn(pos, 255, 0, 255);
		} else {
			arena.colourIn(pos, Color.green);
		}

		veiw.drawImage(arena);
	}
	
	private void viewColour(int x, int y, int depth, SimpleImage place) {
		if (depth < 0)
			return;
		if (maze.outOfRange(x, y))
			return;
		
		// notice no check for previous colour
		// that's because it brings some VERY strange behaviour

		MazeCell mCell = maze.getMCell(x, y);
		place.colourIn(x, y, mCell.getColour());
		
		if (mCell.monster()) {
			messages.setText("Oh no! A monster found you!");
			mCell.wakeMonsters();
		}
		
		viewed[y][x] = true;
		int newDepth = depth - 1;
		if (mCell.wall())
			newDepth = depth - 2;
		
		viewColour(x+1, y, newDepth, place);
		viewColour(x-1, y, newDepth, place);
		viewColour(x, y+1, newDepth, place);
		viewColour(x, y-1, newDepth, place);
	}
}