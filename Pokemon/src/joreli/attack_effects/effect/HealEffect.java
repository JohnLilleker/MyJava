package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;

public class HealEffect extends Effect {

	private float recovered;
	private Condition.Fix fix;
	private Target who;

	public HealEffect(float h, Condition.Fix f, Target t) {
		this.recovered = h;
		this.fix = f;
		this.who = t;
	}

	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		
		Pokemon subject = (who == Effect.Target.TARGET) ? defender : attacker;
		
		if (subject.fainted())
			return;
		
		if ((subject.getHealth() == subject.getHealthMax() && recovered > 0) &&
				(fix != Condition.Fix.NONE && subject.isFine())) {
			arena.log("But nothing happened");
			return;
		}
		boolean lowHealth = (subject.getHealth() < subject.getHealthMax()) && recovered > 0;
		int amount = (int) ((float) subject.getHealthMax() * recovered);
		subject.heal(amount);
		if (lowHealth) {
			arena.log(subject.getName() + " recovered health");
		}
		
		subject.giveFix(fix, arena);
	}

	public Effect copy() {
		return new HealEffect(recovered, fix, who);
	}

	public String description() {
		String desc = "";
		String target = (who == Effect.Target.TARGET) ? "target": "user";
		if (recovered > 0) {
			String start = (recovered < 1) ? "R" : "Fully r";
			return start + "ecovers the " + target + "'s health. ";
		}
		if (fix != Condition.Fix.NONE) {
			String premise = "Cures the " + target + " of ";
			String cond = "nothing";
			switch(fix) {
			case ANTIDOTE:
				cond = "Poisoning";
				break;
			case COOL:
				cond = "Burns";
				break;
			case FALL_OUT:
				cond = "Infatuation";
				break;
			case FLEX:
				cond = "Paralysis";
				break;
			case FULL:
				cond = "all status conditions";
				break;
			case SNAP_OUT:
				cond = "Confusion";
				break;
			case THAW:
				premise = "Thaws the " + target;
				cond = "";
				break;
			case WAKE:
				premise = "Wakes the " + target + " up";
				cond = "";
				break;
			case BREAK_OUT:
				cond = "Trapping moves";
				break;
			default:
				break;
			}
			desc += premise + cond + ". ";
		}
		return desc;
	}

	public String code() {
		return "heal " + recovered + " " + fix + " " + who;
	}

}
