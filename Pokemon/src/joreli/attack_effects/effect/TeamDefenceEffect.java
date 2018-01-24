package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class TeamDefenceEffect extends Effect {

	private Battle.Defence defence;
	
	public TeamDefenceEffect(Battle.Defence d) {
		defence = d;
	}
	
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		Battle.Position pos = arena.positionOfPokemon(attacker);
		arena.setDefence(pos, defence);
	}

	public Effect copy() {
		return new TeamDefenceEffect(defence);
	}

	public String description() {
		String desc = "Some beneficial effect. ";
		switch(defence) {
		case MIST:
			desc = "Causes mist preventing the user's team from having their stats lowered. ";
			break;
		case REFLECT:
			desc = "Causes a mysterious barrier halving physical damage. ";
			break;
		case SAFEGUARD:
			desc = "Causes a mysterious barrier preventing status conditions for 5 turns. ";
			break;
		case SCREEN:
			desc = "Causes a mysterious barrier halving special damage. ";
			break;
		case TAILWIND:
			desc = "Increases all team members' speed for 3 turns. ";
			break;
		}
		return desc;
	}

	public String code() {
		return String.format("defence %s", defence);
	}

}
