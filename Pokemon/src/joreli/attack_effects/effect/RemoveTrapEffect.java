package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class RemoveTrapEffect extends Effect {

	private Target who;
	
	public RemoveTrapEffect(Target t) {
		who = t;
	}
	
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		Pokemon p = (who == Target.SELF) ? attacker : defender;
		Battle.Position pos = arena.positionOfPokemon(p);
		
		arena.removeTrap(pos);
	}

	public Effect copy() {
		return new RemoveTrapEffect(who);
	}

	public String description() {
		return String.format("Removes traps from the %s side of the field. ", (who == Target.SELF) ? "user's" : "target's");
	}

	public String code() {
		return String.format("removeTrap %s", who);
	}

}
