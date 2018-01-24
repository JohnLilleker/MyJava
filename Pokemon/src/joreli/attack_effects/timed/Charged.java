package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class Charged extends Timed {

	/**
	 * Waits a turn before unleashing hell (since most of these attacks are
	 * pretty strong)
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		if (!attacker.isCharging()) {
			attacker.setCharging(attack);
			arena.log(attacker.getName() + " is charging");
		} else {
			attacker.setCharging(null);
			attack.getDamageEffect().harm(attacker, attack, defender, arena);
			attack.use();
		}
	}

	@Override
	public Timed copy() {
		return new Charged();
	}

	public String description() {
		return "Charges on on turn, attacks on the next. ";
	}

	public String code() {
		return "charged";
	}

}
