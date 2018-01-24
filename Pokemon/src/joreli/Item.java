package joreli;

/**
 * A basic wrapper class for Items held by pokemon
 * @author JB227
 */
public abstract class Item {

	/**
	 * A describer of each subclass <br><br>
	 * <i><b>BERRY</b></i> - one use, used predominately for healing<br>
	 * <i><b>BATTLE</b></i> - used to gain some advantage during battle, usage depends on Item<br>
	 * <i><b>EVO</b></i> - may cause instant evolution<br>
	 * <i><b>STONE</b></i> - used for evolving<br>
	 * <i><b>TRAINER</b></i> - Pokemon can't use these items themselves, but a trainer can use them on pokemon</br>
	 * <i><b>MISC</b></i> - Items that have no real unique use, other than existing i.e. everstones
	 * 
	 * @author JB227
	 */
	public enum Category { BERRY, BATTLE, EVO, STONE, TYPE, TRAINER, MISC }
	
	/**
	 * A way of differentiating the different types of item
	 * @return a {@link Category} saying what this item is
	 */
	public abstract Category getCategory();
	
	public Item(String n) {
		this.name = n;
	}
	
	public String name;
	public String getName() { return name; }
	
	public abstract Item copy();
	
	/**
	 * Is this item one use only?
	 * @return true if it is discarded after use
	 */
	public abstract boolean isConsumable();
	
	/**
	 * The effect an item has on a Pokemon
	 * @param p
	 * @return true if there is a change to be investigated, else false
	 */
	public abstract boolean use(Pokemon p, Location place);
	
	/**
	 * Contributes to the toString
	 * @return a String describing this item
	 */
	public abstract String description();
	public String toString() {
		return name + " - " + description();
	}
}
