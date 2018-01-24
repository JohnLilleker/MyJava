package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class CriticalDamage extends Damage {

	/**
	 * Has a high critical hit ratio
	 */
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new CriticalDamage();
	}

	protected int criticalLevel(Pokemon attacker) {
		return super.criticalLevel(attacker) + 1;
	}

	public String description() {
		return "High critical hit ratio. ";
	}

	public String code() {
		return "critical";
	}
}
