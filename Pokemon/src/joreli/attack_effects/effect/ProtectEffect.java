package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class ProtectEffect extends Effect {

	/**
	 * Protects the attacker from attacks this turn, though it becomes less
	 * successful the more you use it
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (attacker.protect()) {
			arena.log(attacker.getName() + " is ready to protect itself");
		} else {
			arena.log("But it failed!");
		}
	}

	public Effect copy() {
		return new ProtectEffect();
	}

	public String description() {
		return "Protects the user from Attacks this turn. ";
	}

	public String code() {
		return "protect";
	}
}
