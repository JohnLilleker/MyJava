package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Item;
import joreli.Pokemon;
import joreli.Type;
import joreli.items.TypeChange;

public class ItemTypeDamage extends Damage {

	private TypeChange.Device dev;
	
	public ItemTypeDamage(TypeChange.Device d) {
		dev = d;
	}
	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new ItemTypeDamage(dev);
	}

	public String description() {
		return "Type depends on the held " + ((dev == TypeChange.Device.PLATE) ? "Plate. " : "Drive. ");
	}
	
	protected Type getAttackType(Attack attack, Pokemon attacker, Battle arena) {
		Type t = attack.getType();
		if (attacker.isHoldingItem()) {
			Item i = attacker.getItem();
			if (i.getCategory() == Item.Category.TYPE) {
				TypeChange c = (TypeChange) i;
				if (c.isItem(dev)) {
					t = c.getType();
				}
			}
		}
		return t;
	}

	public String code() {
		return String.format("itemType %s", dev);
	}
	
	

}
