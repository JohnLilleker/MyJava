package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Weather;

public class WeatherEffect extends Effect {

	private Weather change;

	/**
	 * Change the weather
	 * 
	 * @param w
	 *            What to change to
	 */
	public WeatherEffect(Weather w) {
		change = w;
	}

	/**
	 * Changes the weather of the arena
	 */
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		arena.setWeather(change);
		switch (change) {
		case HAIL:
			arena.log("A Hail storm started!");
			break;
		case RAIN:
			arena.log("Rain began to fall!");
			break;
		case SANDSTORM:
			arena.log("A Sandstorm kicked up!");
			break;
		case SUN:
			arena.log("The Sunlight turned harsh!");
			break;
		default:
			break;
		}
	}

	public Effect copy() {
		return new WeatherEffect(change);
	}

	public String description() {
		return "Changes the weather to " + change + ". ";
	}
	
	public String code() {
		return "weather " + change;
	}

}
