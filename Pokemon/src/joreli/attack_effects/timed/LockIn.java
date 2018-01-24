package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class LockIn extends Timed {

	private int repeating;
	
	public LockIn(int r) {
		repeating = r;
	}
	
	/**
	 * Forces the Pokemon to keep using this attack over a number of turns
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName() + "!");
		
		if (attacker.consecutiveTurnsUsed(attack.getName()) == 1) {
			attack.use();
		}
		
		int uses = attacker.consecutiveTurnsUsed(attack.getName());
		int damage = attack.getDamageEffect().harm(attacker, attack, defender, arena);
		if (damage > 1 && uses <= repeating) {
			attacker.setCharging(attack);
		}
		else {
			attacker.setCharging(null);
		}
	}

	public Timed copy() {
		return new LockIn(repeating);
	}

	public String description() {
		return String.format("Forces the user to attack over %d turns. ", repeating);
	}

	public String code() {
		return String.format("repeat %d", repeating);
	}

}
