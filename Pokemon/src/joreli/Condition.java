package joreli;

/**
 * An enum Describing most conditions that can happen to a pokemon. Not just
 * Burning and poisoning, but also flinching and falling in love *awwww*
 * 
 * @author JB227
 *
 */
public enum Condition {
	NONE, SLEEP, POISON, BURN, FREEZE, PARALYSE, CONFUSE, BAD_POISON, FLINCH, INFATUATION, ENTRAP, CANT_ESCAPE, SEEDED;

	// heal conditions?
	/**
	 * FULL->Everything. WAKE->SLEEP. ANITIDOTE->(BAD_)*POISON. COOL->BURN.
	 * THAW->FREEZE. FLEX->PARALYSE. SNAP_OUT->CONFUSE. FALL_OUT->INFATUATION.
	 * 
	 * @author JB227
	 *
	 */
	public enum Fix {
		NONE, FULL, WAKE, ANTIDOTE, COOL, THAW, FLEX, SNAP_OUT, FALL_OUT, BREAK_OUT, ESCAPE;
		
		public Condition[] opposite() {
			switch(this) {
			case ANTIDOTE:
				return new Condition[]{Condition.POISON, Condition.BAD_POISON};
			case COOL:
				return new Condition[]{Condition.BURN};
			case ESCAPE :
				return new Condition[]{Condition.CANT_ESCAPE};
			case FALL_OUT:
				return new Condition[]{Condition.INFATUATION};
			case FLEX:
				return new Condition[]{Condition.PARALYSE};
			case FULL:
				return Condition.values();
			case NONE:
				return new Condition[]{};
			case SNAP_OUT:
				return new Condition[]{Condition.CONFUSE};
			case THAW:
				return new Condition[]{Condition.FREEZE};
			case WAKE:
				return new Condition[]{Condition.SLEEP};
			case BREAK_OUT:
				return new Condition[]{Condition.ENTRAP, Condition.SEEDED};
			default:
				break;
			
			}
			return new Condition[]{};
		}
	}
	
	public Fix opposite() {
		switch(this) {
		case BAD_POISON:
			return Fix.ANTIDOTE;
		case BURN:
			return Fix.COOL;
		case CANT_ESCAPE:
			return Fix.ESCAPE;
		case CONFUSE:
			return Fix.SNAP_OUT;
		case ENTRAP:
			return Fix.BREAK_OUT;
		case FLINCH:
			return Fix.NONE;
		case FREEZE:
			return Fix.THAW;
		case INFATUATION:
			return Fix.FALL_OUT;
		case NONE:
			return Fix.NONE;
		case PARALYSE:
			return Fix.FLEX;
		case POISON:
			return Fix.ANTIDOTE;
		case SEEDED:
			return Fix.BREAK_OUT;
		case SLEEP:
			return Fix.WAKE;
		}
		return Fix.NONE;
	}

	public String shorthand() {
		String cond;
		switch (this) {
		case BURN:
			cond = "BRN ";
			break;
		case FREEZE:
			cond = "FRZ ";
			break;
		case PARALYSE:
			cond = "PRZ ";
			break;
		case POISON:
		case BAD_POISON:
			cond = "PSN ";
			break;
		case SLEEP:
			cond = "SLP ";
			break;
		default:
			cond = "";
			break;
		}
		return cond;
	}
}
