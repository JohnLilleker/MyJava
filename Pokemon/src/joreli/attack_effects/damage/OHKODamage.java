package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class OHKODamage extends Damage {

	/**
	 * Kills the defender immediately, if it hits
	 */
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = 0;
		if (canHit(attacker, attack, defender, arena)) {
			damage = defender.getHealth();
			Battle b = new Battle();
			b.setLogMethod(arena.getLogMode());
			defender.damage(damage, attack.getCategory(), b);
			nextStep(attacker, damage, attack, defender, arena);
		}
		return damage;
	}

	/**
	 * The protection cannot be broken via aiming
	 * 
	 */
	protected boolean isProtected(Pokemon defender, Pokemon attacker, Attack a) {
		return defender.isProtecting();
	}

	public Damage copy() {
		return new OHKODamage();
	}

	public String description() {
		return "Knocks out the target if it hits. ";
	}

	public String code() {
		return "OHKO";
	}

}
