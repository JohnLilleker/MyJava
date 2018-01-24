package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class HideCharged extends Timed {

	private Battle.Place place;

	public HideCharged(Battle.Place loc) {
		place = loc;
	}

	/**
	 * Disappear, then strike
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		if (!attacker.isCharging()) {
			attacker.setCharging(attack);
			attacker.hide(place);
			String where = "";
			switch (place) {
			case SKY:
				where = " flew up high";
				break;
			case GROUND:
				where = " dug underground";
				break;
			case WATER:
				where = " dived underwater";
				break;
			case ETHER :
				where = " disappeared";
				break;
			default:
				break;
			}

			arena.log(attacker.getName() + where);
			
		} else {
			attacker.setCharging(null);
			attacker.hide(Battle.Place.NOWHERE);
			attack.getDamageEffect().harm(attacker, attack, defender, arena);
			attack.use();
		}

	}

	public Timed copy() {
		return new HideCharged(place);
	}

	public String description() {
		String where = "disappears";
		switch (place) {
		case SKY:
			where = "goes high up";
			break;
		case GROUND:
			where = "digs underground";
			break;
		case WATER:
			where = "dives underwater";
			break;
		default:
			break;
		}
		return "The user " + where + " then attacks on the second turn. ";
	}

	public String code() {
		return "hide " + place;
	}

}
