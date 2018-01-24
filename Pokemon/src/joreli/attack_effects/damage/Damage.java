package joreli.attack_effects.damage;

import joreli.*;
import joreli.attack_effects.Describable;
import joreli.attack_effects.effect.Effect;
import joreli.items.TypeChange;

/**
 * Causes damage to a Pokemon, before following up with effects
 * 
 * @author JB227
 *
 */
public abstract class Damage implements Describable {

	/**
	 * Gives the damage returned to a pokemon by an attack
	 * 
	 * @param attacker
	 *            attacking pokemon
	 * @param attack
	 *            attack
	 * @param defender
	 *            defending pokemon
	 * @param arena
	 *            the battle environment
	 * @return the amount of health to decrease
	 */
	public int damage(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {

		// if it only effects the user or changes the battle object, no further action
		// needs to be taken
		if (attack.effectsSelf() || attack.effectsBattle())
			return -1;

		if (attack.getAcuracyObj().hasMissed(attack, attacker, defender, arena)) {
			arena.log("It missed!");
			return 0;
		}

		if (isProtected(defender, attacker, attack)) {
			arena.log(defender.getName() + " protected itself");
			return 0;
		}

		if (attack.status()) {
			return -1;
		}
		float damage;

		float[] att_def = stats(attacker, attack.physical(), defender, arena.getWeather());
		float att = att_def[0];
		float def = att_def[1];

		float pow = (float) getAttackPower(attack, attacker, defender, arena);

		// actual damage equation
		damage = ((((attacker.getLevel() * 2) / 5) * pow * (att / def)) / 50) + 2;

		// random range 0.85-1
		float r = (float) (0.85 + (Math.random() * 0.15));
		damage *= r;

		float mult = typeMatch(attacker, attack, defender, arena);

		if (mult == 0) {
			return 0;
		}

		if (critical(attacker)) {
			damage *= 1.5;
			arena.log("Critical hit!");
		}

		damage *= mult;

		damage *= battleEffect(attack, arena, attacker);

		damage *= otherEffects(attacker, defender);

		if (damage < 1)
			damage = 1;

		return Math.round(damage);
	}

	/**
	 * Type match manager
	 * 
	 * @param attacker
	 *            Attacking pokemon
	 * @param attack
	 *            Attack
	 * @param defender
	 *            Defending Pokemon
	 * @return type multiplier
	 */
	protected float typeMatch(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {

		TypeCompare weak = new TypeCompare();
		float mult = weak.typeCompare(defender, this.getAttackType(attack, attacker, arena));

		if (mult > 1)
			arena.log("It's super effective!");
		if (mult > 0 && mult < 1)
			arena.log("It's not very effective...");
		if (mult == 0)
			arena.log("It doesn't affect " + defender.getName());

		if (attacker.typeMatch(getAttackType(attack, attacker, arena)))
			mult *= 1.5f;

		if (attacker.isHoldingItem()) {
			Item i = attacker.getItem();
			if (i.getCategory() == Item.Category.TYPE) {
				TypeChange t = (TypeChange) i;
				if (t.isItem(TypeChange.Device.PLATE) && t.getType() == attack.getType())
					mult *= 1.2f;
			}
		}

		return mult;
	}

	/**
	 * Weather effect manager
	 * 
	 * @param attack
	 *            The attack
	 * @param a
	 *            the battle object
	 * @return multiplier based on environment condition
	 */
	protected float battleEffect(Attack attack, Battle arena, Pokemon attacker) {
		float effect = 1;
		if (arena.getWeather() == Weather.RAIN) {
			if (getAttackType(attack, attacker, arena) == Type.FIRE)
				effect *= 0.5;
			if (getAttackType(attack, attacker, arena) == Type.WATER)
				effect *= 1.5;
		}
		if (arena.getWeather() == Weather.SUN) {
			if (getAttackType(attack, attacker, arena) == Type.WATER)
				effect *= 0.5;
			if (getAttackType(attack, attacker, arena) == Type.FIRE)
				effect *= 1.5;
		}
		return effect;
	}

	/**
	 * Critical hit manager.
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @return critical hit? true or false
	 */
	protected boolean critical(Pokemon attacker) {
		// the end can be reached via critical attack, focus energy and an item
		final double[] LEVELS = { 0.0625, 0.125, 0.25, 0.33333333, 0.5 };
		return Math.random() < LEVELS[criticalLevel(attacker)];
	}

	protected int criticalLevel(Pokemon attacker) {
		int level = 0;
		if (attacker.critical())
			level += 2;
		return level;
	}

	/**
	 * Attack and defence stat manager. gives either normal attack and defnece or
	 * special attack and special defence
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @param physical
	 *            Is the attack a physical attack?
	 * @param defender
	 *            Defending Pokemon
	 * @param w
	 *            The weather
	 * @return float array {attack value, defence value}
	 */
	protected float[] stats(Pokemon attacker, boolean physical, Pokemon defender, Weather w) {
		float att = attacker.getAtk();

		float def = defender.getDef();

		if (!physical) {
			att = attacker.getSpAtk();
			// increase sp.def of ROCK if SANDSTORM
			def = defender.getSpDef();
			if (w == Weather.SANDSTORM && defender.typeMatch(Type.ROCK))
				def *= 1.5f;
		}
		float[] r = { att, def };
		return r;
	}

	/**
	 * Other things that may effect damage caused.
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @param defender
	 *            Defending Pokemon
	 * @return a multiplier
	 */
	protected float otherEffects(Pokemon attacker, Pokemon defender) {
		return 1;
	}

	/**
	 * Checks the opposing pokemon is protecting themself
	 * 
	 * @param defender
	 * @return
	 */
	protected boolean isProtected(Pokemon defender, Pokemon attacker, Attack attack) {
		boolean protect = defender.isProtecting();
		if (protect && attacker.nomiss()) {
			int acc = attack.getAccuracy();
			int canBreak = 100 - acc;
			return Math.random() * 100 < canBreak;
		}
		return protect;
	}

	/**
	 * Gets the attack power, can be overridden for variable attack powers
	 * 
	 * @param attack
	 *            the attack
	 * @param attacker
	 *            the attacking pokemon
	 * @param defender
	 *            the defending pokemon
	 * @return
	 */
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		return attack.getPow();
	}

	protected Type getAttackType(Attack attack, Pokemon attacker, Battle arena) {
		return attack.getType();
	}

	/**
	 * Causes damage to a Pokmeon, then follow up on the effects
	 * 
	 * @param attacker
	 *            attacking pokemon
	 * @param attack
	 *            attack
	 * @param defender
	 *            defending pokemon
	 * @param arena
	 *            the current battle
	 * @return the amount of damage caused, indicating success
	 */
	public abstract int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena);

	/**
	 * Causes default damage before carrying on. Helps with DRY
	 * 
	 * @param attacker
	 *            attacking pokemon
	 * @param attack
	 *            attack
	 * @param defender
	 *            defending pokemon
	 * @param arena
	 *            the current battle
	 * @return the amount of damage caused, indicating success
	 */
	protected int defaultBehaviour(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		int damage = this.damage(attacker, attack, defender, arena);
		int pain = damage;
		if (damage > -1) {
			damage = (damage > defender.getHealth()) ? defender.getHealth() : damage;
			pain = defender.damage(damage, attack.getCategory(), arena);
		}
		nextStep(attacker, pain, attack, defender, arena);
		return damage;
	}

	/**
	 * Carry on with the attack chain, use the effects from attack
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @param damage
	 *            damage caused by harm
	 * @param attack
	 *            the attack
	 * @param defender
	 *            Defending Pokemon
	 * @param w
	 *            Weather
	 */
	protected void nextStep(Pokemon attacker, int damage, Attack attack, Pokemon defender, Battle arena) {
		if (damage != 0) {
			for (Effect e : attack.getEffects()) {
				e.effect(attacker, damage, attack, defender, arena);
			}
		}
	}

	/**
	 * A check for unconventional attacks, see's if an attack can hit
	 * 
	 * @param attacker
	 *            Attacking Pokemon
	 * @param damage
	 *            damage caused by harm
	 * @param attack
	 *            the attack
	 * @param defender
	 *            Defending Pokemon
	 * @param w
	 *            Weather
	 * @param print
	 *            prints stuff that happens if true
	 * @return
	 */
	protected boolean canHit(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		if (!attack.getAcuracyObj().hasMissed(attack, attacker, defender, arena)) {
			if (!TypeCompare.isImmune(defender, getAttackType(attack, attacker, arena))) {
				if (!isProtected(defender, attacker, attack))
					return true;
				else
					arena.log(defender.getName() + " protected itself");
			} else
				arena.log("But it didn't effect " + defender.getName());
		} else
			arena.log("It missed!");

		return false;
	}

	/**
	 * 
	 * @return A copy of the Damage subclass
	 */
	public abstract Damage copy();

	public String toString() {
		return description();
	}

}
