package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Item;
import joreli.Pokemon;

public class ThiefEffect extends Effect {

	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (defender.isHoldingItem() && !attacker.isHoldingItem()) {
			Item i = defender.takeItem();
			arena.log(attacker.getName() + " took " + defender.getName() + "'s " + i.getName() + "!");
			attacker.giveItem(i);
		}
	}

	public Effect copy() {
		return new ThiefEffect();
	}

	public String description() {
		return "Steals the targets held item. ";
	}

	public String code() {
		return "steal";
	}

}
