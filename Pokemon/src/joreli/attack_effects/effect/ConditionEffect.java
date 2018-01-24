package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;

public class ConditionEffect extends Effect {

	private Condition[] conds;
	private int likelihood;
	private Target target;

	/**
	 * Causes a
	 * 
	 * @param c
	 *            condition with probability
	 * @param l
	 *            to the target,
	 * @param t
	 *            {1 => defender, 0 => user}
	 */
	public ConditionEffect(Condition c, int l, Target t) {
		this(new Condition[] { c }, l, t);
	}

	/**
	 * Causes a condition selected from
	 * 
	 * @param c
	 *            an array of conditions, with probability
	 * @param l
	 *            to the target,
	 * @param t
	 *            Target enum describing who is effected
	 */
	public ConditionEffect(Condition[] c, int l, Target t) {
		conds = c;
		likelihood = l;
		target = t;
	}

	/**
	 * Could cause multiple effects based on a String delimited by ','
	 * 
	 * @param cs
	 *            the conditions
	 * @param l
	 *            the likelihood %age
	 * @param t
	 *            the target {1 => defender, 0 => user}
	 */
	public ConditionEffect(String cs, int l, Target t) {
		String[] parse = cs.split(",");
		conds = new Condition[parse.length];
		for (int i = 0; i < parse.length; i++) {
			conds[i] = Condition.valueOf(parse[i]);
		}
		likelihood = l;
		target = t;
	}

	private void giveCondition(Pokemon p, Pokemon attacker, Attack attack, int damage, Battle arena) {
		
		if (Math.random() * 100 > likelihood) {
			return;
		}

		// pick a random Condition from conds
		int ind = (int) Math.round(Math.random() * (conds.length - 1));
		Condition cond = conds[ind];

		switch(cond) {
		case BAD_POISON:
		case BURN:
		case CONFUSE:
		case FREEZE:
		case PARALYSE:
		case POISON:
		case SLEEP:
			Battle.Position pos = arena.positionOfPokemon(p);
			if (arena.defenceSet(pos, Battle.Defence.SAFEGUARD)) {
				arena.log("But it failed!");
				return;
			}
			break;
		default:
			break;
		
		}
		
		boolean worked;
		if (cond == Condition.INFATUATION) 
			worked = p.giveCondition(cond, attacker.getName(), arena);
		else 
			worked = p.giveCondition(cond, attack.getName(), arena);
		
		if (!worked && damage < 1) {
			arena.log("But it failed!");
		}
	}

	/**
	 * Causes a status condition
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {

		Pokemon p = (target == Target.TARGET) ? defender : attacker;
		if (!p.fainted())
			giveCondition(p, attacker, attack, damage, arena);
	}

	public Effect copy() {
		return new ConditionEffect(conds, likelihood, target);
	}

	public String description() {
		String type = "";
		boolean may = likelihood < 100;
		String who = (target == Target.TARGET) ? "target" : "user";

		if (conds.length > 1) {
			for (int i = 0; i < conds.length; i++) {
				switch (conds[i]) {
				case POISON:
					type = "Poison the " + who;
					break;
				case BAD_POISON:
					type += "Badly Poison the " + who;
					break;
				case BURN:
					type += "Burn the " + who;
					break;
				case FREEZE:
					type += "Freeze the " + who;
					break;
				case PARALYSE:
					type += "Paralyse the " + who;
					break;
				case CONFUSE:
					type += "Confuse the " + who;
					break;
				case FLINCH:
					type += "make the target Flinch";
				default:
					break;
				}
				if (i < conds.length - 2) {
					type += ", ";
				} else if (i == conds.length - 2) {
					type += " or ";
				}
			}
			return "May " + type;
		} else {
			switch (conds[0]) {
			case SLEEP:
				if (may)
					return "May put the " + who + " to sleep. ";
				else
					return "Puts the " + who + " to sleep. ";

			case POISON:
				type = "Poison";
				break;
			case BAD_POISON:
				type = "Badly Poison";
				break;
			case BURN:
				type = "Burn";
				break;
			case FREEZE:
				type = "Freeze";
				break;
			case PARALYSE:
				type = "Paralyse";
				break;
			case CONFUSE:
				type = "Confuse";
				break;
			case FLINCH:
				if (may)
					return "May make the " + who + " Flinch. ";
				else
					return "Makes the " + who + " Flinch. ";
			case INFATUATION:
				if (may)
					return "May make the target fall in love with the user. ";
				else
					return "Makes the target fall in love with the user. ";
			case ENTRAP:
				return "Traps the target. ";
			case CANT_ESCAPE:
				return String.format("Prevents the %s from being switched out. ", who);
			case SEEDED:
				return "Seeds the target, absorbing some health every turn. ";
			default:
				break;
			}
		}

		if (may) {
			type = "May " + type;
		} else {
			type = type + "s";
		}

		return type + " the " + who + ". ";
	}

	public String code() {
		String cs = "";
		for (int i = 0; i < this.conds.length; i++) {
			cs += this.conds[i];
			if (i < conds.length-1) cs += ",";
		}
		return "condition " + cs + " " + target + " " + likelihood;
	}

}
