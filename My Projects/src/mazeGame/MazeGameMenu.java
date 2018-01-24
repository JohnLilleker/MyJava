package mazeGame;

import javax.swing.*;

import jb.Reader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * Created: 9/2/2015
 * Last modified: 12/2/2015 (MazeGen labels)
 */
@SuppressWarnings("serial")
public class MazeGameMenu extends JFrame {

	private JButton play = new JButton("Go");
	private JLabel mazeL = new JLabel("Select a maze:");
	private JComboBox<String> mazes = new JComboBox<>();
	private ArrayList<Maze> options;

	public MazeGameMenu() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setTitle("Menu");
		setResizable(false);

		setSize(250, 150);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		setLocation(new Point(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2));

		mazes();
		
		mazes.addItem("Random");
		for (Maze o : options) {
			mazes.addItem(o.getName());
		}
		mazes.addItem("Generated S");
		mazes.addItem("Generated M");
		mazes.addItem("Generated L");
		mazes.addItem("Generated XL");

		Container holder = getContentPane();
		holder.setLayout(null);

		holder.add(play);
		holder.add(mazeL);
		holder.add(mazes);

		play.addActionListener(e -> {
			Maze choice = load((String) mazes.getSelectedItem());

			MazeGame mg = new MazeGame(choice);

			mg.activate();
		});

		setVisible(true);

		play.setBounds(120, 75, 100, 20);
		mazeL.setBounds(20, 25, 100, 20);
		mazes.setBounds(120, 25, 100, 20);
	}
	
	private void mazes() {

		Reader fog = new Reader("src/mazeGame/maps.txt");
		options = new ArrayList<>();

		while (!fog.endOfFile()) {

			String size = fog.read();
			if (size == null)
				break;
			if (size.startsWith("!"))
				break;
			int len = 0;
			try {
				len = Integer.valueOf(size);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			String[] maze = new String[len + 1];
			for (int row = 0; row < len + 1; row++) {
				maze[row] = fog.read();
			}

			String[] mazeData = Arrays.copyOfRange(maze, 1, maze.length);

			options.add(new Maze(mazeData, maze[0]));
		}
	}
	
	private Maze load(String name) {

		Maze thisOne = null;

		if (name.equals("Random")) {
			Collections.shuffle(options);
			thisOne = options.get(0);
		}

		else if (name.startsWith("Generated")) {
			String[] sep = name.split("\\s");

			String size = sep[1];
			switch (size) {

			case "S":
				thisOne = MazeGen.generate(10, 10, name);
				break;
			case "M":
				thisOne = MazeGen.generate(15, 15, name);
				break;
			case "L":
				thisOne = MazeGen.generate(20, 25, name);
				break;
			case "XL":
				thisOne = MazeGen.generate(30, 35, name);
				break;
			}
		} else {
			for (Maze m : options) {
				if (m.getName().equalsIgnoreCase(name)) {
					thisOne = m;
					break;
				}
			}
		}

		return thisOne;
	}

}
