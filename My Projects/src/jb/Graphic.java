package jb;

import javax.swing.*;
import java.awt.*;

/*
 * Created: 24/6/2014
 * Last Modified: 16/1/2015 (removed MyPanel, now GPanel in another file) 
 */
/**
 * A basic window containing a GPanel. Can also hide visibility and tell you if
 * it won't work
 * 
 * @author JB227
 */
@SuppressWarnings("serial")
public class Graphic extends JFrame {

	private boolean visable;
	private GPanel canvas;

	/**
	 * @param w
	 *            Width of window
	 * @param h
	 *            Height of window
	 */
	public Graphic(int w, int h) {
		setTitle("Canvas");
		Insets in = getInsets();
		int exW = w + in.left + in.right;
		int exH = h + in.top + in.bottom;
		setSize(exW, exH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container content = this.getContentPane();
		canvas = new GPanel(w, h);
		content.add(canvas, "Center");
		setResizable(false);
		setVisible(true);
		visable = true;
		pack();

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenDimensions = toolkit.getScreenSize();
		setLocation(new Point((screenDimensions.width / 2 - this.getWidth() / 2),
				(screenDimensions.height / 2 - this.getHeight() / 2)));

	}

	public Graphic(int w, int h, String t) {
		this(w, h);
		setTitle(t);
	}

	/**
	 * Sets the current colour
	 * 
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 */
	public void setColour(int r, int g, int b) {
		canvas.setColour(new Color(r, g, b));
	}

	/**
	 * Sets the current colour, the next line would be in this colour
	 * @param c the Color object
	 */
	public void setColour(Color c) {
		canvas.setColour(c);
	}

	/**
	 * The Color of a coordinate
	 * 
	 * @param x
	 * @param y
	 * @return a number from 0-255 that indicates red
	 */
	public Color getColor(int x, int y) {
		return canvas.getColour(x, y);
	}

	/**
	 * Clears the window
	 */
	public void clear() {
		canvas.clear();
	}

	/**
	 * Changes the colour of the background
	 * 
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 */
	public void backGroundFill(int r, int g, int b) {
		canvas.background(new Color(r, g, b));
	}
	
	/**
	 * Changes the colour of the background
	 * 
	 * @param c the background colour
	 */
	public void backGroundFill(Color c) {
		canvas.background(c);
	}

	/**
	 * Moves the cursor to a point
	 * 
	 * @param x
	 *            the target x
	 * @param y
	 *            the target y
	 */
	public void moveTo(int x, int y) {
		canvas.moveTo(x, y);
	}

	/**
	 * Draws a String
	 * 
	 * @param m
	 *            the String to be drawn
	 * @param x
	 *            bottom left x coordinate
	 * @param y
	 *            bottom left y coordinate
	 * @param size
	 */
	public void drawString(String m, int x, int y, int size) {
		canvas.drawString(m, x, y, size);
	}

	/**
	 * Draws a line from one point to another
	 * 
	 * @param x
	 *            start x
	 * @param y
	 *            start y
	 * @param finx
	 *            end x
	 * @param finy
	 *            end y
	 */
	public void drawLine(int x, int y, int finx, int finy) {
		canvas.drawLine(x, y, finx, finy);
	}

	/**
	 * Draws a line from current position to another point
	 * 
	 * @param x
	 *            end x
	 * @param y
	 *            end y
	 */
	public void drawLineTo(int x, int y) {
		canvas.lineTo(x, y);
	}

	/**
	 * Draws a SimpleImage, the 2d array of Pix
	 * 
	 * @param i
	 *            the SimpleImage to be drawn, coordinates are contained within
	 *            the object
	 */
	public void drawImage(SimpleImage i) {
		canvas.drawImage(i);
	}

	/**
	 * Draws an Image from a file
	 * 
	 * @param f
	 *            the file name
	 * @param x
	 *            bottom left x
	 * @param y
	 *            bottom left y
	 */
	public void drawImage(String f, int x, int y) {
		canvas.drawImage(f, x, y);
	}

	/**
	 * Draws an Image if you give it directly, saves faffing about
	 * 
	 * @param i
	 *            the Image as an object
	 * @param x
	 *            bottom left x
	 * @param y
	 *            bottom left y
	 */
	public void drawImage(Image i, int x, int y) {
		canvas.drawImage(i, x, y);
	}

	/**
	 * Draws the Image from a file reflected, better than having separate files
	 * 
	 * @param f
	 *            the file name
	 * @param x
	 *            bottom left x
	 * @param y
	 *            bottom left y
	 * @param horizon
	 *            true for horizontal reflection (in the y axis), false vertical
	 *            (x axis)
	 */
	public void drawReflectedImage(String f, int x, int y, boolean horizon) {
		canvas.drawRefectedImage(f, x, y, horizon);
	}

	/**
	 * Draws a Rectangle
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @param x
	 *            bottom left x
	 * @param y
	 *            bottom left y
	 * @param fill
	 *            true for filled, false for empty
	 */
	public void drawRectangle(int h, int w, int x, int y, boolean fill) {
		canvas.drawRectangle(h, w, x, y, fill);
	}

	/**
	 * Draws a Ellipse
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @param x
	 *            bottom left x
	 * @param y
	 *            bottom left y
	 * @param fill
	 *            true for filled, false for empty
	 */
	public void drawEllipse(int h, int w, int x, int y, boolean fill) {
		canvas.drawEllipse(h, w, x, y, fill);
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
		canvas.drawEllipse(h, w, (x - w / 2), (y - h / 2), fill);
	}

	/**
	 * Changes the visibility, hide or show
	 * 
	 * @param vis
	 *            true for hide, false to reveal the window in it's glory
	 * @return true if it makes sense, hide(false) if not visible for example
	 */
	public boolean hide(boolean vis) {

		if (vis) { // hide
			if (visable) { // if it is visible
				setVisible(false);
				return true;
			} // else
			return false;
		}

		else { // show
			if (!visable) {
				setVisible(true);
				return true;
			}
			return false;
		}
	}

	public void tryShape() {

		canvas.drawShape(4, 30, 40, 17, true);

	}
}
