package jb;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/*
 * Created: 16/1/2015 (rather, in its own file. Previously in Graphic)
 * Last modified: 26/11/2017 (Minor fixes)
 */
/**
 * A component based on a JPanel that allows shapes to be drawn on it.
 * SimpleImages and Image files can also be used on it. To be used as part of a
 * JFrame. (0,0) is bottom left
 * 
 * @author JB227
 */
@SuppressWarnings("serial")
public class GPanel extends JPanel {

	private int width;
	private int height;
	private int cursorX = 0;
	private int cursorY = 0;
	private Color currentColour = Color.BLACK;

	private BufferedImage image = null;
	private Graphics2D canvas2d;

	private void warning(String w) {
		setColour(Color.RED);
		drawString(w, width / 2, height / 4, 30);
		System.exit(0);
	}

	/**
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public GPanel(int w, int h) {
		height = h;
		width = w;
		setPreferredSize(new Dimension(w, h));
		setBounds(width, height, 0, 0);
		setSize(w, h);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		setBackground(Color.WHITE);
		canvas2d = image.createGraphics();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, null);
	}
	
	/**
	 * Clears the panel
	 */
	public void clear() {
		background(Color.WHITE);
	}

	/**
	 * Draws a String
	 * 
	 * @param m
	 *            the String
	 * @param x
	 * @param y
	 *            coordinates
	 * @param size
	 *            how big
	 */
	public void drawString(String m, int x, int y, int size) {
		canvas2d.setColor(currentColour);
		Font s = new Font("SanSerif", Font.PLAIN, size);
		canvas2d.setFont(s);
		canvas2d.drawString(m, x, invert(y));
		repaint();
	}

	/**
	 * What colour the next shapes will be
	 * 
	 * @param c
	 */
	public void setColour(Color c) {
		currentColour = c;
	}

	/**
	 * Draws a line from current position to (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public void lineTo(int x, int y) {
		drawLine(cursorX, cursorY, x, y);
		moveTo(x, y);
	}

	/**
	 * Moves the cursor
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		cursorX = x;
		cursorY = y;
	}

	/**
	 * Block fills the panel
	 * 
	 * @param c
	 *            the desired colour
	 */
	public void background(Color c) {
		setColour(c);
		drawRectangle(height + 10, width + 10, -5, -5, true);
	}

	/**
	 * Draws a line from (x,y) to (finx,finy)
	 * 
	 * @param x
	 * @param y
	 * @param finx
	 * @param finy
	 */
	public void drawLine(int x, int y, int finx, int finy) {
		moveTo(finx, finy);
		Shape l = new Line2D.Double(x, invert(y), finx, invert(finy));
		canvas2d.setColor(currentColour);
		canvas2d.draw(l);
		repaint();
	}

	/**
	 * Draws a rectangle
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @param x
	 * @param y
	 *            coordinates
	 * @param fill
	 *            true, filled false, empty
	 */
	public void drawRectangle(int h, int w, int x, int y, boolean fill) {
		moveTo(x + w, y + h);
		Shape r = new Rectangle2D.Double(x, invert(y) - h, w, h);
		canvas2d.setColor(currentColour);
		if (fill)
			canvas2d.fill(r);
		else
			canvas2d.draw(r);
		repaint();
	}

	/**
	 * Draws an ellipse
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @param x
	 * @param y
	 *            coordinates of bottom left corner of bounding box
	 * @param fill
	 *            true, filled false, empty
	 */
	public void drawEllipse(int h, int w, int x, int y, boolean fill) {
		moveTo(x + w / 2, y + h / 2);
		Shape e = new Ellipse2D.Double(x, invert(y) - h, w, h);
		canvas2d.setColor(currentColour);
		if (fill)
			canvas2d.fill(e);
		else
			canvas2d.draw(e);
		repaint();
	}

	/**
	 * Draws an Ellipse
	 * 
	 * @param h
	 *            the height
	 * @param w
	 *            the width
	 * @param x
	 *            the centre x
	 * @param y
	 *            the centre y
	 * @param fill
	 *            true for filled, false for empty
	 */
	public void drawCentredEllipse(int h, int w, int x, int y, boolean fill) {
		drawEllipse(h, w, (x - w / 2), (y - h / 2), fill);
	}

	/*
	 * What I want to do, drawShape(sides, size, x, y, filled) A regular shape;
	 * number of sides, length of sides, ?, ?, filled or not
	 */

	public void drawShape(int sides, int size, int x, int y, boolean filled) {
		// ^ ^ No idea what these represent
		// new Polygon(int[], int[], int)
		// canvas2d.drawPolygon(^)/fillPolygon(^)

		// Graphic has drawTriangle(), drawPentagon(), drawHexagon()

		int[] nx = { 10, 10, 30 };
		int[] ny = { 10, 30, 30 };
		int un = 3;

		canvas2d.setColor(currentColour);

		canvas2d.fillPolygon(nx, ny, un);
		// ^ ^ ^
		// x coords
		// y coords
		// number of coord pairs
	}

	/**
	 * Draws a SimpleImage at its current coordinates as a rectangle of squares
	 * 
	 * @param i
	 *            the SimpleImage
	 */
	public void drawImage(SimpleImage i) {

		Color c = this.currentColour;
		for (int x = 0; x < i.getWidth(); x++) {
			for (int y = 0; y < i.getHeight(); y++) {
				setColour(i.getColour(x, y));
				drawRectangle(i.getScale(), i.getScale(), i.getX() + (x * i.getScale()), i.getY() + (y * i.getScale()), true);
			}
		}
		setColour(c);
	}

	/**
	 * Reads from an image file and draws the image
	 * 
	 * @param file
	 *            the file name
	 * @param x
	 * @param y
	 *            coordinates
	 */
	public void drawImage(String file, int x, int y) {

		try {
			BufferedImage pic = ImageIO.read(new File(file));
			canvas2d.drawImage(pic, x, invert(y), null);

		} catch (IOException e) {
			warning("Image File: " + file + " not found");
		}

	}

	/**
	 * Draws a given Image
	 * 
	 * @param i
	 *            the Image
	 * @param x
	 * @param y
	 *            coordinates
	 */
	public void drawImage(Image i, int x, int y) {
		canvas2d.drawImage(i, x, invert(y), null);
	}

	/**
	 * Reads the Image from a file, and flips it before drawing it
	 * 
	 * @param file
	 *            file name
	 * @param x
	 * @param y
	 *            coordinates
	 * @param yaxis
	 *            true, flips horizontally false, upside-down
	 */
	public void drawRefectedImage(String file, int x, int y, boolean yaxis) {

		try {
			BufferedImage pic = ImageIO.read(new File(file));

			if (yaxis) { // horizontally
				AffineTransform t = AffineTransform.getScaleInstance(-1, 1);
				t.translate(-pic.getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				pic = op.filter(pic, null);
			}

			else { // vertically
				AffineTransform t = AffineTransform.getScaleInstance(1, -1);
				t.translate(0, -pic.getHeight());
				AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				pic = op.filter(pic, null);
			}

			canvas2d.drawImage(pic, x, invert(y), null);

		} catch (IOException e) {
			warning("Image File: " + file + " not found");
		}

	}

	/**
	 * Gets the colour of a certain pixel
	 * 
	 * @param x
	 * @param y
	 *            the pixel coordinates
	 * @return the Color of that pixel
	 */
	public Color getColour(int x, int y) {
		return new Color(image.getRGB(x, invert(y)));
	}

	/**
	 * Flips the y axis
	 * 
	 * @param y
	 *            raw parameter
	 * @return updated y coord
	 */
	public int invert(int y) {
		return ((height) - y -1);
	}
}
