import java.awt.Color;

import jb.Counter;

public class SnakeFood extends Counter {

	private Color colour;
	private static int order = 0;

	public SnakeFood(int x, int y) {
		super(x, y, "Food");
		int[][] colours = { { 255, 77, 77 }, { 133, 79, 171 }, { 244, 192, 155 }, { 163, 241, 233 }, { 60, 158, 251 } };
		int[] c = colours[SnakeFood.order];
		order++;
		if (order == colours.length) {
			order = 0;
		}
		this.colour = new Color(c[0], c[1], c[2]);
	}

	@Override
	public Counter copy() {
		return new SnakeFood(xCoord, yCoord);
	}

	public Color getColour() {
		return colour;
	}

}
