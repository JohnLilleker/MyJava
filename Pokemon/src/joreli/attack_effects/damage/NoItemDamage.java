package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class NoItemDamage extends Damage {


	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new NoItemDamage();
	}

	public String description() {
		return "Deals double damage if the user isn't holding an item. ";
	}

	public String code() {
		return "noItem";
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		int mult = 1;
		if (!attacker.isHoldingItem())
			mult = 2;
		return attack.getPow()* mult;
	}
}
