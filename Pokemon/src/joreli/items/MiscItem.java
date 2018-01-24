package joreli.items;

import joreli.Item;
import joreli.Location;
import joreli.Pokemon;

public class MiscItem extends Item {

	String desc; 
	
	public MiscItem(String n, String d) {
		super(n);
		desc = d;
	}

	public Category getCategory() {
		return Category.MISC;
	}

	public Item copy() {
		return new MiscItem(name, desc);
	}

	public boolean isConsumable() {
		return false;
	}

	public boolean use(Pokemon p, Location place) {
		return false;
	}

	public String description() {
		return desc;
	}

}
