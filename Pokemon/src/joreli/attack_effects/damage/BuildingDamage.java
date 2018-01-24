package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class BuildingDamage extends Damage {
	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int worked = defaultBehaviour(attacker, attack, defender, arena);
		
		if (worked < 1) {
			attacker.stopCountingConsecutiveUses();
		}
		return worked;
	}

	public Damage copy() {
		return new BuildingDamage();
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		int usage = attacker.consecutiveTurnsUsed(attack.getName());
		int multiplier = (int) Math.pow(2, (usage-1));
		if (multiplier > 16) multiplier = 16;
		int pow = attack.getPow();
		return pow * multiplier;
	}

	public String description() {
		return "Doubles in power with consectutive uses. ";
	}

	public String code() {
		return "build";
	}

}
