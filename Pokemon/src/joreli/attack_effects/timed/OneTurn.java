package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class OneTurn extends Timed {

	/**
	 * Uses the attack immediately
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		attack.getDamageEffect().harm(attacker, attack, defender, arena);
		attack.use();
	}

	public Timed copy() {
		return new OneTurn();
	}

	public String description() {
		return "";
	}

	public String code() {
		return "";
	}

}
