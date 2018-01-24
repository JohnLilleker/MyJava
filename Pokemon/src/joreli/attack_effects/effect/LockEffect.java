package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class LockEffect extends Effect {

	@Override
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		attacker.aim();
		arena.log(attacker.getName() + " took aim");
	}

	@Override
	public Effect copy() {
		return new LockEffect();
	}

	public String description() {
		return "The next attack cannot miss. ";
	}

	public String code() {
		return "aim";
	}

}
