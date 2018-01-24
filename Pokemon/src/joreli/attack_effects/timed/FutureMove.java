package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class FutureMove extends Timed {

	private int turns;

	public FutureMove(int t) {
		this.turns = t;
	}

	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());
		arena.log(attacker.getName() + " foresaw an attack!");


		int m = arena.getLogMode();
		arena.setLogMethod(Battle.NONE);
		int damage = attack.getDamageEffect().damage(attacker, attack, defender, arena);
		arena.setLogMethod(m);
		arena.enqueueAttack(attack, damage, turns, arena.positionOfPokemon(defender));
		attack.use();
	}

	public Timed copy() {
		return new FutureMove(turns);
	}

	public String description() {
		return "Attacks the target in " + turns + " turns. Works even if either pokemon switches out or faints. ";
	}

	public String code() {
		return "future " + turns;
	}
}
