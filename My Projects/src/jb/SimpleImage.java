package jb;

import java.awt.Color;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Created: 19/6/2014
 * Last Modified: 26/11/2017 (fill(...))
 */
/**
 * A 2d array of squares, or scalable pixels, easier to draw than making a squirrel out of
 * geometric shapes. The location is stored within the object.
 *
 * @author JB227
 *
 */
public class SimpleImage {

	private Color[][] frame;
	private int scale;
	private int x;
	private int y;
	private int height;
	private int width;

	/**
	 * A white filled SimpleImage with a given size and location
	 * @param w
	 *            the width, it doesn't have to be a square
	 * @param h
	 *            the height of the image, relative to the number of squares not
	 *            pixels
	 * @param s
	 *            the size of each square
	 * @param x
	 *            the x coordinate of bottom left
	 * @param y
	 *            the y coordinate of bottom left
	 */
	public SimpleImage(int w, int h, int s, int x, int y) {

		frame = new Color[h][w];
		this.scale = s;
		this.x = x;
		this.y = y;
		this.height = h;
		this.width = w;

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				frame[j][i] = Color.WHITE;
			}
		}
	}
	
	/**
	 * A white filled SimpleImage with a given size and location
	 * @param w the width in squares
	 * @param h the height in squares
	 * @param s the size of each square
	 * @param co the location of the SimpleImage
	 */
	public SimpleImage(int w, int h, int s, Coordinates co) {
		this(w, h, s, co.getX(), co.getY());
	}

	/**
	 * A Simple Image at position 0,0
	 *
	 * @param w
	 *            squares wide
	 * @param h
	 *            squares high
	 * @param s
	 *            size of each square
	 */
	public SimpleImage(int w, int h, int s) {
		this(w, h, s, 0, 0);
	}

	/**
	 * A SimpleImage made of 2x2 squares each size 10 at coordinates 0,0
	 */
	public SimpleImage() {
		this(2, 2, 10, 0, 0);
	}

	/**
	 * Gets the colour at a certain index, or null if out of bounds<br>
	 * Note: 0,0 is bottom left
	 * @param x x coordinate within the SimpleImage
	 * @param y y coordinate within the SimpleImage
	 * @return the Color
	 */
	public Color getColour(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		return frame[y][x];
	}
	
	public Color getColour(Coordinates co) {
		return getColour(co.getX(), co.getY());
	}

	/**
	 * Gets the x coordinate of the SimpleImage's bottom left corner
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate of the SimpleImage's bottom left corner
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the Coordinates object of the SimpleImage's bottom left corner
	 * @return
	 */
	public Coordinates getCoordinates() {
		return new Coordinates(x, y);
	}

	/**
	 * Relative to the squares, multiply by scale for pixel value
	 * @return the number of squares high the object is
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Relative to the squares, multiply by scale for pixel value
	 * @return the number of squares wide the object is
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height and width of the SimpleImage in a Coordinates object 
	 * @return a Coordinates object where getX() -> width and getY() -> height
	 */
	public Coordinates getDimensions() {
		return new Coordinates(width, height);
	}
	
	/**
	 * Calculates the true height of the SimpleImage in pixels
	 * @return the height in pixels
	 */
	public int getTrueHeight() {
		return height*scale;
	}
	
	/**
	 * Calculates the true width of the SimpleImage in pixels
	 * @return the width in pixels
	 */
	public int getTrueWidth() {
		return width*scale;
	}
	
	/**
	 * Gets the height in pixels and width in pixels of the SimpleImage in a Coordinates object 
	 * @return a Coordinates object where getX() -> width in pixels and getY() -> height in pixels
	 */
	public Coordinates getTrueDimensions() {
		return new Coordinates(width, height).mul(scale);
	}

	/**
	 * Each square is of a certain size in pixels. This is it
	 * @return the size of each square
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Colours in a particular square
	 *
	 * @param x
	 * @param y
	 *            the coordinates of the square within the image, (0,0) is bottom left
	 * @param c
	 *            the colour
	 */
	public void colourIn(int x, int y, Color c) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		frame[y][x] = c;
	}
	
	/**
	 * Colours in a particular square
	 * @param co the coordinates of the square, (0,0) is bottom left
	 * @param c the Colour
	 */
	public void colourIn(Coordinates co, Color c) {
		colourIn(co.getX(), co.getY(), c);
	}

	/**
	 * Colours in a particular square
	 * 
	 * @param x
	 * @param y
	 *            the coordinates of the square within the image, (0,0) is bottom left
	 * @param r
	 *            red component
	 * @param g
	 *            green component
	 * @param b
	 *            blue component
	 */
	public void colourIn(int x, int y, int r, int g, int b) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		frame[y][x] = new Color(r, g, b);
	}
	
	/**
	 * Changes the colour of a square in the SimpleImage 
	 * @param co the index of the square to colour, (0,0) is bottom left
	 * @param r
	 *            red component
	 * @param g
	 *            green component
	 * @param b
	 *            blue component
	 */
	public void colourIn(Coordinates co, int r, int g, int b) {
		colourIn(co.getX(), co.getY(), r, g, b);
	}
	
	/**
	 * Colours in every square of one colour another colour, so change red to blue entirely for example
	 * @param o the colour you want to replace
	 * @param c the new colour
	 */
	public void colourIn(Color o, Color c) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getColour(x, y).equals(o)) {
					colourIn(x, y, c);
				}
			}
		}
	}
	
	/**
	 * Creates a new SimpleImage with all the colours inverted, or converted to negative
	 * @return the new SimpleImage with the inverted colours
	 */
	public SimpleImage invert() {
		SimpleImage inv = new SimpleImage(width, height, scale, x, y);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				Color p = getColour(i, j);
				inv.colourIn(i, j, 255-p.getRed(), 255-p.getGreen(), 255-p.getBlue());
			}
		}
		return inv;
	}

	/**
	 * Changes the coordinates of the image, moving it
	 *
	 * @param dx
	 *            change in x, not new coordinate. New coordinate is getX() + dx
	 * @param dy
	 *            change in y, not new coordinate. New coordinate is getY() + dy
	 */
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	/**
	 * Changes the coordinates of the image, moving it
	 *
	 * @param co a movement Coordinate. New coordinates are getCoordinates().add(co)
	 */
	public void move(Coordinates d) {
		this.x += d.getX();
		this.y += d.getY();
	}

	/**
	 * A complete movement, the new coordinates are the parameters
	 *
	 * @param x
	 * @param y
	 */
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * A complete movement to new coordinates
	 * @param co the new Coordinate
	 */
	public void setCoordinates(Coordinates co) {
		this.x = co.getX();
		this.y = co.getY();
	}
	
	/**
	 * Resizes the SimpleImage so each square is the given size in pixels
	 * @param size the new size of each square
	 */
	public void resize(int size) {
		this.scale = size;
	}
	
	/**
	 * Clones the SimpleImage, creating an identical SimpleImage at the same position
	 * @return a new identical SimpleImage
	 */
	public SimpleImage copy() {
		SimpleImage copy = new SimpleImage(width, height, scale, x, y);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				copy.frame[y][x] = frame[y][x];
			}
		}
		return copy;
	}
	
	/**
	 * Compares this SimpleImage with another, checking if they have the same colours and are the same size
	 * @param o The SimpleImage to compare to
	 * @return true iff this image and the other have the same dimensions and scale and every colour in this image is identical to the colour at the same index in the other SimpleImage<br>
	 * The position of the SimpleImages is irrelevant
	 */
	public boolean same(SimpleImage o) {
		if (!this.getDimensions().same(o.getDimensions()) || o.getScale() != getScale()) return false;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!o.getColour(x, y).equals(getColour(x, y))) return false;
			}
		}
		return true;
	}

	/**
	 * Reflects the current SimpleImage and returns the reflection
	 *
	 * @param yAxis
	 *            true if reflected horizontally (y axis), false reflected
	 *            vertically (x axis)
	 * @return the reflected SimpleImage, with the same coordinates.
	 */
	public SimpleImage reflect(boolean yAxis) {

		SimpleImage mirrored = new SimpleImage(this.width, this.height, this.scale, this.x, this.y);

		if (yAxis) {// switch x's

			for (int y = 0; y < this.height; y++) {
				for (int oldx = 0; oldx < this.width; oldx++) {
					int newx = (this.width - 1) - oldx;
					Color col = getColour(oldx, y);
					mirrored.colourIn(newx, y, col);
				}
			}

		} else { // switch y's

			for (int oldy = 0; oldy < this.height; oldy++) {
				for (int x = 0; x < this.width; x++) {
					int newy = (this.height - 1) - oldy;
					Color col = getColour(x, oldy);
					mirrored.colourIn(x, newy, col);
				}
			}

		}
		return mirrored;
	}
	
	/**
	 * Copies the colours on this SimpleImage to another.<br>
	 * A use case for this would be to share the pattern with a SimpleImage of different dimensions
	 * @param i the other SimpleImage, may or may not be the same size
	 */
	public void transfer(SimpleImage i) {
		for (int y = 0; y < getHeight(); y++) {
			// the given SimpleImage is smaller than this, stop looping
			if (y >= i.getHeight()) break;
			
			for (int x = 0; x < getWidth(); x++) {
				// see previous comment
				if (x >= i.getWidth()) break;
				
				i.colourIn(x, y, getColour(x, y));
			}
		}
	}
	/**
	 * Moves the pattern on the SimpleImage without changing the actual structure
	 * @param dx how much to move each colour left or right
	 * @param dy how much to move each colour up or down
	 * @return a new SimpleImage the same size and position, but each colour has been moved within the coordinate space
	 */
	public SimpleImage shiftColours(int dx, int dy) {
		SimpleImage s = new SimpleImage(getWidth(), getHeight(), getScale(), getX(), getY());
		
		for (int y = 0; y < getHeight(); y++) {
			int ny = y + dy;
			if (ny < 0 || ny >= s.getHeight()) continue;
			for (int x = 0; x < getWidth(); x++) {
				int nx = x + dx;
				if (nx < 0 || nx >= s.getWidth()) continue;
				s.colourIn(nx, ny, getColour(x, y));
			}
		}
		
		return s;
	}
	
	/**
	 * Moves the pattern on the SimpleImage without changing the actual structure
	 * @param d a Coordinate storing the change
	 * @return a new SimpleImage the same size and position, but each colour has been moved within the coordinate space
	 */
	public SimpleImage shift(Coordinates d) {
		return shiftColours(d.getX(), d.getY());
	}

	/**
	 * Colours in the entire SimpleImage one colour
	 *
	 * @param c
	 *            the colour
	 */
	public void fill(Color c) {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				colourIn(x, y, c);
			}
		}
	}
	
	/**
	 * Colours in the entire SimpleImage one colour
	 *
	 * @param r
	 *            red value
	 * @param g
	 *            green value
	 * @param b
	 *            blue value
	 */
	public void fill(int r, int g, int b) {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				colourIn(x, y, r, g, b);
			}
		}
	}

	/**
	 * Fills a shape within the SimpleImage, starting at the given location. The fill is stopped by another colour 
	 * @param x the x coordinate to start at. This is the colour that will be replaced
	 * @param y the y coordinate to start at. This is the colour that will be replaced
	 * @param c the new colour to fill
	 */
	public void fill(int x, int y, Color c) {
		// cancel out any strange logic
		Color t = getColour(x, y);
		if (t.equals(c)) return;
		fillTarget(c, t, x, y);
	}
	
	/**
	 * Fills a shape within the SimpleImage, starting at the given location. The fill is stopped by another colour 
	 * @param co a coordinate to start at. This is the colour that will be replaced
	 * @param c the new colour to fill
	 */
	public void fill(Coordinates co, Color c) {
		fill(co.getX(), co.getY(), c);
	}
	
	/**
	 * Colours in the SimpleImage, specifying a colour to change. Used in fill()
	 *
	 * @param c
	 *            the colour you want to add
	 * @param a
	 *            the colour you want to change
	 * @param x
	 *            a start point
	 * @param y
	 */
	private void fillTarget(Color c, Color t, int x, int y) {

		if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
			Color origin = getColour(x, y);

			if (origin.equals(t)) {
				colourIn(x, y, c);

				fillTarget(c, t, x + 1, y);
				fillTarget(c, t, x - 1, y);
				fillTarget(c, t, x, y + 1);
				fillTarget(c, t, x, y - 1);
			}
		}
	}

	/**
	 * Compares with another SimpleImage for overlap
	 *
	 * @param o
	 *            the other SimpleImage
	 * @return true if they share a coordinate
	 */
	public boolean overlaps(SimpleImage o) {
		if (getX() + getTrueWidth() < o.getX()+1 || o.getX() + o.getTrueWidth() < getX()+1 || 
				getY() + getTrueHeight() < o.getY()+1 || o.getY() + o.getTrueHeight() < getY()+1)
		    return false;
		else
		    return true;
	}

	/**
	 * Checks if the SimpleImage covers a certain point
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @return true if the point is covered by the SimpleImage
	 */
	public boolean covers(int x, int y) {
		return (x >= getX() && x < getX() + getTrueWidth() && y >= getY() && y < getY() + getTrueHeight());
	}
	
	/**
	 * Checks of the SimpleImage covers a ceratin point
	 * @param co the Coordinate object of the point
	 * @return true if the point is covered by the simpleImage
	 */
	public boolean covers(Coordinates co) {
		return covers(co.getX(), co.getY());
	}
	
	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param x the x coordinate of bottom left corner of the bounding box
	 * @param y the y coordinate of bottom left corner of the bounding box
	 * @param w the width of the bounding area
	 * @param h the height of the bounding area
	 * @return true if the SimpleImage has any part within the area described.
	 */
	public boolean inBounds(int x, int y, int w, int h) {
		if (getX() + getTrueWidth() < x+1 || x + w < getX()+1 || getY() + getTrueHeight() < y+1 || y + h < getY()+1)
		    return false;
		else
		    return true;
	}

	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param pos the Coordinates of the bottom left of the bounding area
	 * @param w the width of the bounding area
	 * @param h the height of the bounding area
	 * @return true if the SimpleImage has any part within the area described.
	 */
	public boolean inBounds(Coordinates pos, int w, int h) {
		return inBounds(pos.getX(), pos.getY(), w, h);
	}
	
	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param pos the Coordinates of the bottom left of the bounding area
	 * @param w the width of the bounding area
	 * @param h the height of the bounding area
	 * @return true if the SimpleImage has any part within the area described.
	 */
	public boolean inBounds(Coordinates pos, Coordinates di) {
		return inBounds(pos.getX(), pos.getY(), di.getX(), di.getY());
	}
	
	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param x the x coordinate of bottom left corner of the bounding box
	 * @param y the y coordinate of bottom left corner of the bounding box
	 * @param w the width of the bounding area
	 * @param h the height of the bounding area
	 * @return true if the SimpleImage is completely within the area described. If any part is out of the area this method returns false
	 */
	public boolean containedBy(int x, int y, int w, int h) {
		if (getX() >= x && getX() < x+w) {
			if (getY() >= y && getY() < y+h) {
				if (getX()+getWidth() >= x && getX()+getWidth() < x+w) {
					if (getY()+getHeight() >= y && getY()+getHeight() < y+h) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param pos the Coordinates of the bottom left of the bounding area
	 * @param w the width of the bounding area
	 * @param h the height of the bounding area
	 * @return true if the SimpleImage is completely within the area described. If any part is out of the area this method returns false
	 */
	public boolean containedBy(Coordinates pos, int w, int h) {
		return containedBy(pos.getX(), pos.getY(), w, h);
	}
	
	/**
	 * Checks the SimpleImage is within the bounds defined by the parameters
	 * @param pos the Coordinates of the bottom left of the bounding area
	 * @param di a Coordinates object storing the width (getX) and height (getY)
	 * @return true if the SimpleImage is completely within the area described. If any part is out of the area this method returns false
	 */
	public boolean containedBy(Coordinates pos, Coordinates di) {
		return containedBy(pos.getX(), pos.getY(), di.getX(), di.getY());
	}
	
	/**
	 * Finds the coordinates of the square that a particular point is within
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @return the coordinates of the square in which the point resides, or null if outside the SimpleImage
	 */
	public Coordinates coversAt(int x, int y) {
		for (int iy = 0; iy < height; iy++) {
			for (int ix = 0; ix < width; ix++) {
				if (x >= (getX() + (ix * getScale())) && x < (getX() + ((ix+1) * getScale()))) {
					if (y >= (getY() + (iy * getScale())) && y < (getY() + ((iy+1) * getScale()))) {
						return new Coordinates(ix, iy);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds the coordinates of the square that a particular point is within
	 * @param co the coordinates of the point
	 * @return the coordinates of the square in which the point resides, or null if outside the SimpleImage
	 */
	public Coordinates coversAt(Coordinates co) {
		return coversAt(co.getX(), co.getY());
	}
	
	/**
	 * Looks through the SimpleImage counting the occurrences of each colour
	 * @return a map of Color to number of squares of that colour
	 */
	public Map<Color, Integer> colourBalance() {
		Map<Color, Integer> balance = new HashMap<>();
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Color c = getColour(x, y);
				if (balance.containsKey(c)) {
					balance.put(c, balance.get(c)+1);
				}
				else {
					balance.put(c, 0);
				}
			}
		}
		
		return balance;
	}
	
	/**
	 * Creates a SimpleImage from a xml file at position 0,0
	 * @param file the file path of the xml file
	 * @return the SimpleImage the file describes, or null on errors
	 */
	public static SimpleImage fromFile(String file) {
		SimpleImage pic = null; 
		
		// event stream based approach to xml parsing, pretty memory efficient
		class CustomHandler extends DefaultHandler {
			private SimpleImage p = null;
			
			private boolean height = false, width = false, scale = false;
			private int h = -1, w = -1, s = -1;
			
			private boolean red = false, green = false, blue = false, alpha = false, xCoord = false, yCoord = false;
			private int x = -1, y = -1, r = -1, g = -1, b = -1, a = 255;
					
			@Override
			public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
				// read <(\w+)>
				String code = qName.toLowerCase();
				switch(code) {
				case "height":
					height = true;
					break;
				case "width":
					width = true;
					break;
				case "scale":
					scale = true;
					break;
				case "red":
					red = true;
					break;
				case "green":
					green = true;
					break;
				case "blue":
					blue = true;
					break;
				case "alpha" :
					alpha = true;
					break;
				case "xcoord":
					xCoord = true;
					break;
				case "ycoord":
					yCoord = true;
					break;
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				// read </(\w+)>
				if (qName.equalsIgnoreCase("dimensions")) {
					if (w != -1 && h != -1 && s != -1) {
						p = new SimpleImage(w, h, s);
					}
				}
				else if (qName.equalsIgnoreCase("fill")) {
					if (p != null && r != -1 && g != -1 && b != -1) {
						// optional alpha component
						Color c = (a != 255) ? new Color(r,g,b,a) : new Color(r,g,b);
						p.fill(c);
					}
					r = -1; g = -1; b = -1; a = 255;
				}
				else if (qName.equalsIgnoreCase("colour")) {
					if (p != null && x != -1 && y != -1 && r != -1 && g != -1 && b != -1) {
						// optional alpha component
						Color c = (a != 255) ? new Color(r,g,b,a) : new Color(r,g,b);
						p.colourIn(x, y, c);
					}
					r = -1; g = -1; b = -1; x = -1; y = -1; a = 255;
				}
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				// read data
				String str = new String(ch, start, length);
				if (str.trim().length() == 0) return;
				
				int i = Integer.valueOf(str);
				if (height) {
					h = i;
					height = false;
				}
				if (width) {
					w = i;
					width = false;
				}
				if (scale) {
					s = i;
					scale = false;
				}
				if (red) {
					r = i;
					red = false;
				}
				if (green) {
					g = i;
					green = false;
				}
				if (blue) {
					b = i;
					blue = false;
				}
				if (alpha) {
					a = i;
					blue = false;
				}
				if (xCoord) {
					x = i;
					xCoord = false;
				}
				if (yCoord) {
					y = i;
					yCoord = false;
				}
			}
			
			// the reason it isn't anonymous
			public SimpleImage getPic() { return p; }
		}
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			CustomHandler handler = new CustomHandler();
			parser.parse(file, handler);
			
			pic = handler.getPic();
		} catch(Exception e) {
			pic = null;
			e.printStackTrace();
		}
		
		return pic;
	}

	/**
	 * Writes the SimpleImage to a xml file. Note position is NOT stored
	 * @param file the file path of the file to write to
	 * @return true if the writing is successful, false on errors
	 */
	public boolean toFile(String file) {
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			
			// main root element
			Element root = document.createElement("image");
			document.appendChild(root);
			
			// constructor arguments
			Element dimensions = document.createElement("dimensions");
			root.appendChild(dimensions);
			Element height = document.createElement("height");
			height.appendChild(document.createTextNode(String.valueOf(this.height)));
			Element width = document.createElement("width");
			width.appendChild(document.createTextNode(String.valueOf(this.width)));
			Element scale = document.createElement("scale");
			scale.appendChild(document.createTextNode(String.valueOf(this.scale)));
			dimensions.appendChild(height); dimensions.appendChild(width); dimensions.appendChild(scale);
			
			// colours
			Element colours = document.createElement("colours");
			root.appendChild(colours);

			// fill, the most common colour. This means less squares need to be stored
			Map<Color, Integer> balance = this.colourBalance();
			Entry<Color, Integer> common = Collections.max(balance.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue());
			Color mode = common.getKey();
			
			if (common.getValue() > 1) {
				Element fill = document.createElement("fill");
				Element fRed = document.createElement("red"), fGreen = document.createElement("green"), fBlue = document.createElement("blue");
				fRed.appendChild(document.createTextNode(String.valueOf(mode.getRed())));
				fGreen.appendChild(document.createTextNode(String.valueOf(mode.getGreen())));
				fBlue.appendChild(document.createTextNode(String.valueOf(mode.getBlue())));
				fill.appendChild(fRed); fill.appendChild(fGreen); fill.appendChild(fBlue);
				// optional alpha
				if (mode.getAlpha() != 255) {
					Element alpha = document.createElement("alpha");
					alpha.appendChild(document.createTextNode(String.valueOf(mode.getAlpha())));
					fill.appendChild(alpha);
				}
				colours.appendChild(fill);
			}
			
			// store the colour and position of each square...
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					
					Color c = frame[y][x];
					// ...unless they are covered by <fill>
					if (c.equals(mode)) continue;
					
					// store each colour
					Element pix = document.createElement("colour");
					
					Element xco = document.createElement("xCoord");
					xco.appendChild(document.createTextNode(String.valueOf(x)));
					Element yco = document.createElement("yCoord");
					yco.appendChild(document.createTextNode(String.valueOf(y)));
					Element red = document.createElement("red");
					red.appendChild(document.createTextNode(String.valueOf(c.getRed())));
					Element green = document.createElement("green");
					green.appendChild(document.createTextNode(String.valueOf(c.getGreen())));
					Element blue = document.createElement("blue");
					blue.appendChild(document.createTextNode(String.valueOf(c.getBlue())));
					pix.appendChild(xco); pix.appendChild(yco); pix.appendChild(red); pix.appendChild(green); pix.appendChild(blue);
					
					// optional alpha
					if (c.getAlpha() != 255) {
						Element alpha = document.createElement("alpha");
						alpha.appendChild(document.createTextNode(String.valueOf(c.getAlpha())));
						pix.appendChild(alpha);
					}
					
					colours.appendChild(pix);
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(file));
			transformer.transform(source, result);
			
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
