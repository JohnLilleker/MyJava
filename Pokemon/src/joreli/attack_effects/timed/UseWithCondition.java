package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;

public class UseWithCondition extends Timed {

	private Condition noStop;
	
	public UseWithCondition(Condition c) {
		noStop = c;
	}
	
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		attack.getDamageEffect().harm(attacker, attack, defender, arena);
		attack.use();
	}

	public Timed copy() {
		return new UseWithCondition(noStop);
	}

	public String description() {
		String cond = "bored";
		switch(noStop) {
		case FREEZE:
			cond = "frozen";
			break;
		case INFATUATION:
			cond = "infatuated";
			break;
		case PARALYSE:
			cond = "paralysed";
			break;
		case SLEEP:
			cond = "asleep";
			break;
		default:
			break;
		}
		return String.format("Can be used even if the user is %s. ", cond);
	}

	public String code() {
		return String.format("useWith %s", noStop);
	}
	
	public boolean canUseWithCondition(Condition c) {
		if (c == noStop) return true;
		return super.canUseWithCondition(c);
	}

}
