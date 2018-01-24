package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.attack_effects.Describable;

/**
 * A class describing the effect an attack has on a Pokemon. Extensions give
 * behaviour such as absorbing, conditions, status conditions etc
 * 
 * @author JB227
 *
 */
public abstract class Effect implements Describable {

	public enum Target {
		SELF, TARGET, ALL
	};

	/**
	 * Causes an effect to pokemon
	 * 
	 * @param attacker
	 *            attacking pokemon
	 * @param damage
	 *            how much damage the defender has already suffered
	 * @param defender
	 *            defending pokemon
	 * @param arena
	 *            the battle
	 */
	public abstract void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena);

	/**
	 * Override for each Effect
	 * 
	 * @return A copy of the effect
	 */
	public abstract Effect copy();

	public String toString() {
		return description();
	}
}
