package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class EndureEffect extends Effect {

	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (attacker.endure()) {
			arena.log(attacker.getName() + " is ready prepared for attacks");
		} else {
			arena.log("But it failed!");
		}
	}

	public Effect copy() {
		return new EndureEffect();
	}

	public String description() {
		return "Enables the user to survive any attacks this turn with at least 1hp. ";
	}

	public String code() {
		return "endure";
	}

}
