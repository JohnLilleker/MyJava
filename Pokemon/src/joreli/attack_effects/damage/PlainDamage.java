package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Type;

public class PlainDamage extends Damage {

	/**
	 * Does typless damage without critical hits. Used in Future sight and could
	 * be used elsewhere
	 */
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	protected Type getAttackType(Attack attack, Pokemon attacker, Battle arena) {
		return Type.NONE;
	}

	protected boolean critical(Pokemon attacker) {
		return false;
	}

	public Damage copy() {
		return new PlainDamage();
	}

	public String description() {
		return "Inflicts typeless damage. ";
	}

	public String code() {
		return "typeless";
	}

}
