package wargames;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import jb.*;
import joreli.Pokedex;
import joreli.Pokemon;

public class Chase extends JFrame {

	private static final long serialVersionUID = 1L;

	private GPanel graphic;
	private ArrayList<Unit> swarm = new ArrayList<>();
	private ArrayList<int[]> points = new ArrayList<>();
	private boolean pause = false;

	public Chase(int w, int h, int number) {
		setTitle("Follow the mouse click");

		Insets in = getInsets();
		int exW = w + in.left + in.right;
		int exH = h + in.top + in.bottom;
		setSize(exW, exH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container cont = getContentPane();
		graphic = new GPanel(w, h);
		cont.add(graphic, "Center");

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenDimensions = toolkit.getScreenSize();
		setLocation(new Point((screenDimensions.width / 2 - this.getWidth() / 2),
				(screenDimensions.height / 2 - this.getHeight() / 2)));

		setResizable(false);
		setVisible(true);

		pack();
		Pokedex g = new Pokedex(false);
		g.readPok();

		for (int i = 0; i < number; i++) {

			SimpleImage fl = new SimpleImage(5, 5, 10);

			int r = MathsToolKit.random(50, 255);
			int gr = MathsToolKit.random(50, 255);
			int b = MathsToolKit.random(50, 255);
			fl.fill(new Color(r, gr, b));
			
			fl.colourIn(1, 3, Color.BLACK);
			fl.colourIn(3, 3, Color.BLACK);
			fl.colourIn(2, 2, Color.BLACK);
			fl.colourIn(3, 1, Color.BLACK);
			fl.colourIn(1, 1, Color.BLACK);
			fl.colourIn(2, 1, Color.BLACK);
			fl.colourIn(1, 0, Color.BLACK);
			fl.colourIn(3, 0, Color.BLACK);
			

			int[] ggtt = { MathsToolKit.random(w - 25), MathsToolKit.random(h - 25) };
			Pokemon p = g.randomPokemon();
			p.setLevel(50);
			swarm.add(new Unit(fl, p, ggtt, "Feed me", true));
		}

		// new point added with every click
		graphic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int x = e.getX();
				int y = (graphic.getHeight() - 1) - e.getY();

				int[] newP = { x, y };
				points.add(newP);

				redraw();
			}

		});

		// or stroke
		graphic.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = graphic.invert(e.getY());

				int[] newP = { x, y };
				points.add(newP);

				redraw();
			}

		});

		for (int i = 0; i < number; i++) {
			int[] p = { MathsToolKit.random(w), MathsToolKit.random(h) };

			points.add(p);
		}
		redraw();

		go();

		redraw();
	}

	private void redraw() {

		graphic.clear();

		for (Unit u : swarm) {
			graphic.drawImage(u.getImage());
		}

		graphic.setColour(Color.black);

		for (int[] p : points) {

			graphic.drawCentredEllipse(15, 15, p[0], p[1], true);
		}
	}

	private void go() {

		try {
			redraw();
			while (!pause || points.size() > 0) {
				for (Unit s : swarm) {

					int[] c = find(s);
					s.orders(s.decide(c));
					s.move();
					redraw();

					if (pause)
						break;

					Iterator<int[]> i = points.iterator();

					while (i.hasNext()) {
						int[] p = i.next();
						if (MathsToolKit.euclidDist(s.centre(), p) < 20) {
							i.remove();
						}
					}
					redraw();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				redraw();
			}
		} catch (ConcurrentModificationException e) {
			go();
		}
	}

	private int[] find(Unit u) {
		int[] me = u.centre();

		Pair<int[], Double> best = new Pair<>(me, Double.MAX_VALUE);

		for (int[] p : points) {
			double dist = MathsToolKit.euclidDist(me, p);
			if (dist < best.second()) {
				best.first(p);
				best.second(dist);
			}
		}
		return best.first();
	}

	public static void main(String[] args) {

		new Chase(300, 300, 1);

	}

}
