package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RemoveDefenceEffect extends Effect {

	private String defs;
	private Battle.Defence[] defences;

	public RemoveDefenceEffect(String ds) {
		defs = ds;
		String[] sDefences = ds.split(",");
		defences = new Battle.Defence[sDefences.length];
		for (int i = 0; i < sDefences.length; i++) {
			defences[i] = Battle.Defence.valueOf(sDefences[i]);
		}
	}
	
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		arena.removeDefences(arena.positionOfPokemon(defender), defences);
	}

	public Effect copy() {
		return new RemoveDefenceEffect(defs);
	}

	public String description() {
		if (defences.length > 3) {
			return "Removes all the target's barriers, such as Safeguard and Reflect. ";
		}
		String desc = "";
		for (int i = 0; i < defences.length; i++) {
			switch(defences[i]) {
			case MIST:
				desc += "Mist";
				break;
			case REFLECT:
				desc += "Reflect";
				break;
			case SAFEGUARD:
				desc += "Safeguard";
				break;
			case SCREEN:
				desc += "Light Screen";
				break;
			case TAILWIND:
				desc += "Tailwind";
				break;
			}
			if (i < defences.length - 2)
				desc += ", ";
			else if (i == defences.length-2)
				desc += " and ";
		}
		return String.format("Removes the target's %s. ", desc);
	}

	public String code() {
		return "remDef " + defs;
	}

}
