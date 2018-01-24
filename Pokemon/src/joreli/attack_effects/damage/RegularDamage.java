package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RegularDamage extends Damage {

	/**
	 * causes default damage to the opponent
	 */
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new RegularDamage();
	}

	public String description() {
		return "A regular Attack. ";
	}

	public String code() {
		return "";
	}

}
