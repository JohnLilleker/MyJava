package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class HitProtectedDamage extends Damage {

	/**
	 * Can even hit targets protecting themselves
	 */
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	protected boolean isProtected(Pokemon defender, Pokemon attacker, Attack attack) {
		// to decrease the protect counter, but also make sure it has no effect
		defender.isProtecting();
		return false;
	}

	public Damage copy() {
		return new HitProtectedDamage();
	}

	public String description() {
		return "Can even hit Pokemon protecting themselves. ";
	}

	public String code() {
		return "hit_protect";
	}

}
