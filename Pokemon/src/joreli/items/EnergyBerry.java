package joreli.items;

import joreli.Attack;
import joreli.Battle;
import joreli.Item;
import joreli.Location;
import joreli.Pokemon;

public class EnergyBerry extends Berry {

	private int increase; 
	
	public EnergyBerry(String n, int inc) {
		super(n, Taste.NONE);
		this.increase = inc;
	}

	public boolean eat(Pokemon p, Battle arena) {
		for (int i = 0; i < Pokemon.NO_OF_ATTACKS; i++) {
			Attack a = p.getAttack(i);
			if (a == null) continue;
			if (a.exhausted()) {
				a.recharge(increase);
				String gender = "it's";
				if (p.female()) 
					gender = "her";
				if (p.female())
					gender = "his";
				arena.log( p.getName() + " ate " + gender + " " + this.getName());
				arena.log(p.getName() + " recovered energy");
				return true;
			}
		}
		return false;
	}

	public Item copy() {
		return new EnergyBerry(berry_name, increase);
	}

	public boolean use(Pokemon p, Location place) {
		Attack weak = null;
		for (int i = 0; i < Pokemon.NO_OF_ATTACKS; i++) {
			Attack a = p.getAttack(i);
			if (a == null) continue;
			if (weak == null) {
				weak = a;
				continue;
			}
			if (a.getUses() < weak.getUses()) {
				weak = a;
			}
		}
		if (weak.getUses() == weak.getMaxPP())
			return false;
		weak.recharge(increase);
		return true;
	}

	public String description() {
		return String.format("Restores the pp of an exhausted move by %d", increase);
	}

}
