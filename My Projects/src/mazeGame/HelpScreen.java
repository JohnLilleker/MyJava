package mazeGame;

import javax.swing.JFrame;

import jb.Coordinates;
import jb.GPanel;
import jb.SimpleImage;

/*
 * A help screen
 * Created: 2/9/2017
 */
@SuppressWarnings("serial")
public class HelpScreen extends JFrame {
	
	private enum Info {
		LIGHT, DARK, BOOST, BLAST, DIAG, FAST, GHOST, GUARD, RAND, SMART, WATCH
	}
	
	public HelpScreen(Maze maze) {
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		/*
		 * Set up contents
		 * 
		 * Need a panel for controls
		 * A scroll panel for maze information?
		 */
		
		
		
		readMaze(maze);
		setVisible(true);
	}
	
	private void readMaze(Maze maze) {
		boolean[] information = new boolean[Info.values().length];
		for (int i = 0; i < information.length; i++) {
			information[i] = false;
		}
		
		for (int x = 0; x < maze.getWidth(); x++) {
			for (int y = 0; y < maze.getHeight(); y++) {
				MazeCell cell = maze.getMCell(x, y);
				// special cells
				if (cell.dark())
					information[Info.DARK.ordinal()] = true;
				if (cell.light())
					information[Info.LIGHT.ordinal()] = true;
				// pickups
				if (cell.contains("Blaster"))
					information[Info.BLAST.ordinal()] = true;
				if (cell.contains("Light"))
					information[Info.BOOST.ordinal()] = true;
				// monsters
				if (cell.contains("Intel"))
					information[Info.SMART.ordinal()] = true;
				if (cell.contains("Fast"))
					information[Info.FAST.ordinal()] = true;
				if (cell.contains("Random"))
					information[Info.RAND.ordinal()] = true;
				if (cell.contains("Ghost"))
					information[Info.GHOST.ordinal()] = true;
				if (cell.contains("Watch"))
					information[Info.WATCH.ordinal()] = true;
				if (cell.contains("Bishop"))
					information[Info.DIAG.ordinal()] = true;
				if (cell.contains("Guardian"))
					information[Info.GUARD.ordinal()] = true;
			}
		}
		
		for (int i = 0; i < information.length; i++) {
			if (information[i]) {
				GPanel panel = new GPanel(20, 20);
				SimpleImage pic = new SimpleImage(1, 1, 20);
				MazeCell example = null;
				String text = "";
				String name = "";
				switch(Info.values()[i]) {
				case BLAST:
					example = new MazeCell(new Coordinates(0,0), 'L');
					name = "Repulser";
					text = "A single use weapon that affects all 8 directions. When it hits a Counter it is sent flying into the nearest wall in that direction, "
							+ "paralysing Monsters for a couple of turns and knocking Monsters off / out off walls. Take care though, you can only carry 3 at once. "
							+ "Press 'B' to use";
					break;
				case BOOST:
					example = new MazeCell(new Coordinates(0,0), 'R');
					name = "Light Booster";
					text = "This increases your field of view, one is enough to start pentrating walls! Don't forget if a monster is covered, they are aware of"
							+ " your presence and will try to attack... Luckily you can drop a counter to shrink the view again, just press the Spacebar";
					break;
				case DARK:
					example = new MazeCell(new Coordinates(0,0), 'd');
					name = "Dark cell";
					text = "A special Cell, it wipes the monsters making them forget you exist! However, the darkness also covers all progress so far so use caution";
					break;
				case DIAG:
					example = new MazeCell(new Coordinates(0,0), 'b');
					name = "Diagonal Monster";
					text = "A Monster that chases you DIAGONALLY! This also means it can run though walls... But it needs a turn to get through each. It's"
							+ " movements means trapping you is easy so keep an eye on them.";
					break;
				case FAST:
					example = new MazeCell(new Coordinates(0,0), 'q');
					name = "Leap Monster";
					text = "These are the sppedsters. They move in leaps, covering 2 steps each time they move. They also leap over you, but that's a surefire"
							+ " way of trapping you. If they hit a wall mid jump however, they render themselves unconcious for a turn, so leg it!";
					break;
				case GHOST:
					example = new MazeCell(new Coordinates(0,0), 'g');
					name = "Ghost Monster";
					text = "These spooky monsters can move through walls, though they're solid enough to take a turn to get out of them. this makes them harder to trap "
							+ "than most monsters but also slightly slower if walls are involved";
					break;
				case GUARD:
					example = new MazeCell(new Coordinates(0,0), 'X');
					name = "Guardian Monster";
					text = "An insomniac Monster, never sleeping but instead they wander the maze, looking for something to chase. If they find you, they go into a frenzy,"
							+ " even climbing over walls to get to you!";
					break;
				case LIGHT:
					example = new MazeCell(new Coordinates(0,0), 'l');
					name = "Light Cell";
					text = "A special Cell that reveals the entire maze, allowing you to see the exit! However, the Monsters can also see you...";
					break;
				case RAND:
					example = new MazeCell(new Coordinates(0,0), 'r');
					name = "Glitch Monster";
					text = "A very strange breed of Monster, when woken it moves randomly regardless of where you are, unless it lands on you...";
					break;
				case SMART:
					example = new MazeCell(new Coordinates(0,0), 'i');
					name = "Smart Monster";
					text = "In theory, the smartest Monster, it methodically plans a route to get to you but it has a short attention span and so is easily avoided";
					break;
				case WATCH:
					example = new MazeCell(new Coordinates(0,0), 'w');
					name = "Watcher Monster";
					text = "A rather peculiar Monster, light and dark have no effect on it. Instead it stands still, looking for you... If you happen to land in it's "
							+ "line of sight, it will charge towards you, or the last place it saw you. With careful movements you can easily avoid it";
					break;
				}
				pic.colourIn(0, 0, example.getColour());
				panel.drawImage(pic);
				System.out.println(name);
				System.out.println(text);
			}
		}
		/*
		 * check each enum
		 * 	if info[enum]
		 * 		Show a cell with enum condition
		 * 		give some helpful info on the subject
		 * 
		 * i.e.
		 * 	if information[Info.LIGHT.ordinal] 
		 * 		have a small panel showing a cell with light = true
		 * 		String = "A special cell that lights up the entire maze, waking EVERY monster so use with caution";
		 */
		
	}
	
}
