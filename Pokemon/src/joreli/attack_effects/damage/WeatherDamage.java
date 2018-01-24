package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class WeatherDamage extends Damage {

	private Weather[] weather;
	private float multiplier;
	
	public WeatherDamage(Weather[] w, float m) {
		weather = w;
		multiplier = m;
	}
	public WeatherDamage(String desc, float m) {
		String[] w = desc.split(",");
		weather = new Weather[w.length];
		for (int i = 0; i < w.length; i++) {
			weather[i] = Weather.valueOf(w[i]);
		}
		multiplier = m;
	}
	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new WeatherDamage(weather, multiplier);
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		float mult = 1;
		for (Weather w : weather) {
			if (arena.getWeather() == w) {
				mult = multiplier;
			}
		}
		return (int) (attack.getPow() * mult);
	}

	public String description() {
		String weathers = "";
		for (int i = 0; i < weather.length; i++) {
			weathers += weather[i];
			if (i < weather.length-2) weathers += ", ";
			else if (i < weather.length-1) weathers += " or ";
		}
		return String.format("Deals %fx damage if the weather is %s. ", multiplier, weathers);
	}

	public String code() {
		String weathers = "";
		for (int i = 0; i < weather.length; i++) {
			weathers += weather[i];
			if (i < weather.length-1) weathers += ",";
		}
		return String.format("weatherDamage %s %f", weathers, multiplier);
	}

}
