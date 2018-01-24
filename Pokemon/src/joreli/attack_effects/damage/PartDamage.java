package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class PartDamage extends Damage {

	private float part;

	public PartDamage(float p) {
		this.part = p;
	}

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = 0;

		if (canHit(attacker, attack, defender, arena)) {
			int full = defender.getHealthMax();

			damage = Math.round((float) full * part);
			damage = (damage > defender.getHealth()) ? defender.getHealth() : damage;
			
			int pain = defender.damage(damage, attack.getCategory(), arena);

			nextStep(attacker, pain, attack, defender, arena);
		}

		return damage;
	}

	public Damage copy() {
		return new PartDamage(part);
	}

	public String description() {
		return "Inflicts damage equivalent to " + (part * 100) + "% of the opponents max health. ";
	}

	public String code() {
		return "healthReduce " + part;
	}

}
