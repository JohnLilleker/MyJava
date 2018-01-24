package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;

public class HasCondition extends Timed {

	private Condition[] conds;
	private final Condition[] defaults = { Condition.BAD_POISON, Condition.BURN, Condition.CONFUSE, Condition.FREEZE,
			Condition.PARALYSE, Condition.POISON, Condition.SLEEP };

	public HasCondition(String c) {
		if (c.equals("any")) {
			this.conds = defaults;
		} else {
			String[] cs = c.split(",");
			Condition[] cons = new Condition[cs.length];
			for (int i = 0; i < cs.length; i++) {
				cons[i] = Condition.valueOf(cs[i]);
			}
			this.conds = cons;
		}
	}

	public HasCondition(Condition[] cs) {
		this.conds = cs;
	}

	private boolean any() {
		for (int i = 0; i < conds.length && i < defaults.length; i++) {
			if (conds[i] != defaults[i])
				return false;
		}
		return true;
	}

	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		boolean canHit = false;
		for (Condition c : this.conds) {
			if (defender.hasCondition(c)) {
				attack.getDamageEffect().harm(attacker, attack, defender, arena);
				canHit = true;
				break;
			}
		}
		if (!canHit)
			arena.log("But it failed!");
			
		attack.use();
	}

	public Timed copy() {
		return new HasCondition(conds);
	}

	public String description() {
		if (any()) {
			return "Only effects targets that have a status condition. ";
		} else {
			String conditions = "";
			for (int i = 0; i < conds.length; i++) {
				switch (conds[i]) {
				case BAD_POISON:
					conditions += "Badly Poisoned";
					break;
				case BURN:
					conditions += "Burned";
					break;
				case CONFUSE:
					conditions += "Confused";
					break;
				case ENTRAP:
					conditions += "Trapped";
					break;
				case FREEZE:
					conditions += "Frozen";
					break;
				case INFATUATION:
					conditions += "In love";
					break;
				case NONE:
					conditions += "Unafflicted by Status Conditions";
					break;
				case PARALYSE:
					conditions += "Paralysed";
					break;
				case POISON:
					conditions += "Poisoned";
					break;
				case SLEEP:
					conditions += "Asleep";
					break;
				default:
					continue;
				}
				if (i < conds.length - 2) {
					conditions += ", ";
				} else if (i == conds.length - 2) {
					conditions += " or ";
				}
			}
			return "Only effects targets that are " + conditions + ". ";
		}
	}

	public String code() {
		String cs = "";
		for (int i = 0; i < this.conds.length; i++) {
			cs += this.conds[i];
			if (i < conds.length-1) cs += ",";
		}
		return "hasCond " + cs;
	}

}
