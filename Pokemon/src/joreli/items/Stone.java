package joreli.items;

import joreli.Item;
import joreli.Location;
import joreli.Pokemon;

public class Stone extends Item {
	
	String type;
	
	public Stone(String n) {
		super(n + " Stone");
		type = n;
	}
	
	public Category getCategory() {
		return Category.STONE;
	}

	public Item copy() {
		return new Stone(type);
	}

	public boolean isConsumable() {
		return true;
	}

	public String description() {
		return "This may allow a Pokemon to evolve";
	}

	public boolean use(Pokemon p, Location place) {
		String name = p.checkEvolve(this);
		if (!name.equals("none")) return true;
		return false;
	}

}
