package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;

public class ConditionDamage extends Damage {

	private Condition[] conds;
	private final Condition[] defaults = { Condition.BAD_POISON, Condition.BURN, Condition.CONFUSE, Condition.FREEZE,
			Condition.PARALYSE, Condition.POISON, Condition.SLEEP };
	private float multipiler;

	public ConditionDamage(String c, float m) {
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
		this.multipiler = m;
	}

	public ConditionDamage(Condition[] cs, float m) {
		this.conds = cs;
		this.multipiler = m;
	}

	private boolean any() {
		for (int i = 0; i < conds.length && i < defaults.length; i++) {
			if (conds[i] != defaults[i])
				return false;
		}
		return true;
	}

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		float mult = 1;
		for (Condition c : this.conds) {
			if (defender.hasCondition(c))
				mult = multipiler;
		}
		
		return (int) (attack.getPow() * mult);
	}

	public Damage copy() {
		return new ConditionDamage(conds, multipiler);
	}

	public String description() {
		if (any()) {
			return "Has " + multipiler + "x power against targets afflicted a status condition. ";
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
			return "has " + multipiler + "x power against targets that are " + conditions + ". ";
		}
	}

	public String code() {
		String cs = "";
		for (int i = 0; i < this.conds.length; i++) {
			cs += this.conds[i];
			if (i < conds.length-1) cs += ",";
		}
		return "condDamage " + cs + " " + multipiler;
	}

}
