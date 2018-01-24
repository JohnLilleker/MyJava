package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RestEffect extends Effect {

	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		attacker.notAttack();
	}

	public Effect copy() {
		return new RestEffect();
	}

	public String description() {
		return "The user cannot attack next turn. ";
	}

	public String code() {
		return "rest";
	}

}
