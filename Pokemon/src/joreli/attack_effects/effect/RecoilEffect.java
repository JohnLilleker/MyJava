package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RecoilEffect extends Effect {

	private float recoil;

	/**
	 * 
	 * @param r
	 *            % recoil from the attack
	 */
	public RecoilEffect(float r) {
		this.recoil = r;
	}

	/**
	 * Damages the attacking pokemon as well
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (damage > 0) {
			arena.log(attacker.getName() + " is hurt by recoil!");
			attacker.damage(Math.round(recoil * damage), Attack.Category.STATUS, arena);
		}
	}

	public Effect copy() {
		return new RecoilEffect(recoil);
	}

	public String description() {
		String mag = (recoil < 0.3f) ? "a little" : "quite a lot";
		return "Damages the user " + mag + " as well. ";
	}

	public String code() {
		return "recoil " + recoil;
	}


}
