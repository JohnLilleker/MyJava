package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class DestructEffect extends Effect {

	/**
	 * Knocks out the attacking Pokemon
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		attacker.damage(attacker.getHealthMax(), Attack.Category.STATUS, arena);
	}

	public Effect copy() {
		return new DestructEffect();
	}

	public String description() {
		return "The user immediately faints after using this move. ";
	}

	public String code() {
		return "destruct";
	}
}
