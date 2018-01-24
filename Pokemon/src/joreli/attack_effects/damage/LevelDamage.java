package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class LevelDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = 0;
		if (canHit(attacker, attack, defender, arena)) {
			damage = (attacker.getLevel() > defender.getHealth()) ? defender.getHealth() : attacker.getLevel();
			int pain = defender.damage(damage, attack.getCategory(), arena);
			nextStep(attacker, pain, attack, defender, arena);
		}
		return damage;
	}

	public Damage copy() {
		return new LevelDamage();
	}

	public String description() {
		return "Does damage equal to the user's level. ";
	}

	public String code() {
		return "level";
	}

}
