package joreli.attack_effects.effect;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Trainer;

public class SwitchEffect extends Effect {

	private Target whoSwitched;
	
	public SwitchEffect(Target who) {
		whoSwitched = who;
	}
	
	public void effect(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		Battle.Position pos;
		Pokemon next = null;
		if (whoSwitched == Target.TARGET) {
			if (defender.fainted())
				return;
			pos = arena.positionOfPokemon(defender);
			Trainer t = arena.getTrainerForPos(pos);
			
			if (t == null) {
				defender.run();
				return;
			}
			if (t.actives() > 1)
				next = t.randomTeamMember(defender);
		}
		else {
			if (attacker.fainted())
				return;
			pos = arena.positionOfPokemon(attacker);
			Trainer t = arena.getTrainerForPos(pos);
			
			if (t != null)
				if (t.actives() > 1)
					next = t.selectPokemon(attacker);
			
		}
		if (next != null)
			arena.switchIn(next, pos);
	}

	public Effect copy() {
		return new SwitchEffect(whoSwitched);
	}

	public String description() {
		if (whoSwitched == Target.TARGET) {
			return "The target is switched out and replaced by a random pokemon. Ends a wild pokemon battle. ";
		}
		else {
			return "The user is switched out. ";
		}
	}

	public String code() {
		// TODO Auto-generated method stub
		return String.format("switch %s", whoSwitched);
	}

}
