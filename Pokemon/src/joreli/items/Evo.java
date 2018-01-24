package joreli.items;

import joreli.Item;
import joreli.Location;
import joreli.Pokemon;
import joreli.Type;

public class Evo extends Item {

	private Type type;
	private String kind;
	
	public Evo(String n, Type t) {
		super(n + " Evo");
		this.type = t;
		this.kind = n;
	}

	public Category getCategory() {
		return Category.EVO;
	}

	public Item copy() {
		return new Evo(kind, type);
	}

	public boolean isConsumable() {
		return true;
	}

	public Type getTypeMatch() { return type; }
	
	public boolean use(Pokemon p, Location place) {
		return p.typeMatch(type);
	}

	public String description() {
		return String.format("Causes instant evolution in %s-type pokemon", type);
	}

}
