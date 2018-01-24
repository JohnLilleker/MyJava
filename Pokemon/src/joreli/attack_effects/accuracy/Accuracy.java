package joreli.attack_effects.accuracy;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.attack_effects.Describable;

public abstract class Accuracy implements Describable {

	public abstract boolean hasMissed(Attack attack, Pokemon attacker, Pokemon defender, Battle arena);

	public String toString() {
		return this.description();
	}

	public abstract Accuracy copy();
}
