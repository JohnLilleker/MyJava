package joreli.attack_effects.accuracy;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class IgnoreEvasionAccuracy extends Accuracy {

	@Override
	public String description() {
		return "";
	}

	@Override
	public String code() {
		return "";
	}

	@Override
	public boolean hasMissed(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		if (attack.effectsSelf()) {
			return false;
		}

		if (attacker.nomiss()) {
			return false;
		}

		if (defender.hidingPlace() != Battle.Place.NOWHERE)
			return true;

		if (attack.getAccuracy() == 0) {
			return false;
		}

		float acc = (float) attack.getAccuracy() / 100;
		float miss = attacker.getAccuracy() / 100;
		acc = acc * miss;
		return Math.random() > acc;
	}

	@Override
	public Accuracy copy() {
		return new IgnoreEvasionAccuracy();
	}

}
