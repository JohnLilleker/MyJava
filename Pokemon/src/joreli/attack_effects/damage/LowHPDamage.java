package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class LowHPDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new LowHPDamage();
	}

	public String description() {
		return "Becomes more powerful the less health the user has. ";
	}

	public String code() {
		return "lowHP";
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		
		int pow;
		
		int prop = (int) (64 * ((float)attacker.getHealth() / attacker.getHealthMax()));
		
		if (prop < 2) pow = 200;
		else if (prop < 6) pow = 150;
		else if (prop < 13) pow = 100;
		else if (prop < 22) pow = 80;
		else if (prop < 43) pow = 40;
		else pow = 20;
		
		return pow;
	}

}
