package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class TrapEffect extends Effect {

	private Battle.Trap trap;
	
	public TrapEffect(Battle.Trap t) {
		trap = t;
	}
	
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		Battle.Position target = arena.positionOfPokemon(defender);
		if (arena.setTrap(trap, target)) {
				String t = "spikes";
				switch(trap) {
				case TOXIC:
					t = "poison " + t;
					break;
				case ROCK:
					t = "floating rocks";
					break;
				case SPIKES:
					break;
				
				}
				arena.log(attacker.getName() + " threw " + t + " towards the opposing team!");
		}
		else
			arena.log("But it failed!");
	}

	public Effect copy() {
		return new TrapEffect(trap);
	}

	public String description() {
		String t = "spikes";
		String what = "damaging";
		switch(trap) {
		case TOXIC:
			t = "poison " + t;
			what = "poisoning";
			break;
		case ROCK:
			t = "floating rocks";
			break;
		case SPIKES:
			break;
		
		}
		// either throws poison* spikes or floating rocks
		// the user lays a trap of * around the opposing team
		return String.format("The user lays a trap of %s around the opposing team, %s pokemon that switch in. ", t, what);
	}

	public String code() {
		return String.format("trap %s", trap);
	}

}
