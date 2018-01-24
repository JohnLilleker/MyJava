package animation;

import java.awt.Color;
import jb.*;

public class Colourful {
	private Graphic window = new Graphic(255, 255);
	private SimpleImage cover = new SimpleImage(256, 256, 1);

	public void colour(String c) {

		for (int x = 0; x < cover.getWidth(); x++) {
			for (int y = 0; y < cover.getHeight(); y++) {

				Color col = new Color(0, 0, 0);
				if (c.equalsIgnoreCase("rg"))
					col = new Color(x, y, 0);
				if (c.equalsIgnoreCase("rb"))
					col = new Color(x, 0, y);
				if (c.equalsIgnoreCase("gb"))
					col = new Color(0, x, y);

				cover.colourIn(x, y, col);
			}
		}
		window.drawImage(cover);
	}

	public static void main(String[] args) {

		Colourful rg = new Colourful();
		rg.colour("rg");

		Colourful rb = new Colourful();
		rb.colour("rb");

		Colourful bg = new Colourful();
		bg.colour("gb");
	}
}
