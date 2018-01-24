package joreli.items;

import joreli.Battle;
import joreli.Condition;
import joreli.Item;
import joreli.Location;
import joreli.Pokemon;

public class ConditionBerry extends Berry {

	private Condition heal = Condition.NONE;
	private boolean all = false;
	
	public ConditionBerry(String n, String c) {
		super(n, Taste.NONE);
		if (c.equals("All")) {
			all = true;
		}
		else {
			heal = Condition.valueOf(c);
		}
	}

	public boolean eat(Pokemon p, Battle arena) {
		
		if (p.isFine())
			return false;
		
		if (all) {
			boolean healed = false;
			String gender = "it's";
			if (p.female())
				gender = "her";
			if (p.female())
				gender = "his";
			arena.log( p.getName() + " ate " + gender + " " + this.getName());
			
			if (p.isBurned()) {
				p.giveFix(Condition.Fix.COOL, arena);
				healed = true;
			}
			if (p.isConfused()) {
				p.giveFix(Condition.Fix.SNAP_OUT, arena);
				healed = true;
			}
			if (p.isFrozen()) {
				p.giveFix(Condition.Fix.THAW, arena);
				healed = true;
			}
			if (p.isParalysed()) {
				p.giveFix(Condition.Fix.FLEX, arena);
				healed = true;
			}
			if (p.isAsleep()) {
				p.giveFix(Condition.Fix.WAKE, arena);
				healed = true;
			}
			if (p.isPoisoned() || p.isBadlyPoisoned()) {
				p.giveFix(Condition.Fix.ANTIDOTE, arena);
				healed = true;
			}
			p.makeFriends((short) 5);
			return healed;
		}
		
		if (p.hasCondition(heal) || (heal == Condition.POISON && p.isBadlyPoisoned())) {
			String gender = "it's";
			if (p.female())
				gender = "her";
			if (p.female())
				gender = "his";
			arena.log( p.getName() + " ate " + gender + " " + this.getName());
			p.giveFix(heal.opposite(), arena);
			p.makeFriends((short) 5);
			return true;
		}
		return false;
	}

	public Item copy() {
		String c = heal.toString();
		if (all) c = "All";
		return new ConditionBerry(berry_name, c);
	}

	public boolean use(Pokemon p, Location place) {
		Battle b = new Battle();
		b.setLogMethod(Battle.PRINT);
		return eat(p, b);
	}

	public String description() {
		if (all) return "Heals the user of any status conditions";
		switch(heal) {
		case BURN:
			return "Heals a burned pokemon";
		case CONFUSE:
			return "Snaps a pokemon out of confusion";
		case FREEZE:
			return "Thaws a frozen pokemon";
		case PARALYSE:
			return "Heals a paralysed pokemon";
		case POISON:
			return "Heals a pokemon of poisoning";
		case SLEEP:
			return "Wakes a sleeping pokemon";
		default:
			return "Cures the user of a status problem";
		}
	}

}
