package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class HighHPDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new HighHPDamage();
	}

	public String description() {
		return "Becomes less powerful the less health the user has. ";
	}

	public String code() {
		return "fullHP";
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		return 150 * (attacker.getHealth() / attacker.getHealthMax());
	}

}
