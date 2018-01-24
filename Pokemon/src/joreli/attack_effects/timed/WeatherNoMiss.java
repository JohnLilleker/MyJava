package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class WeatherNoMiss extends Timed {

	private Weather weather;
	
	public WeatherNoMiss(Weather w) {
		weather = w;
	}
	
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		if (arena.getWeather() == weather) attacker.aim();
		attack.getDamageEffect().harm(attacker, attack, defender, arena);
		if (arena.getWeather() == weather) attacker.endAim();
		attack.use();
	}

	public Timed copy() {
		return new WeatherNoMiss(weather);
	}

	public String description() {
		return String.format("Never misses in %s weather. ", weather);
	}

	public String code() {
		return String.format("weatherNoMiss %s", weather);
	}

}
