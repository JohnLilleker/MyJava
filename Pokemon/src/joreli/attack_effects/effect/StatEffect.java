package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class StatEffect extends Effect {
	private int effect;
	private Pokemon.Stats[] stats;
	private Target target;
	private int likelihood;

	/**
	 * 
	 * @param s
	 *            the stats to change
	 * @param ef
	 *            the magnitude of change
	 * @param t
	 *            enum describing who is effected
	 * @param l
	 *            how likely it is
	 */
	public StatEffect(Pokemon.Stats[] s, int ef, Target t, int l) {
		this.stats = s;
		this.effect = ef;
		this.target = t;
		this.likelihood = l;
	}

	/**
	 * 
	 * @param s
	 *            the stats to change, a string delimited by "," with elements
	 *            {atk, def, spatk, spdef, spd, acc, eva, ctl}
	 * @param ef
	 *            the magnitude of change
	 * @param t
	 *            enum describing who is effected
	 * @param l
	 *            how likely it is
	 */
	public StatEffect(String s, int ef, Target t, int l) {
		String[] ss = s.split(",");
		Pokemon.Stats[] sts = new Pokemon.Stats[ss.length];
		for (int i = 0; i < ss.length; i++) {
			sts[i] = Pokemon.Stats.valueOf(ss[i]);
		}
		this.stats = sts;
		this.effect = ef;
		this.target = t;
		this.likelihood = l;
	}

	private String decode(Pokemon.Stats s) {
		String eff_stat = "STAT";
		
		switch(s) {
		case ACC:
			eff_stat = "accuracy";
			break;
		case ATK:
			eff_stat = "attack";
			break;
		case DEF:
			eff_stat = "defence";
			break;
		case EVA:
			eff_stat = "evasiveness";
			break;
		case SPATK:
			eff_stat = "special attack";
			break;
		case SPD:
			eff_stat = "speed";
			break;
		case SPDEF:
			eff_stat = "special defence";
			break;
		default:
			break;
		}

		return eff_stat;
	}

	private void changeStat(Pokemon subject, int damage, Battle arena) {

		if (Math.random() * 100 > likelihood) {
			if (damage < 1)
				arena.log("But nothing happened");
			return;
		}

		if (stats[0] == Pokemon.Stats.CTL) {
			if (subject.statChange(stats[0], 1)) {
				arena.log(subject.getName() + " is ready");
			} else {
				arena.log("But it failed");
			}
			return;
		}
		
		if (effect < 0) {
			Battle.Position pos = arena.positionOfPokemon(subject);
			if (arena.defenceSet(pos, Battle.Defence.MIST)) {
				arena.log("But it failed");
				return;
			}
		}

		for (Pokemon.Stats stat : stats) {
			String d_stat = decode(stat);
			if (!subject.statChange(stat, effect)) {
				// complain about stat being too high
				String dir = (effect > 0) ? "higher" : "lower";
				arena.log(subject.getName() + "'s " + d_stat + " can't get any " + dir + "!");
				
			} else {

				String mag = "";
				if (Math.abs(effect) > 1) {
					mag = " sharply";
				}

				String change = " rose!";
				if (effect < 0) {
					change = " fell!";
				}

				arena.log(subject.getName() + "'s " + d_stat + change + mag);
			}
		}
	}

	/**
	 * Changes the stat of a pokemon
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		Pokemon s = (target == Target.SELF) ? attacker : defender;
		if (!s.fainted())
			changeStat(s, damage, arena);
	}

	public String description() {

		if (stats[0] == Pokemon.Stats.CTL) {
			return "Increases the user's critical hit ratio. ";
		}

		String chain = "";
		if (this.likelihood < 100)
			chain += "May " + ((Math.abs(effect) > 1) ? "sharply " : "") + ((effect > 0) ? "increase " : "decrease ");
		else
			chain = (effect > 0) ? ((Math.abs(effect) > 1) ? "Sharply i" : "I") + "ncreases "
					: ((Math.abs(effect) > 1) ? "Sharply d" : "D") + "ecreases ";

		chain += (target == Target.SELF) ? "the user's " : "the opponent's ";
		int len = stats.length;
		for (int i = 0; i < len; i++) {
			chain += decode(stats[i]);
			if (i < len - 2)
				chain += ", ";
			else if (i == len - 2)
				chain += " and ";
		}

		return chain + ". ";
	}

	public Effect copy() {
		return new StatEffect(stats, effect, target, likelihood);
	}

	public String code() {
		String ss = "";
		for (int i = 0; i < this.stats.length; i++) {
			ss += this.stats[i];
			if (i < stats.length-1) ss += ",";
		}
		return "stat " + ss + " " + effect + " " + target + " " + likelihood;
	}
}
