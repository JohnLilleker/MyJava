package mazeGame;

import java.util.ArrayList;
/*
 * Created: 10/1/2015
 * Last modified: 26/8/2017 (Better maze generation)
 */
import java.util.Collections;

import jb.Coordinates;
import mazeGame.monsters.GuardianMonster;

/**
 * Makes a maze given the dimensions. No need to instantiate, all methods static
 * 
 * @author JB227
 *
 */
public class MazeGen {

	/**
	 * Creates a maze using only height and width, name "Generated"
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @return the Maze
	 */
	public static Maze generate(int h, int w) {
		return generate(h, w, "Generated");
	}

	/**
	 * Makes a maze
	 * 
	 * @param h
	 *            height
	 * @param w
	 *            width
	 * @param n
	 *            a name
	 * @return the maze
	 */
	public static Maze generate(int h, int w, String n) {

		int s_h = 2*h+1;
		int s_w = 2*w+1;
		
		boolean[][] visited = new boolean[h][w];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				visited[y][x] = false;
			}
		}
		
		Maze ret = new Maze(s_h, s_w, n);
		
		// fill maze with walls, then break out cells
		for (int x = 0; x < s_w; x++) {
			for (int y = 0; y < s_h; y++) {
				if (x % 2 == 0 || y % 2 == 0)
					ret.getMCell(x, y).buildWall(true);
				else
					ret.getMCell(x, y).buildWall(false);
			}
		}
		
		// {wall_x, wall_y, cell_x, cell_y}
		ArrayList<int[]> wall_list = new ArrayList<>();
		
		int[] start = randomCoords(h, w);
		int xs = start[0], ys = start[1];
		visited[ys][xs] = true;
		
		int[] s_start = scaleUp(start);
		int s_xs = s_start[0], s_ys = s_start[1];
		
		if (xs > 0) {
			wall_list.add(new int[]{s_xs-1, s_ys, s_xs, s_ys});
		}
		if (ys > 0) {
			wall_list.add(new int[]{s_xs, s_ys-1, s_xs, s_ys});
		}
		if (xs < w-1) {
			wall_list.add(new int[]{s_xs+1, s_ys, s_xs, s_ys});
		}
		if (ys < h-1) {
			wall_list.add(new int[]{s_xs, s_ys+1, s_xs, s_ys});
		}
		
		while(!wall_list.isEmpty()) {
			Collections.shuffle(wall_list);
			int[] wall = wall_list.get(0);
			
			int wx = wall[0], wy = wall[1], cx = wall[2], cy = wall[3];
			int[] next = nextCell(wx, wy, cx, cy);
			int nx = next[0], ny = next[1];
			int[] s_n = scaleDown(next);
			int s_nx = s_n[0], s_ny = s_n[1]; 
			
			
			if (!visited[s_ny][s_nx]) {
				// break wall
				ret.getMCell(wx, wy).buildWall(false);
				
				// mark as visited 
				visited[s_ny][s_nx] = true;
				
				// add new walls
				if (s_nx > 0) {
					wall_list.add(new int[]{nx-1, ny, nx, ny});
				}
				if (s_ny > 0) {
					wall_list.add(new int[]{nx, ny-1, nx, ny});
				}
				if (s_nx < w-1) {
					wall_list.add(new int[]{nx+1, ny, nx, ny});
				}
				if (s_ny < h-1) {
					wall_list.add(new int[]{nx, ny+1, nx, ny});
				}	
			}
			wall_list.remove(wall);
		}
		// reset player and exit
		Coordinates p = ret.getPlayer().find();
		ret.getCell(p).remove("Hero");

		Coordinates exit = ret.goal();
		ret.getMCell(exit).breakOut(false);
		
		// set randomly out of 4 corners
		ArrayList<int[]> s_e = new ArrayList<>();
		s_e.add(new int[]{1, 1});
		s_e.add(new int[]{1, s_h-2});
		s_e.add(new int[]{s_w-2, s_h-2});
		s_e.add(new int[]{s_w-2, 1});
		
		Collections.shuffle(s_e);
		
		int[] ent = s_e.get(0);
		int[] end = s_e.get(1);
		
		ret.getCell(ent[0], ent[1]).place(new Player(ent[0], ent[1]));
		ret.getMCell(end[0], end[1]).breakOut(true);
		
		int monsters = h*w/50;
		int lighters = h*w/75;
		int shadows = h*w/100;
		int i;
		for (i = 0; i < monsters; i++) {
			int[] monster = scaleUp(randomCoords(h, w));

			int monx = monster[0], mony = monster[1];
			MazeCell cell = ret.getMCell(monx, mony);
	
			if (cell.isEmpty()) {
				cell.place(new GuardianMonster(monx, mony));
			}
		}
		for (i = 0; i < lighters; i++) {
			int[] light = scaleUp(randomCoords(h, w));
			int lx = light[0], ly = light[1];
			MazeCell cell = ret.getMCell(lx, ly);
			if (!cell.contains("Hero")) cell.place(new LightBoost(lx, ly));
		}
		for (i = 0; i < shadows; i++) {
			int[] shadow = scaleUp(randomCoords(h, w));
			int sx = shadow[0], sy = shadow[1];
			MazeCell cell = ret.getMCell(sx, sy);
			if (!cell.contains("Hero")) cell.darken(true);
		}
		for (i = 0; i < lighters; i++) {
			int[] blast = scaleUp(randomCoords(h, w));
			int sx = blast[0], sy = blast[1];
			MazeCell cell = ret.getMCell(sx, sy);
			if (cell.isEmpty()) cell.place(new Repulser(sx, sy));;
		}
		
		return ret;
	}
	
	private static int[] scaleUp(int[] x) {		
		return new int[]{(2*x[0])+1, (2*x[1])+1};
	}
	private static int[] scaleDown(int[] x) {		
		return new int[]{(x[0]-1)/2, (x[1]-1)/2};
	}
	private static int[] nextCell(int wx, int wy, int cx, int cy) {
		// w_x = c_x-1, so n_x = w_x - 1
		int nx = wx - (cx - wx);
		int ny = wy - (cy - wy);
		return new int[]{nx, ny};
	}

	/**
	 * Is there a way from the player to the exit?
	 * 
	 * @param m
	 *            the maze in question
	 * @return true if path found, false for impossible maze
	 */
	public static boolean validate(Maze m) {

		Coordinates player = m.getPlayer().find();
		return validRec(m, player, new ArrayList<Coordinates>());
	}

	private static boolean validRec(Maze m, Coordinates coords, ArrayList<Coordinates> arrayList) {

		arrayList.add(coords);

		// base case, found exit
		if (m.getMCell(coords).exit()) {
			return true;
		}

		ArrayList<Coordinates> glance = movesFrom(m, coords);
		ArrayList<Coordinates> filt = new ArrayList<>();

		// dead end

		for (Coordinates now : glance) {
			Coordinates at = coords.add(now);
			if (!arrayList.contains(at)) {
				filt.add(now);
			}
		}

		if (filt.isEmpty()) {
			return false;
		}

		// try next move
		for (Coordinates move : filt) {
			Coordinates next = coords.add(move);

			ArrayList<Coordinates> hist = new ArrayList<>();
			hist.addAll(arrayList);

			if (validRec(m, next, hist))
				return true;
		}

		return false;
	}
	
	private static int[] randomCoords(int height, int width) {

		int x = -1;
		int y = -1;

		while (x < 0 || x >= width || y < 0 || y >= height) {

			x = (int) ((Math.random() * width));
			y = (int) ((Math.random() * height));

		}

		int[] c = { x, y };

		return c;
	}

	private static ArrayList<Coordinates> movesFrom(Maze m, Coordinates coords) {

		ArrayList<Coordinates> moves = new ArrayList<>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 ^ y == 0) {
					Coordinates work = new Coordinates(x, y);
					Coordinates move = coords.add(work);
					if (!m.outOfRange(move)) {
						if (!m.getMCell(move).wall()) {
							moves.add(work);
						}
					}
				}
			}
		}

		return moves;
	}
}
