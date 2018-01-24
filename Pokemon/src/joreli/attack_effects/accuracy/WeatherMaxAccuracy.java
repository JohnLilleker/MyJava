package joreli.attack_effects.accuracy;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class WeatherMaxAccuracy extends Accuracy {

	private Weather favoured;

	public WeatherMaxAccuracy(Weather favoured) {
		this.favoured = favoured;
	}

	@Override
	public String description() {
		return "This attack has 100% accuracy in " + favoured;
	}

	@Override
	public String code() {
		return String.format("nvmissWeather %s", favoured);
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

		if (attack.getAccuracy() == 0)
			return false;

		if (arena.getWeather() == favoured)
			return false;

		float acc = (float) attack.getAccuracy() / 100;
		float miss = attacker.getAccuracy() / defender.getEvasiveness();
		acc = acc * miss;
		return Math.random() > acc;
	}

	@Override
	public Accuracy copy() {
		return new WeatherMaxAccuracy(favoured);
	}

}
