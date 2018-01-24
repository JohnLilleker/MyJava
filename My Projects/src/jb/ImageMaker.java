package jb;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Can be used to create SimpleImages
 * 
 * @author JB227
 *
 */
public class ImageMaker extends JFrame {

	private static final long serialVersionUID = -506065113008542261L;

	private String file = null;
	private SimpleImage image;
	private GPanel window;
	// check it has been saved, store the last saved then compare
	private SimpleImage saved;
	// size
	private final int WIDTH = 775;
	private final int HEIGHT = 655;
	private final int CANVAS_WIDTH = 600;
	private final int CANVAS_HEIGHT = 600;

	private final Coordinates CANVAS_DIMS = new Coordinates(CANVAS_WIDTH, CANVAS_HEIGHT);

	// real size of squares
	private int scale;
	// displayed size of square
	private int zoom;
	// undo history
	private Deque<SimpleImage> history;
	// redo future
	private Deque<SimpleImage> redo;

	private Color primary = Color.BLACK;
	private Color secondary = Color.WHITE;

	// tools to interact with the image
	private enum Tool {
		// colour in a square
		COLOUR,
		// fill the image
		FILL,
		// colour in white
		ERASE,
		// select a colour from the image
		PICKER
	}

	// mouse button
	private enum Click {
		LEFT, RIGHT
	}

	private Tool currentTool = Tool.COLOUR;

	// store the last action. This means if someone repeatedly clicks the same
	// square it would only be put in history once
	private Coordinates lastClick = null;
	private Tool lastTool = null;
	private Click lastButton = null;
	private Color lastColour = null;

	// global as the state can change
	private JMenuItem undoBtn;
	private JMenuItem redoBtn;
	private JButton primaryColourBtn;
	private JButton secondaryColourBtn;
	private JLabel zoomLabel;

	public ImageMaker() {
		setResizable(false);
		setTitle("Image Maker - Untitled");
		setSize(WIDTH, HEIGHT);
		getContentPane().setLayout(null);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent w) {
				if (confirmSaved("Are you sure you want to quit?"))
					System.exit(0);
			}
		});

		window = new GPanel(CANVAS_WIDTH, CANVAS_HEIGHT);
		// a bit of fun :P
		window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		window.setLocation(0, 0);
		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change(e);
			}
		});
		window.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				change(e);
			}
		});
		getContentPane().add(window);

		// a centered image
		image = new SimpleImage(10, 10, 50, 50, 50);
		saved = image.copy();
		scale = image.getScale();
		zoom = image.getScale();

		history = new ArrayDeque<>(20);
		redo = new ArrayDeque<>(20);

		JPanel controls = new JPanel();
		controls.setBounds(600, 0, 169, 600);
		getContentPane().add(controls);

		primaryColourBtn = new JButton("Primary Colour");
		primaryColourBtn.setBackground(primary);
		primaryColourBtn.addActionListener(e -> {
			Color p = pickColour(primary);
			if (p != null) {
				primary = p;
				primaryColourBtn.setBackground(p);
			}
		});

		secondaryColourBtn = new JButton("Secondary Colour");
		secondaryColourBtn.setBackground(secondary);
		secondaryColourBtn.addActionListener(e -> {
			Color s = pickColour(secondary);
			if (s != null) {
				secondary = s;
				secondaryColourBtn.setBackground(s);
			}
		});

		JButton btnColourIn = new JButton("Colour In");
		btnColourIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentTool = Tool.COLOUR;
			}
		});

		JButton btnErase = new JButton("Erase");
		btnErase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentTool = Tool.ERASE;
			}
		});

		JButton btnFill = new JButton("Fill");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentTool = Tool.FILL;
			}
		});

		JButton btnPicker = new JButton("Picker");
		btnPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentTool = Tool.PICKER;
			}
		});

		// TODO make the zoom scale better
		JButton btnZoomOut = new JButton("-");
		btnZoomOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1)
					zoom(0.5);
				else
					zoom(0.75);
			}
		});

		JButton btnZoomIn = new JButton("+");

		btnZoomIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1)
					zoom(1.5);
				else
					zoom(1.25);
			}
		});

		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveImg(0, 10);
			}
		});

		JButton btnLeft = new JButton("Left");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveImg(-10, 0);
			}
		});

		JButton btnRight = new JButton("Right");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveImg(10, 0);
			}
		});

		JButton btnDown = new JButton("Down");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveImg(0, -10);
			}
		});

		JButton btnRestore = new JButton("Restore");
		btnRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoom = scale;
				image.resize(scale);
				zoomLabelUpdate();
				centreImg();
				redraw();
			}
		});

		JLabel lblZoomLevel = new JLabel("Zoom level");

		zoomLabel = new JLabel("100.00%");

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		GroupLayout gl_controls = new GroupLayout(controls);
		gl_controls.setHorizontalGroup(gl_controls.createParallelGroup(Alignment.TRAILING).addGroup(gl_controls
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_controls.createParallelGroup(Alignment.TRAILING).addGroup(gl_controls
						.createSequentialGroup()
						.addGroup(gl_controls.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnColourIn, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(secondaryColourBtn, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(primaryColourBtn, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(btnErase, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(btnFill, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(btnPicker, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addGroup(gl_controls.createSequentialGroup().addComponent(btnZoomOut)
										.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE).addGroup(
												gl_controls.createParallelGroup(Alignment.LEADING).addComponent(btnUp)
														.addGroup(gl_controls
																.createSequentialGroup()
																.addGroup(gl_controls
																		.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblZoomLevel)
																		.addGroup(gl_controls.createSequentialGroup()
																				.addGap(2).addComponent(zoomLabel)))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(btnZoomIn))))
								.addGroup(gl_controls.createSequentialGroup().addComponent(btnLeft)
										.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
										.addComponent(btnRight))
								.addComponent(btnRestore, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
						.addContainerGap())
						.addGroup(gl_controls.createSequentialGroup().addComponent(btnDown).addGap(52))))
				.addGroup(Alignment.LEADING, gl_controls.createSequentialGroup().addGap(29).addComponent(btnNewButton)
						.addContainerGap(51, Short.MAX_VALUE)));
		gl_controls.setVerticalGroup(gl_controls.createParallelGroup(Alignment.LEADING).addGroup(gl_controls
				.createSequentialGroup().addContainerGap().addComponent(primaryColourBtn)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(secondaryColourBtn)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnColourIn)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnErase)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnFill)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnPicker)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_controls.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_controls.createSequentialGroup()
								.addGroup(gl_controls.createParallelGroup(Alignment.LEADING).addComponent(btnZoomOut)
										.addGroup(gl_controls.createSequentialGroup().addComponent(lblZoomLevel)
												.addGap(3).addComponent(zoomLabel)))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnUp))
						.addComponent(btnZoomIn))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_controls.createParallelGroup(Alignment.BASELINE).addComponent(btnLeft)
						.addComponent(btnRight))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDown)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRestore).addGap(18)
				.addComponent(btnNewButton).addContainerGap(222, Short.MAX_VALUE)));
		controls.setLayout(gl_controls);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		JMenuItem newOption = new JMenuItem("New");
		// create a new one, set file = null
		newOption.addActionListener(e -> {
			if (confirmSaved("Create new Image?"))
				newImg();
		});
		menu.add(newOption);

		JMenuItem open = new JMenuItem("Open");
		// open a file
		open.addActionListener(e -> {
			if (confirmSaved("Open new Image?"))
				open();
		});
		menu.add(open);

		JMenuItem save = new JMenuItem("Save");
		// save through file, or if file not defined use save as
		save.addActionListener(e -> {
			save();
		});
		menu.add(save);

		JMenuItem saveAs = new JMenuItem("Save As...");
		// pick a file
		saveAs.addActionListener(e -> {
			saveAs();
		});
		menu.add(saveAs);

		JMenuItem quit = new JMenuItem("Quit");
		// close, but check save first
		quit.addActionListener(e -> {
			if (confirmSaved("Are you sure you want to quit?"))
				System.exit(0);
		});
		menu.add(quit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		undoBtn = new JMenuItem("Undo");
		undoBtn.setEnabled(false);
		mnEdit.add(undoBtn);

		redoBtn = new JMenuItem("Redo");
		redoBtn.setEnabled(false);
		mnEdit.add(redoBtn);

		undoBtn.addActionListener(e -> {
			undo();
		});
		redoBtn.addActionListener(e -> {
			redo();
		});

		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.addActionListener(e -> {
			clear();
		});
		mnEdit.add(mntmClear);

		/*
		 * TODO Controls Under 'Edit'? change height (resizeImg(image.getWidth(), h); )
		 * [number field] change width (resizeImg(w, image.getHeight()); ) [number
		 * field] change scale (storeHistory(); image.resize(s); scale = s; zoom = s;
		 * redraw(); ) [number field]
		 * 
		 * button design fill / single square colourIn / erase / picker [ bucket, pen,
		 * rubber, picker icons, also change cursor] show true size [icon] arrow key or
		 * buttons -> move image [arrow icons] buttons (zoom) -> resize image
		 * [magnifying icons]
		 * 
		 * Save / load progress bar? Or at least a marker somewhere
		 */
		redraw();
	}

	private boolean confirmSaved(String title) {

		if (checkSaved()) {
			return true;
		} else {
			int response = JOptionPane.showConfirmDialog(null, "You will lose any unsaved changes", title,
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				return true;
			}
		}
		return false;
	}

	private void moveImg(int dx, int dy) {
		image.move(dx, dy);
		if (!image.inBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT)) {
			image.move(-dx, -dy);
		}
		redraw();
	}

	private void resizeImg(int w, int h) {
		storeHistory();
		SimpleImage n = new SimpleImage(w, h, zoom);
		n.setCoordinates(image.getCoordinates());
		image.transfer(n);
		image = n;
		redraw();
	}

	private void change(MouseEvent e) {

		int x = e.getX();
		// invert the y
		int y = window.invert(e.getY());
		Click button = null;
		if (SwingUtilities.isLeftMouseButton(e)) {
			button = Click.LEFT;
		}
		if (SwingUtilities.isRightMouseButton(e)) {
			button = Click.RIGHT;
		}

		// only left and right click allowed
		if (button == null)
			return;

		if (!image.covers(x, y))
			return;

		Coordinates coordinate = image.coversAt(x, y);

		Color select = (button == Click.LEFT) ? primary : secondary;

		// prevent repeated history
		if (lastClick != null && coordinate.same(lastClick) && lastTool == currentTool && select == lastColour
				&& lastButton == button)
			return;

		// do the effect
		switch (currentTool) {
		case COLOUR: // colours in a square
			storeHistory();
			image.colourIn(coordinate, select);
			break;
		case ERASE: // colours in white
			storeHistory();
			image.colourIn(coordinate, Color.WHITE);
			break;
		case FILL: // fills the image
			storeHistory();
			image.fill(coordinate, select);
			break;
		case PICKER: // colour picker, gets a colour from the image
			switch (button) {
			case LEFT:
				primary = image.getColour(coordinate);
				primaryColourBtn.setBackground(primary);
				break;
			case RIGHT:
				secondary = image.getColour(coordinate);
				secondaryColourBtn.setBackground(secondary);
				break;
			}
			currentTool = Tool.COLOUR;
			return;
		}

		lastClick = coordinate;
		lastTool = currentTool;
		lastColour = select;
		lastButton = button;
		redraw();
	}

	private void zoom(double z) {
		int old = zoom;
		zoom *= z;
		if (zoom == old) {
			if (z < 1)
				zoom--;
			else
				zoom++;
		}
		if (zoom < 1)
			zoom = 1;
		image.resize(zoom);
		zoomLabelUpdate();
		centreImg();
		redraw();
	}

	private void newImg() {
		// TODO optionPane for height, width, scale
		image = new SimpleImage(10, 10, 50, 50, 50);

		saved = image.copy();
		file = null;
		scale = image.getScale();
		zoom = image.getScale();
		zoomLabelUpdate();
		history.clear();
		redo.clear();
		undoBtn.setEnabled(false);
		redoBtn.setEnabled(false);
		setTitle("Image Maker - Untitled");
		history.clear();
		redraw();
	}

	private void save() {
		if (file == null)
			saveAs();
		else {
			saveToFile();
			redraw();
		}
	}

	private void saveAs() {
		// TODO
		// jFileChooser
		// pick new file (need to subclass JFileChooser?)
		// file = file
		// setTitle("Image Maker - " + file)
		// saveToFile();
		redraw();
	}

	private Color pickColour(Color first) {
		// TODO my own colour chooser?
		// make sure history is stored, remove / amend preview
		return JColorChooser.showDialog(null, "Choose a colour", first);
	}

	private void saveToFile() {
		SimpleImage toSave = image.copy();
		toSave.resize(scale);
		toSave.toFile(file);
		saved = toSave.copy();
	}

	private void open() {

		JFileChooser chooser;
		chooser = new JFileChooser(file);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Images", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String file = chooser.getSelectedFile().getAbsolutePath();
			image = SimpleImage.fromFile(file);
			if (image != null) {
				this.file = file;
				centreImg();
				saved = image.copy();
				scale = image.getScale();
				zoom = image.getScale();
				zoomLabelUpdate();
				setTitle("Image Maker - " + file);
				redraw();
				history.clear();
				redo.clear();
			} else {
				newImg();
			}
		}
	}

	private void centreImg() {
		image.setCoordinates(CANVAS_DIMS.sub(image.getTrueDimensions()).div(2));
	}

	private void redraw() {
		// clear
		window.background(new Color(200, 200, 200));
		// draw image
		window.drawImage(image);
	}

	private void undo() {
		if (history.size() > 0) {
			SimpleImage past = history.pop();

			past.setCoordinates(image.getCoordinates());
			scale = past.getScale();
			past.resize(zoom);
			redo.push(image.copy());
			redoBtn.setEnabled(true);
			image = past;
			if (history.size() == 0) {
				undoBtn.setEnabled(false);
			}
			redraw();
		}
	}

	private void redo() {
		if (redo.size() > 0) {
			SimpleImage next = redo.pop();

			next.setCoordinates(image.getCoordinates());
			scale = next.getScale();
			next.resize(zoom);
			history.push(image.copy());
			undoBtn.setEnabled(true);
			image = next;
			if (redo.size() == 0) {
				redoBtn.setEnabled(false);
			}
			redraw();
		}
	}

	private void clear() {
		storeHistory();
		image.fill(Color.WHITE);
		redraw();
	}

	private void storeHistory() {
		// any updates to the image
		history.push(image.copy());
		if (history.size() == 20) {
			history.removeLast();
		}
		undoBtn.setEnabled(true);
		if (redo.size() > 0) {
			redoBtn.setEnabled(false);
			redo.clear();
		}
	}

	private boolean checkSaved() {
		SimpleImage cpy = image.copy();
		cpy.resize(scale);
		return saved.same(cpy);
	}

	private void zoomLabelUpdate() {
		float lvl = ((float) zoom) / scale;
		zoomLabel.setText(String.format("%.2f%%", lvl * 100));
	}

	public static void main(String args[]) {
		(new ImageMaker()).setVisible(true);
	}
}
