package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class WeatherOneTurn extends Timed {

	private Weather weather;
	
	public WeatherOneTurn(Weather w) {
		weather = w;
	}
	
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		
		if (arena.getWeather() != weather || attacker.isCharging()) {
			if (!attacker.isCharging()) {
				attacker.setCharging(attack);
				arena.log(attacker.getName() + " is charging");
			} else {
				attacker.setCharging(null);
				attack.getDamageEffect().harm(attacker, attack, defender, arena);
				attack.use();
			}
		}
		else {
			attacker.setCharging(null);
			attack.getDamageEffect().harm(attacker, attack, defender, arena);
			attack.use();
		}
	}

	public Timed copy() {
		return new WeatherOneTurn(weather);
	}

	public String description() {
		return String.format("Attacks in one turn if the weather is %s, else charges first then attacks. ", weather);
	}

	public String code() {
		return String.format("weatherOneTurn %s", weather);
	}

}
