package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class CounterDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = 0;
		if (canHit(attacker, attack, defender, arena)) {

			switch (attack.getCategory()) {
			case PHYSICAL:
				damage = 2 * attacker.getPhysicalDamage();
				break;
			case SPECIAL:
				damage = 2 * attacker.getSpecialDamage();
				break;
			default:
				break;
			}

			if (damage == 0) {
				arena.log("But it failed!");
			} else {
				damage = (damage > defender.getHealth()) ? defender.getHealth() : damage;
				int pain = defender.damage(damage, attack.getCategory(), arena);
				nextStep(attacker, pain, attack, defender, arena);
			}
		}

		return damage;
	}

	public Damage copy() {
		return new CounterDamage();
	}

	public String description() {
		return "Inflicts double the damage taken this turn. ";
	}

	public String code() {
		return "counter";
	}

}
