package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Type;
import joreli.Weather;

public class StatDamage extends Damage {

	private Pokemon.Stats atk;
	private Pokemon.Stats def;
	
	public StatDamage(Pokemon.Stats a, Pokemon.Stats d) {
		atk = a;
		def = d;
	}
	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new StatDamage(atk, def);
	}

	public String description() {
		String attack = decode(atk);
		String defence = decode(def);
		return String.format("Does damage based on the user's %s and the defender's %s. ", attack, defence);
	}

	protected float[] stats(Pokemon attacker, boolean physical, Pokemon defender, Weather w) {
		
		float attack = getStat(atk, attacker);
		float defence = getStat(def, defender);
		if (def == Pokemon.Stats.SPDEF) {
			// increase sp.def of ROCK if SANDSTORM
			if (w == Weather.SANDSTORM && defender.typeMatch(Type.ROCK))
				defence *= 1.5f;
		}
		float[] r = { attack, defence };
		return r;
	}
	
	private String decode(Pokemon.Stats s) {
		String eff_stat = "STAT";
		
		switch(s) {
		case ATK:
			eff_stat = "physical attack";
			break;
		case DEF:
			eff_stat = "physical defence";
			break;
		case SPATK:
			eff_stat = "special attack";
			break;
		case SPDEF:
			eff_stat = "special defence";
			break;
		default:
			break;
		}

		return eff_stat;
	}

	private int getStat(Pokemon.Stats s, Pokemon p) {
		int stat = 10;
		
		switch(s) {
		case ATK:
			stat = p.getAtk();
			break;
		case DEF:
			stat = p.getDef();
			break;
		case SPATK:
			stat = p.getSpAtk();
			break;
		case SPDEF:
			stat = p.getSpDef();
			break;
		default:
			break;
		}
		return stat;
	}
	
	public String code() {
		return String.format("statDamage %s %s", atk, def);
	}

}
