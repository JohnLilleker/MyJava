package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class GenderCanHit extends Timed {

	public enum Who {
		SAME, DIFFERENT
	};

	private Who is_same;

	public GenderCanHit(Who cond) {
		is_same = cond;
	}

	/**
	 * Only hits those of a gender either same or different.
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		// basically, if they are different genders check if that means it can
		// attack
		if (attacker.oppositeGenders(defender) ? is_same == Who.DIFFERENT : is_same == Who.SAME) {
			attack.getDamageEffect().harm(attacker, attack, defender, arena);
		} else {
			arena.log("But it failed!");
		}
		attack.use();
	}

	public Timed copy() {
		return new GenderCanHit(is_same);
	}

	public String description() {
		return "Works only if the target is the " + ((is_same == Who.SAME) ? "same" : "opposite") + " gender. ";
	}

	public String code() {
		return "gender " + is_same;
	}

}
