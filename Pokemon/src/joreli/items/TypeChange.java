package joreli.items;

import joreli.Item;
import joreli.Location;
import joreli.Pokemon;
import joreli.Type;

public class TypeChange extends Item {

	public enum Device {PLATE, DRIVE}
	
	private String cat;
	private Device dev;
	private Type type;
	
	public TypeChange(String n, Device d, Type t) {
		super(n + ((d == Device.PLATE) ? " Plate" : " Drive"));
		cat = n;
		dev = d;
		type = t;
	}

	public Category getCategory() {
		return Category.TYPE;
	}

	public boolean isItem(Device d) {
		return dev == d;
	}
	
	public Type getType() {
		return type;
	}
	
	public Item copy() {
		return new TypeChange(cat, dev, type);
	}

	public boolean isConsumable() {
		return false;
	}

	public boolean use(Pokemon p, Location place) {
		return false;
	}

	public String description() {
		String s = "";
		if (dev == Device.PLATE) {
			s = String.format("Boosts %s attacks by 20%. Also changes the type of Judgement", type);
		}
		else {
			s = String.format("Changes the type of Techno Blast");
		}
		return s;
	}

}
