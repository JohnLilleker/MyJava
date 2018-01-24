package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RevengeDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new RevengeDamage();
	}

	public String description() {
		return "Deals double damage if the user takes damage first. ";
	}

	public String code() {
		return "Revenge";
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		int mult = 1;
		if (attacker.getPhysicalDamage() > 0 || attacker.getSpecialDamage() > 0)
			mult = 2;
		return attack.getPow()* mult;
	}
}
