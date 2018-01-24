package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class HideDamage extends Damage {

	private Battle.Place notsafe;
	private float mult;

	public HideDamage(Battle.Place l, float m) {
		notsafe = l;
		mult = m;
	}

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		return (int) (attack.getPow() * ((defender.hidingPlace().equals(notsafe)) ? mult : 1));
	}

	public Damage copy() {
		return new HideDamage(notsafe, mult);
	}

	public String description() {
		String where = "that are invisible";
		switch (notsafe) {
		case SKY:
			where = "in the air";
			break;
		case GROUND:
			where = "underground";
			break;
		case WATER:
			where = "underwater";
			break;
		default:
			break;
		}
		return "Can even hit targets " + where + ". ";
	}

	public String code() {
		return "no_hide " + notsafe + " " + mult;
	}

}
