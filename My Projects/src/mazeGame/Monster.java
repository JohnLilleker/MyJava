package mazeGame;


/*
 * Created: 16/1/2015
 * Last modified: 9/9/2017 (Monster is an abstract subclass of MazeCounter)
 */
/**
 * Maze's Computer controlled Baddies
 * 
 * @author JB227
 *
 */
public abstract class Monster extends MazeCounter {

	protected boolean sleep = true;
	protected int koCount = 0;
	
	public Monster(int x, int y, String m) {
		super(x, y, m);
	}

	/**
	 * Move
	 * 
	 * @param here
	 */
	public abstract void move(Maze here);

	/**
	 * Is it asleep?
	 * 
	 * @return
	 */
	public boolean sleeping() {
		return sleep;
	}
	
	public boolean paralysed() {
		if (koCount > 0) {
			koCount--;
			return true;
		}
		return false;
	}
	
	public boolean isMonster() {
		return true;
	}

	/**
	 * It can now hunt you down!
	 */
	public void wake() {
		sleep = false;
	}
	
	public void sleep() {
		sleep = true;
	}

	public void sparkOut() {
		koCount = 3;
	}
	
}
