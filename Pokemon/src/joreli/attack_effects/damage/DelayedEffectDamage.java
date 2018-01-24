package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.attack_effects.effect.Effect;

public class DelayedEffectDamage extends Damage {

	private int delay;
	
	public DelayedEffectDamage(int d) {
		delay = d;
	}
	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	protected void nextStep(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (damage != 0 && attacker.consecutiveTurnsUsed(attack.getName()) > delay) {
			for (Effect e : attack.getEffects()) {
				e.effect(attacker, damage, attack, defender, arena);
			}
		}
	}
	
	public Damage copy() {
		return new DelayedEffectDamage(delay);
	}

	public String description() {
		return String.format("Causes effects after %d turns. ", delay);
	}

	public String code() {
		return String.format("delay %d", delay);
	}

}
