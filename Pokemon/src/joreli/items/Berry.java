package joreli.items;

import joreli.Battle;
import joreli.Item;
import joreli.Pokemon;
import joreli.NatureGen;

/**
 * An abstract class, since there are many different berries.<br>The only implemented methods are the category and consumable
 * @author JB227
 *
 */
public abstract class Berry extends Item {

	protected String berry_name;
	protected Taste taste;
	
	public enum Taste {SPICY, SOUR, DRY, BITTER, SWEET, NONE} 
	
	public Berry(String n, Taste t) {
		super(n + " Berry");
		berry_name = n;
		taste = t;
	}
	
	/**
	 * Autonomous usage by a pokemon
	 * @param p
	 * @return
	 */
	public abstract boolean eat(Pokemon p, Battle arena);
	
	public Category getCategory() {
		return Category.BERRY;
	}

	public boolean isConsumable() {
		return true;
	}
	
	/**
	 * Indicates if a pokemon likes the taste of this berry 
	 * @param p the devouring pokemon
	 * @return 0, no preference. 1, likes taste. -1, hates taste.
	 */
	public int likesTaste(Pokemon p) {
		if (taste == Taste.NONE) return 0;
		float[] n = NatureGen.createNature(p.getNature());
		int best = -1;
		int worst = -1;
		for (int i = 0; i < n.length; i++) {
			if (n[i] > 1) best = i;
			if (n[i] < 1) worst = i;
		}
		if (best == -1 && worst == -1) return 0;
		
		if (Taste.values()[best] == taste) return 1;
		if (Taste.values()[worst] == taste) return -1;
		
		return 0;
	}
}
