package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class NoKillDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = this.damage(attacker, attack, defender, arena);
		int pain = damage;
		if (damage > -1) {
			damage = (damage >= defender.getHealth()) ? defender.getHealth()-1 : damage;
			pain = defender.damage(damage, attack.getCategory(), arena);
		}
		nextStep(attacker, pain, attack, defender, arena);
		return damage;
	}

	public Damage copy() {
		return new NoKillDamage();
	}

	public String description() {
		return "Leaves the opponent with 1hp. ";
	}

	public String code() {
		return "nokill";
	}

}
