package joreli.items;

import joreli.Battle;
import joreli.Condition;
import joreli.Item;
import joreli.Location;
import joreli.Pokemon;

public class HealthBerry extends Berry {

	public enum Method {HP, PART}
	
	private float amount;
	private Method method;
	
	public HealthBerry(String n, Taste t, float a, Method m) {
		super(n, t);
		this.amount = a;
		this.method = m;
	}

	public Item copy() {
		return new HealthBerry(berry_name, taste, amount, method);
	}

	public boolean use(Pokemon p, Location place) {
		if (p.getHealth() == p.getHealthMax() || p.fainted()) return false;

		Battle b = new Battle();
		b.setLogMethod(Battle.PRINT);
		return eat(p, b);
	}

	public boolean eat(Pokemon p, Battle arena) {
		if (p.getHealth() / (float) p.getHealthMax() > 0.5) return false;
		if (p.fainted()) return false;

		int heal = 0;
		switch(method) {
		case HP:
			heal = (int) amount;
			break;
		case PART:
			heal = (int) (p.getHealthMax() * amount);
			break;		
		}
		p.heal(heal);
		String gender = "it's";
		if (p.female())
			gender = "her";
		if (p.female())
			gender = "his";
		arena.log( p.getName() + " ate " + gender + " " + this.getName());
		System.out.printf(p.getName() + " recovered health");
		
		int likey = this.likesTaste(p);
		if (likey == -1) {
			p.giveCondition(Condition.CONFUSE, "Bad Berry", arena);
			arena.log(p.getName() + " was confused!");
			
			p.makeFriends((short) -5);
		}
		else if (likey == 0) {
			p.makeFriends((short) 5);
		}
		else {
			p.makeFriends((short) 15);
		}
		
		return true;
	}

	public String description() {
		String how_much = "";
		String taste_info = "";
		switch(method) {
		case HP:
			how_much = (int) amount + "HP";
			break;
		case PART:
			how_much = (amount * 100) + "% max hp";
			break;		
		}
		if (this.taste != Taste.NONE) {
			taste_info = String.format(", but it confuses if the user doesn't like %s flavours", taste);
		}
		return String.format("Heals the user by %s in a pinch%s", how_much, taste_info);
	}

}
