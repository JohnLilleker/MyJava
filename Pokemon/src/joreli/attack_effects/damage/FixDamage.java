package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class FixDamage extends Damage {

	private int dam;

	public FixDamage(int d) {
		dam = d;
	}

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = 0;
		if (canHit(attacker, attack, defender, arena)) {
			damage = (dam > defender.getHealth()) ? defender.getHealth() : dam;
			int pain = defender.damage(damage, attack.getCategory(), arena);
			nextStep(attacker, pain, attack, defender, arena);
		}
		return damage;
	}

	public Damage copy() {
		return new FixDamage(dam);
	}

	public String description() {
		return "Causes " + dam + "hp damage. ";
	}

	public String code() {
		return "fix " + dam;
	}
}
