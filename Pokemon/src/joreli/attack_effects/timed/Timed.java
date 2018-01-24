package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;
import joreli.attack_effects.Describable;

/**
 * Decides when an Attack should be used and how many times
 * 
 * @author JB227
 *
 */
public abstract class Timed implements Describable {

	/**
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @param attack
	 *            The calling Attack
	 * @param defender
	 *            The Target Pokemon
	 * @param arena
	 *            the current battle
	 */
	public abstract void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena);

	/**
	 * 
	 * @return A copy of this class
	 */
	public abstract Timed copy();

	public boolean canUseWithCondition(Condition c) {
		switch (c) {
		case FREEZE:
			return false;
		case SLEEP:
			return false;
		default:
			return true;

		}
	}

	public String toString() {
		return description();
	}
}
