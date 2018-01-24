package games;

import java.util.Random;

/* Created 19/5/2014
 * Last modified 21/5/2014
 */
public abstract class MicroGame {

	protected Random random = new Random();

	public abstract boolean getWin();

	public abstract void play();

	public abstract String getType();

	public abstract String getName();

}
