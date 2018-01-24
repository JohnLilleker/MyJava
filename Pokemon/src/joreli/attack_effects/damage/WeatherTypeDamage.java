package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Type;
import joreli.Weather;

public class WeatherTypeDamage extends Damage {

	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	public Damage copy() {
		return new WeatherTypeDamage();
	}

	public String description() {
		return "Damage and type based on the weather. ";
	}

	public String code() {
		return "weatherType";
	}
	
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		if (arena.getWeather() == Weather.CLEAR)
			return attack.getPow();
		else {
			return attack.getPow() * 2;
		}
	}
	
	protected Type getAttackType(Attack attack, Pokemon attacker, Battle arena) {
		Type t;
		switch(arena.getWeather()) {
		case HAIL:
			t = Type.ICE;
			break;
		case RAIN:
			t = Type.WATER;
			break;
		case SANDSTORM:
			t = Type.ROCK;
			break;
		case SUN:
			t = Type.FIRE;
			break;
		default:
			t = attack.getType();
			break;
		}
		return t;
	}

}
