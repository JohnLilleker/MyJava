package joreli;

import joreli.attack_effects.accuracy.Accuracy;
import joreli.attack_effects.damage.Damage;
import joreli.attack_effects.effect.Effect;
import joreli.attack_effects.timed.Timed;

/* Created: 15/3/14
 * Last Modified: 16/1/18 (Accuracy object)
 */
/**
 * The Attack class
 * 
 * @author JB227
 */
public class Attack {

	public enum Category {
		PHYSICAL, SPECIAL, STATUS
	};

	// allows possible extension later
	public enum Target {
		SELF, ENEMY, BATTLE
	};

	private String name;
	private int power;
	private int pp;
	private Type type;
	private int accuracy;
	private Category kind;
	private int priority;

	private Timed when;
	private Damage damage;
	private Effect[] effects;
	private Accuracy canMiss;

	private int energy;
	private Target target;

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
	 * @param target
	 *            who the attack is aimed at
	 * @param spd
	 *            priority, highest goes first
	 * @param w
	 *            a subclass of Timed describing when the attack happens
	 * @param dam
	 *            a subclass of Damage describing how the attack is carried out
	 * @param es
	 *            an array of Effect subclasses describing any additional effects
	 *            the attack has
	 */
	public Attack(String n, int p, int a, int pp, Type t, Category c, Target target, int spd, Timed w, Damage dam,
			Effect[] es, Accuracy miss) {
		this.name = n;
		this.power = (c == Category.STATUS) ? 0 : p;
		this.pp = pp;
		this.type = t;
		this.accuracy = a;
		this.kind = c;
		this.energy = pp;
		this.priority = spd;
		this.when = w;
		this.damage = dam;
		this.effects = es;
		this.target = target;
		this.canMiss = miss;
	}

	public String getName() {
		return name;
	}

	public int getPow() {
		return power;
	}

	public int getUses() {
		return energy;
	}

	public int getMaxPP() {
		return pp;
	}

	public Type getType() {
		return type;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public boolean physical() {
		return kind == Category.PHYSICAL;
	}

	public boolean special() {
		return kind == Category.SPECIAL;
	}

	public boolean status() {
		return kind == Category.STATUS;
	}

	public int getPriority() {
		return priority;
	}

	public boolean effectsSelf() {
		return target == Target.SELF;
	}

	public boolean effectsBattle() {
		return target == Target.BATTLE;
	}

	public String toString() {
		String d = (power > 0) ? String.valueOf(power) : "-";
		String a = (accuracy > 0) ? String.valueOf(accuracy) : "-";

		String output = name + " Pow: " + d + ". Accuracy: " + a + "%. PP: " + energy + "/" + pp + ". Type: " + type
				+ ". Category: " + kind + "\n";

		String nvmiss = "";
		String spd = "";
		if (accuracy == 0 && !status() && !effectsSelf()) {
			nvmiss += "Never misses. ";
		}

		if (priority == 2) {
			spd += "Always goes first. If used against similar attacks, speed counts. ";
		} else if (priority == 3) {
			spd += "Faster than all other moves. ";
		}

		output += spd + nvmiss;

		String w = when.toString();
		String dam = damage.toString();
		String ef = "";
		for (Effect e : effects)
			ef += e;

		output += w;
		// prevent it from having "a regular attack" if it isn't
		if (!(dam.contains("regular") && !(ef.equalsIgnoreCase("") && w.equalsIgnoreCase("")
				&& nvmiss.equalsIgnoreCase("") && spd.equalsIgnoreCase("")))) {
			output += dam;
		}
		output += ef;

		return output;
	}

	public Attack copy() {
		Effect[] e = new Effect[effects.length];
		for (int i = 0; i < e.length; i++)
			e[i] = effects[i].copy();
		return new Attack(name, power, accuracy, pp, type, kind, target, priority, when.copy(), damage.copy(), e,
				canMiss.copy());
	}

	protected final String SHWORD = "Sh4d0wM0rph";

	public void use() {
		energy--;
	}

	public void recharge() {
		this.energy = this.pp;
	}

	public void recharge(int e) {
		this.energy += e;
		if (this.energy > this.pp)
			this.energy = this.pp;
	}

	public boolean exhausted() {
		return (energy <= 0);
	}

	public Timed getTiming() {
		return when;
	}

	public Damage getDamageEffect() {
		return damage;
	}

	public Effect[] getEffects() {
		return effects;
	}

	public Accuracy getAcuracyObj() {
		return canMiss;
	}

	public Category getCategory() {
		return kind;
	}

	public void effect(Pokemon attacker, Pokemon target, Battle arena) {
		when.use(attacker, this, target, arena);
	}

	/**
	 * Builds a string describing the attack's Timed, Damage and Effect
	 * components.<br>
	 * The sections are separated by |, with each effect also | separated. the codes
	 * are documented elsewhere
	 * 
	 * @return
	 */
	public String getEffectString() {
		String time = when.code();

		String pain = damage.code();
		String acc = canMiss.code();

		if (acc.length() > 0)
			pain += "|" + acc;

		String efs = "";
		for (int i = 0; i < effects.length; i++) {
			efs += effects[i].code();
			if (i < effects.length - 1)
				efs += "|";
		}

		return time + "|" + pain + "|" + efs;
	}
}
