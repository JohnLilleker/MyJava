package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class AbsorbEffect extends Effect {

	private float amount;

	/**
	 * 
	 * @param d
	 *            the percentage health recovered
	 */
	public AbsorbEffect(float d) {
		this.amount = d;
	}

	/**
	 * Recovers the attackers health
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (damage > 0) {
			attacker.heal(Math.round(amount * damage));
			arena.log(attacker.getName() + " absorbed " + defender.getName() + "'s health");
		}
	}

	public Effect copy() {
		return new AbsorbEffect(amount);
	}

	public String description() {
		return "The user recovers " + amount*100 + "% of the damage given. ";
	}

	public String code() {
		return "absorb " + amount;
	}

}
