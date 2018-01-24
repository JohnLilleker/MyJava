package joreli;

import java.util.ArrayList;

import joreli.Attack.Category;
import joreli.Attack.Target;
import joreli.attack_effects.accuracy.Accuracy;
import joreli.attack_effects.accuracy.StandardAccuracy;
import joreli.attack_effects.damage.Damage;
import joreli.attack_effects.damage.RegularDamage;
import joreli.attack_effects.effect.Effect;
import joreli.attack_effects.timed.OneTurn;
import joreli.attack_effects.timed.Timed;

public class AttackBuilder {

	private String name;
	private int power;
	private int pp;
	private Type type;
	private int accuracy;
	private Category category;

	private int priority = 1;
	private Target target = Target.ENEMY;
	private Timed when = new OneTurn();
	private Damage damage = new RegularDamage();
	private ArrayList<Effect> effects = new ArrayList<>();
	private Accuracy canMiss = new StandardAccuracy();

	/**
	 * 
	 * @param n
	 *            name
	 * @param p
	 *            power
	 * @param pp
	 *            power points / uses
	 * @param t
	 *            type
	 * @param a
	 *            accuracy
	 * @param pow
	 *            PHYSICAL, SPECIAL or STATUS
	 */
	public AttackBuilder(String n, int p, int a, int pp, Type t, Category c) {
		this.name = n;
		this.power = p;
		this.accuracy = a;
		this.pp = pp;
		this.type = t;
		this.category = c;
	}

	public AttackBuilder withPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public AttackBuilder withTarget(Target target) {
		this.target = target;
		return this;
	}

	public AttackBuilder withTimed(Timed timed) {
		this.when = timed;
		return this;
	}

	public AttackBuilder withDamage(Damage damage) {
		this.damage = damage;
		return this;
	}

	public AttackBuilder withAccuracy(Accuracy accuracy) {
		this.canMiss = accuracy;
		return this;
	}

	public AttackBuilder withEffect(Effect effect) {
		effects.add(effect);
		return this;
	}

	public Attack build() {
		Effect[] es = new Effect[effects.size()];
		for (int i = 0; i < es.length; i++)
			es[i] = effects.get(i);
		return new Attack(name, power, accuracy, pp, type, category, target, priority, when, damage, es, canMiss);
	}
}
