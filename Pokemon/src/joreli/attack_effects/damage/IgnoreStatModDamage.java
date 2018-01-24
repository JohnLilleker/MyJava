package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class IgnoreStatModDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new IgnoreStatModDamage();
	}

	public String description() {
		return "Ignores target stat changes. ";
	}

	protected boolean miss(Pokemon attacker, Attack attack, Pokemon defender) {

		float acc = (float) attack.getAccuracy() / 100;
		float miss = attacker.getAccuracy() / 100;
		acc = acc * miss;
		return Math.random() > acc;
	}

	protected float[] stats(Pokemon attacker, boolean physical, Pokemon defender, Weather w) {
		float att = attacker.getAtk();
		float def = defender.getDefMax();

		if (!physical) {
			att = attacker.getSpAtk();
			def = defender.getSpDefMax();
		}
		float[] r = { att, def };
		return r;
	}

	public String code() {
		return "ignoreMod";
	}

}
