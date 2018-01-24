package joreli.attack_effects.accuracy;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class LevelDiffAccuracy extends Accuracy {

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
		int lev_diff = attacker.getLevel() - defender.getLevel();

		if (lev_diff < 0)
			return true;

		if (defender.hidingPlace() != Battle.Place.NOWHERE)
			return true;

		if (attacker.nomiss())
			return false;

		float acc = (attack.getAccuracy() + lev_diff) / 100.f;
		float miss = attacker.getAccuracy() / defender.getEvasiveness();
		acc = acc * miss;
		return Math.random() > acc;
	}

	@Override
	public Accuracy copy() {
		return new LevelDiffAccuracy();
	}

}
