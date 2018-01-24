package joreli;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import joreli.attack_effects.damage.PlainDamage;
import joreli.attack_effects.effect.RecoilEffect;
import joreli.items.Berry;
import joreli.items.Evo;

/* Created: 15/3/14
 * Last Modified: 19/8/17 Leech seed logic
 */
/**
 * The class for Pokemon, a creature that can fight other pokemon using attacks.
 * They have attacks and natures and types and all that jazz
 * 
 * @author JB227
 */
public class Pokemon {

	// pokedex number relative to being read in
	private int index;
	// species name, unchangeable
	private String name;
	// nickname
	private String nName = "";
	// form name
	private String fName;
	private Gender gender;
	private String gender_balance;
	private int health;
	private int atk;
	private int spAtk;
	private int def;
	private int spDef;
	private int speed;
	private Type type1;
	private Type type2;
	private Type type3 = Type.NONE;
	private Evolution[] evolves = null;
	public static final int NO_OF_ATTACKS = 4;
	private Attack[] attackSet = new Attack[NO_OF_ATTACKS];
	private Map<Integer, String[]> learnedAttacks;
	private String creator = "unknown";
	private String region = "joreli";
	private String nature;
	private float[] natureEffect = new float[5];
	private int[] ivs = new int[6];
	// the stats that can change
	private int rthp;
	// {attack, defence, sp.attack, sp.defence, speed, accuracy, evasiveness}
	private int[] stats_mod = new int[7];
	private boolean critical = false;
	private int level = 1;
	// base stats used
	private int baseHP;
	private int baseAtk;
	private int baseDef;
	private int baseSpAtk;
	private int baseSpDef;
	private int baseSpd;
	// conditions
	private Condition condition = Condition.NONE;
	// uses depend on condition, for example count down for sleep or count up
	// for Badly_Poison damage
	private int conditionCount = 0;
	private int trappedCount = 0;
	private int confuseCount = 0;
	private boolean confused = false;
	private boolean flinch = false;
	private String inLove = null;
	private String trappedIn = null;
	private boolean trapped = false;
	private boolean seeded = false;

	private int repeatCount = 0;
	private String lastAttack = null;
	private Attack nextAttack = null;

	private boolean protect = false;
	private boolean endure = false;
	// used to measure how many times consecutively the pokemon protects itself
	private int protectCount = 0;
	private boolean wait = false;
	private int nomiss = 0;
	// charging attacks
	private Attack charging = null;

	// either {nowhere, sky, ground, water}
	// used by fly, dig and surf etc
	private Battle.Place hidingPlace = Battle.Place.NOWHERE;

	private int physicalDamage = 0;
	private int specialDamage = 0;

	// All default, I'm not changing the constructor again
	private short friendship = 50;

	private Item held = null;
	private int originalTrainer = 0;
	private int stage;

	private int heightInInches;
	private float weightInPounds;

	public enum EggGroup {
		GRASS, BUG, FLYING, HUMAN_LIKE, MONSTER, FAIRY, DRAGON, MINERAL, FIELD, AMORPHOUS, WATER_1, WATER_2, WATER_3, DITTO, UNDISCOVERED, NONE
	}

	private EggGroup eggGroup1;
	private EggGroup eggGroup2;

	/**
	 * MALE, FEMALE or GENDERLESS has a method for opposite genders and (if I get it
	 * working) returns a matching symbol
	 * 
	 * @author JB227
	 *
	 */
	public enum Gender {
		GENDERLESS, MALE, FEMALE;

		public boolean opposite(Gender g) {
			switch (this) {
			case FEMALE:
				return g == MALE;
			case MALE:
				return g == FEMALE;
			default:
				return false;

			}
		}

		public String symbol() {
			switch (this) {
			case FEMALE:
				return "(f)";
			case MALE:
				return "(m)";
			default:
				return "";
			}
		}
	}

	public enum Stats {
		ATK, DEF, SPATK, SPDEF, SPD, ACC, EVA,
		/**
		 * Critical hit definer
		 */
		CTL
	}

	public enum GrowthRate {
		FLUCTUATING, SLOW, S_MEDIUM, MEDIUM, FAST, ERRATIC
	}

	private GrowthRate growth;
	private int currentexp;
	private int nextexp;
	private int base_exp;
	private int capture_rate;

	/**
	 * An attack that all pokemon can use, if they have no other moves available
	 */
	private static final Attack STRUGGLE = new AttackBuilder("Struggle", 50, 100, 1, Type.NORMAL,
			Attack.Category.PHYSICAL).withEffect(new RecoilEffect(50)).build();

	/**
	 * This attack is used by a pokemon on itself when confused
	 */
	private static final Attack CONFUSED = new AttackBuilder("", 40, 0, 1, Type.NORMAL, Attack.Category.PHYSICAL)
			.withDamage(new PlainDamage()).build();

	/**
	 * Returns STRUGGLE, an attack pokemon use when they have no other moves
	 * avaiable
	 * 
	 * @return
	 */
	public static Attack cantAttack() {
		return STRUGGLE.copy();
	}

	/**
	 * Constructor for Pokemon
	 * 
	 * @param n
	 *            Name
	 * @param hp
	 *            Health
	 * @param atk
	 *            Attack stat
	 * @param dfn
	 *            Defence stat
	 * @param sa
	 *            special attack
	 * @param sd
	 *            special defence
	 * @param spd
	 *            Speed
	 * @param t1
	 *            type 1
	 * @param t2
	 *            type 2
	 * @param b
	 *            the pre_evlove used for evolving
	 * @param gender
	 *            a String describing the balance of male to females i.e. "50/50"
	 */
	public Pokemon(int i, String n, int hp, int atk, int dfn, int sa, int sd, int spd, Type t1, Type t2,
			Evolution[] evs, String gender, GrowthRate gr, int capture, int baseExp, int stage, int height,
			float weight, EggGroup egg1, EggGroup egg2) {
		this.index = i;
		this.name = n;
		this.fName = n;

		this.baseHP = hp;
		this.baseAtk = atk;
		this.baseSpAtk = sa;
		this.baseDef = dfn;
		this.baseSpDef = sd;
		this.baseSpd = spd;

		this.type1 = t1;
		this.type2 = t2;
		this.evolves = evs;
		createNature();
		setIVs();
		setStats();

		this.growth = gr;

		this.currentExp();
		this.nextExp();
		this.base_exp = baseExp;
		this.capture_rate = capture;

		this.gender_balance = gender;

		setGender();

		this.learnedAttacks = new HashMap<>();

		this.stage = stage;

		this.heightInInches = height;
		this.weightInPounds = weight;
		this.eggGroup1 = egg1;
		this.eggGroup2 = egg2;
	}

	/**
	 * A slightly simpler constructor, (well, less variables anyway)
	 * 
	 * @param n
	 *            name
	 * @param hp
	 *            max health
	 * @param as
	 *            attack and Sp.attack (they are equal)
	 * @param ds
	 *            defence and Sp.defence (they are equal)
	 * @param s
	 *            speed
	 * @param t1
	 *            type 1
	 * @param t2
	 *            type 2
	 */
	public Pokemon(String n, int hp, int as, int ds, int s, Type t1, Type t2) {
		this(0, n, hp, as, ds, as, ds, s, t1, t2, null, "50/50", GrowthRate.MEDIUM, 45,
				(int) ((hp + as + as + ds + ds + s) * 0.2), 1, 39, 40, EggGroup.UNDISCOVERED, EggGroup.NONE);
	}

	protected static class Evolution {

		protected static enum Method {
			LEVEL, TRADE, ITEM
		};

		private String name;
		private String method;
		private String[] preconds;

		public Evolution(String m, String n, String[] p) {
			this.method = m.replace("_", " ");
			this.name = n;
			this.preconds = new String[p.length];

			for (int i = 0; i < p.length; i++) {
				preconds[i] = p[i].replace("_", " ");
			}
		}

		public String getName() {
			return name;
		}

		public Method getMethod() {
			try {
				Integer.parseInt(method);
				return Method.LEVEL;
			} catch (NumberFormatException e) {
			}

			if (method.equals("Trade")) {
				return Method.TRADE;
			}

			return Method.ITEM;
		}

		public boolean checkEvolve(Pokemon p, Item item, Location place, boolean traded) {

			Method m = this.getMethod();

			if (traded && m != Method.TRADE) {
				return false;
			}

			if (item != null && m != Method.ITEM) {
				return false;
			}

			switch (m) {
			case ITEM:
				if (item == null)
					return false;
				if (!item.getName().equals(method))
					return false;
				break;
			case LEVEL:
				int l = Integer.parseInt(method);
				if (p.getLevel() < l)
					return false;
				break;
			case TRADE:
				if (!traded)
					return false;
				break;
			}
			boolean useItem = false;
			for (String pre : preconds) {
				// parse each, type-case
				String[] peices = pre.split("=");
				switch (peices[0]) {
				case "Gender":
					if (peices[1].equals("Male")) {
						if (!p.male())
							return false;
					} else {
						if (!p.female())
							return false;
					}
					break;

				case "Item":
					if (!p.isHoldingItem(peices[1]))
						return false;
					useItem = true;
					break;

				case "Friendship":
					if (p.getFriendship() < 225)
						return false;
					break;

				case "Time":
					String[] times = peices[1].split("\\|");
					boolean isTime = false;
					Time time = place.getTime();
					for (String t : times) {
						switch (t) {
						case "Morning":
							if (time == Time.MORNING)
								isTime = true;
							break;
						case "Day":
							if (time == Time.DAY)
								isTime = true;
							break;
						case "Evening":
							if (time == Time.EVENING)
								isTime = true;
							break;
						case "Night":
							if (time == Time.NIGHT)
								isTime = true;
							break;
						default:

						}
					}
					if (!isTime)
						return false;
					break;

				case "Area":
					Location.Terrain terrain = place.getTerrain();
					String ter = peices[1];
					switch (terrain) {
					case BUILDING:
						if (!ter.equals("Building"))
							return false;
						break;
					case CAVE:
						if (!ter.equals("Cave"))
							return false;
						break;
					case FOREST:
						if (!ter.equals("Forest"))
							return false;
						break;
					case GRASS:
						if (!ter.equals("Grass"))
							return false;
						break;
					case ICY:
						if (!ter.equals("Icy"))
							return false;
						break;
					case MAGNETIC:
						if (!ter.equals("Magnetic"))
							return false;
						break;
					case UNDERWATER:
						if (!ter.equals("Underwater"))
							return false;
						break;
					case URBAN:
						if (!ter.equals("Urban"))
							return false;
						break;
					case WATER_SURFACE:
						if (!ter.equals("Water_Surface"))
							return false;
						break;
					}
					break;

				case "KnowsAttack":
					if (!p.knowsAttack(peices[1]))
						return false;
					break;
				}
			}
			if (useItem) {
				p.giveItem(null);
			}
			return true;
		}

		public String getMethodDescription() {

			String m = "";
			Method type = getMethod();

			if (type == Method.LEVEL) {
				m += "Level " + ((method.equals("1")) ? "up" : method);
			} else {
				m += method;
			}

			for (String p : preconds) {
				String[] peices = p.split("=");
				switch (peices[0]) {

				case "Gender":
					m += String.format(" %s only", peices[1]);
					break;

				case "Item":
					m += String.format(" holding %s", peices[1]);
					break;

				case "Friendship":
					m += String.format(" with %s friendship", peices[1]);
					break;

				case "Time":
					m += String.format(" at %s", peices[1].replace("|", " or "));
					break;

				case "Area":
					m += String.format(" in a %s area", peices[1]);
					break;

				case "KnowsAttack":
					m += String.format(" knowing %s", peices[1]);
					break;
				case "HasType":
					m += String.format(" knowing a %s type attack", peices[1]);
					break;
				}
			}

			return m;
		}

		public boolean checkGender(Pokemon p) {
			for (String pre : preconds) {
				String[] peices = pre.split("=");
				if (peices[0].equals("Gender")) {
					if (peices[1].equals("Male")) {
						if (!p.male())
							return false;
					} else {
						if (!p.female())
							return false;
					}
				}
			}
			return true;
		}
	}

	private void setGender() {
		// parse the gender_balance
		if (gender_balance.equals("0")) {
			this.gender = Gender.GENDERLESS;
			return;
		}
		String[] vals = gender_balance.split("/");
		if (vals.length != 2) {
			this.gender = Gender.GENDERLESS;
			return;
		}
		String m = vals[0];
		String f = vals[1];
		float male = Float.valueOf(m);
		float female = Float.valueOf(f);
		if (male == 0)
			this.gender = Gender.FEMALE;
		if (female == 0)
			this.gender = Gender.MALE;
		if (Math.random() * (male + female) > male) {
			this.gender = Gender.FEMALE;
		} else {
			this.gender = Gender.MALE;
		}
	}

	public void setLevel(int l) {
		if (l < 1) {
			l = 1;
		}
		if (l > 100) {
			l = 100;
		}

		this.level = l;

		this.currentExp();
		this.nextExp();

		setStats();
	}

	/**
	 * Gives the pokemon more exp
	 * 
	 * @param exp
	 *            how much
	 * @return true if the pokemon changes level. WARNING! this only increases the
	 *         level by 1, so loop if necessary
	 */
	public boolean gainExp(int exp) {
		if (level < 100) {
			currentexp += exp;
			if (currentexp >= nextexp) {
				level += 1;
				makeFriends((short) 15);
				setStats();
				if (level == 100)
					currentexp = nextexp;
				else
					nextExp();
				return true;
			}
		}
		return false;
	}

	private void setStats() {

		double ratio = this.rthp / (double) health;

		this.health = (int) (10 + this.level + (this.level / 100.f * (ivs[0] + (2 * this.baseHP))));

		// (((IV + 2*base (+ EV/4)) * level/100) + 5) * nature
		this.atk = (int) (this.natureEffect[0] * (5 + (level / 100.f * (ivs[1] + (2 * this.baseAtk)))));
		this.def = (int) (this.natureEffect[1] * (5 + (level / 100.f * (ivs[2] + (2 * this.baseDef)))));
		this.spAtk = (int) (this.natureEffect[2] * (5 + (level / 100.f * (ivs[3] + (2 * this.baseSpAtk)))));
		this.spDef = (int) (this.natureEffect[3] * (5 + (level / 100.f * (ivs[4] + (2 * this.baseSpDef)))));
		this.speed = (int) (this.natureEffect[4] * (5 + (level / 100.f * (ivs[5] + (2 * this.baseSpd)))));

		this.rthp = (int) (health * ratio);
		if (rthp == 0)
			rthp = health;

		for (int i = 0; i < this.stats_mod.length; i++) {
			this.stats_mod[i] = 0;
		}
		this.critical = false;
		this.confused = false;
		this.flinch = false;
		this.protect = false;
		this.protectCount = 0;
	}

	/**
	 * Generates an array describing the effects of a nature
	 */
	private void createNature() {
		Random rand = new Random();
		int inc = rand.nextInt(5);
		int dec = rand.nextInt(5);
		for (int d = 0; d < natureEffect.length; d++) {
			this.natureEffect[d] = 1;
			if (d == inc && d != dec) {
				this.natureEffect[d] = 1.1f;
			}
			if (d == dec && d != inc) {
				this.natureEffect[d] = 0.9f;
			}
		}
		this.nature = NatureGen.nameNature(this.natureEffect);
	}

	/**
	 * Generates the IVs. Each is a number between 0 and 31
	 */
	private void setIVs() {
		for (int i = 0; i < ivs.length; i++) {
			ivs[i] = (int) Math.round((Math.random() * 31));
		}
	}

	private void currentExp() {
		this.currentexp = calcExp(this.level);
	}

	private void nextExp() {
		this.nextexp = calcExp(this.level + 1);
	}

	private int calcExp(int level) {
		if (level == 1)
			return 1;
		int exp = 0;
		switch (this.growth) {
		// the fastest
		case ERRATIC:
			if (level < 50) {
				exp = (int) (Math.pow(level, 3) * (100 - level / 50.f));
			} else if (level < 68) {
				exp = (int) (Math.pow(level, 3) * ((150 - level) / 100.f));
			} else if (level < 98) {
				exp = (int) (Math.pow(level, 3) * (((1911 - (10 * level)) / 3) / 500));
			} else {
				exp = (int) (Math.pow(level, 3) * ((160 - level) / 100));
			}
			break;
		case FAST:
			exp = (int) Math.round(0.8 * Math.pow(level, 3));
			break;
		case MEDIUM:
			exp = (int) Math.pow(level, 3);
			break;
		case S_MEDIUM:
			// a fairly complex cubic equation
			exp = (int) Math.round((1.2 * Math.pow(level, 3)) - (15 * Math.pow(level, 2)) + (100 * level) - 140);
			break;
		case SLOW:
			exp = (int) Math.round(1.25 * Math.pow(level, 3));
			break;
		// the slowest
		case FLUCTUATING:
			if (level < 15) {
				exp = (int) (Math.pow(level, 3) * ((24 + ((level + 1.f) / 3)) / 50));
			} else if (level < 36) {
				exp = (int) (Math.pow(level, 3) * (level + 14 / 50.f));
			} else {
				exp = (int) (Math.pow(level, 3) * ((32 + (level / 2.f)) / 50));
			}
			break;
		}
		return exp;
	}

	protected void setIVs(int[] ivs) {
		if (ivs.length == 6)
			this.ivs = ivs;
	}

	public int[] getIvs() {
		return this.ivs;
	}

	public String getNature() {
		return this.nature;
	}

	public void setNature(String n) {
		this.nature = n;
		this.natureEffect = NatureGen.createNature(n);
		setStats();
	}

	/**
	 * Sets the learned attacks
	 * 
	 * @param atks
	 *            a map of level{Integer} to attacks{String[]}
	 */
	public void setLearnedAttacks(Map<Integer, String[]> atks) {
		this.learnedAttacks.putAll(atks);
	}

	/**
	 * Sets the learned attacks by decoding a String array
	 * 
	 * @param atks
	 *            an array of "level{cast to int}:attack(,attack)*"
	 */
	public void setLearnedAttacks(String[] atks) {
		for (String a : atks) {
			String[] level_attack = a.split(":");
			Integer lvl = Integer.valueOf(level_attack[0]);
			String[] attacks = level_attack[1].split(",");
			for (int i = 0; i < attacks.length; i++) {
				attacks[i] = attacks[i].replace('_', ' ');
			}
			learnedAttacks.put(lvl, attacks);
		}
	}

	public String[] getLearnedAttacks() {
		return getLearnedAttacks(this.getLevel());
	}

	/**
	 * @return the attacks learnt the current pokemon level
	 */
	public String[] newAttacks() {
		return this.newAttacks(this.level);
	}

	/**
	 * @param what
	 *            level you are enquiring
	 * @return the attacks learnt at this level
	 */
	public String[] newAttacks(int lvl) {
		if (!learnedAttacks.containsKey(lvl))
			return new String[] {};
		return learnedAttacks.get(lvl);
	}

	/**
	 * Get the list of attacks that should have been learnt by a particular level
	 * 
	 * @param lvl
	 *            what level
	 * @return String[] of attacks, may contain null values
	 */
	public String[] getLearnedAttacks(int lvl) {
		String[] atks = new String[NO_OF_ATTACKS];
		int i = 0;

		while (i < NO_OF_ATTACKS && lvl > 0) {
			if (learnedAttacks.containsKey(lvl)) {
				String[] newAtks = learnedAttacks.get(lvl);
				for (String a : newAtks) {
					atks[i++] = a;
					if (i == NO_OF_ATTACKS)
						break;
				}
			}
			lvl -= 1;
		}
		return atks;
	}

	/**
	 * Allows modification in certain circumstances i.e. different forms
	 * 
	 * @param level
	 *            what level
	 * @param attack
	 *            what attack to learn
	 */
	public void newLearnedAttack(int level, String attack) {
		learnedAttacks.put(level, new String[] { attack });
	}

	// accessors
	public String getName() {
		return (nName.equals("")) ? this.name : this.nName;
	}

	public int getNumber() {
		return index;
	}

	/**
	 * 
	 * @param g
	 *            if true, the gender is indicated as well
	 * @return
	 */
	public String getName(boolean g) {
		String gender = "";
		if (g)
			gender += " " + this.gender.symbol();
		return ((nName.equals("")) ? this.name : this.nName + " (" + this.name + ")") + gender;
	}

	public String getSpecies() {
		return this.name;
	}

	public String getNickName() {
		return this.nName;
	}

	public Gender getGender() {
		return this.gender;
	}

	public boolean male() {
		return this.gender == Gender.MALE;
	}

	public boolean female() {
		return this.gender == Gender.FEMALE;
	}

	public boolean genderless() {
		return this.gender == Gender.GENDERLESS;
	}

	public String getGenderBalance() {
		return this.gender_balance;
	}

	// base
	public int getBaseHealth() {
		return this.baseHP;
	}

	public int getBaseAtk() {
		return this.baseAtk;
	}

	public int getBaseDef() {
		return this.baseDef;
	}

	public int getBaseSpAtk() {
		return this.baseSpAtk;
	}

	public int getBaseSpDef() {
		return this.baseSpDef;
	}

	public int getBaseSpd() {
		return this.baseSpd;
	}

	public int getTotal() {
		return this.baseHP + this.baseAtk + this.baseDef + this.baseSpAtk + this.baseSpDef + this.baseSpd;
	}

	public GrowthRate getGrowthRate() {
		return this.growth;
	}

	// max stats
	public int getHealthMax() {
		return this.health;
	}

	public int getAtkMax() {
		return this.atk;
	}

	public int getSpAtkMax() {
		return this.spAtk;
	}

	public int getDefMax() {
		return this.def;
	}

	public int getSpDefMax() {
		return this.spDef;
	}

	public int getSpeedMax() {
		return this.speed;
	}

	// changing
	public int getHealth() {
		return this.rthp;
	}

	public int getPhysicalDamage() {
		return this.physicalDamage;
	}

	public int getSpecialDamage() {
		return this.specialDamage;
	}

	private float stat_mod(int ind) {
		int base = (ind < 5) ? 2 : 3;
		float den = (float) base;
		int lvl = Math.abs(stats_mod[ind]) + base;
		float mul = (stats_mod[ind] > 0) ? lvl / den : den / lvl;
		return mul;
	}

	public int getAtk() {
		float mul = stat_mod(0);
		if (isBurned())
			mul *= 0.5f;
		return (int) (this.atk * mul);
	}

	public int getDef() {
		return (int) (this.def * stat_mod(1));
	}

	public int getSpAtk() {
		return (int) (this.spAtk * stat_mod(2));
	}

	public int getSpDef() {
		return (int) (this.spDef * stat_mod(3));
	}

	public int getSpeed() {
		float mul = stat_mod(4);
		if (isParalysed())
			mul *= 0.5f;
		return (int) (this.speed * mul);
	}

	public float getAccuracy() {
		return 100 * stat_mod(5);
	}

	public float getEvasiveness() {
		return 100 * stat_mod(6);
	}

	public boolean critical() {
		return critical;
	}

	public int getLevel() {
		return level;
	}

	public int getCurrentExp() {
		return currentexp;
	}

	public int getNextExp() {
		return nextexp;
	}

	public Type getType1() {
		return this.type1;
	}

	public Type getType2() {
		return this.type2;
	}

	public Type getType3() {
		return this.type3;
	}

	public int getStage() {
		return stage;
	}

	// equivalent to Pokemon cards
	public boolean isBaby() {
		return stage == 0;
	}

	public boolean isBasic() {
		return stage == 1;
	}

	public boolean isStage1() {
		return stage == 2;
	}

	public boolean isStage2() {
		return stage == 3;
	}

	public Evolution[] getEvolves() {
		return this.evolves;
	}

	public Map<String, String> getEvolvesStringMap() {

		if (evolves == null)
			return null;

		Map<String, String> evos = new HashMap<>();

		for (Evolution e : evolves)
			evos.put(e.getName(), e.getMethodDescription());

		return evos;
	}

	// If can't attack, return Struggle
	public Attack[] getAtkSet() {
		if (!this.tired())
			return this.attackSet;
		else
			return new Attack[] { STRUGGLE.copy() };
	}

	public String whoMade() {
		return this.creator;
	}

	public String home() {
		return region;
	}

	public int getBaseExp() {
		return this.base_exp;
	}

	public int getCaptureRate() {
		return this.capture_rate;
	}

	public int getHeightInInches() {
		return heightInInches;
	}

	public float getHeightInMetres() {
		return (float) (heightInInches) * 0.0254f;
	}

	public float getWeightInPounds() {
		return weightInPounds;
	}

	public float getWeightInKilos() {
		return weightInPounds * 0.453592f;
	}

	public int expYield() {
		return (base_exp * level) / 7;
	}

	public EggGroup getEggGroup1() {
		return eggGroup1;
	}

	public EggGroup getEggGroup2() {
		return eggGroup2;
	}

	public boolean isEggGroup(EggGroup e) {
		if (e == eggGroup1) {
			return true;
		}
		if (e != EggGroup.NONE && e == eggGroup2) {
			return true;
		}
		return false;
	}

	public boolean eggMatch(Pokemon p) {
		if (this.isEggGroup(EggGroup.UNDISCOVERED) || p.isEggGroup(EggGroup.UNDISCOVERED))
			return false;

		if (this.isEggGroup(EggGroup.DITTO) || p.isEggGroup(EggGroup.DITTO))
			return true;

		if (!this.isEggGroup(p.getEggGroup1()) && !this.isEggGroup(p.getEggGroup2())) {
			return false;
		}

		if (!this.gender.opposite(p.getGender()) && !genderless()) {
			return false;
		}
		return true;
	}

	public Item getItem() {
		return this.held;
	}

	public boolean giveItem(Item item) {
		if (this.held != null && item != null)
			return false;

		this.held = item;
		return true;
	}

	public Item takeItem() {
		Item i = this.held;
		this.held = null;
		return i;
	}

	public boolean useItem(Item item, Location place) {
		return item.use(this, place);
	}

	public boolean isHoldingItem() {
		return this.held != null;
	}

	public boolean isHoldingItem(String item) {
		if (isHoldingItem()) {
			return this.held.getName().equals(item);
		}
		return false;
	}

	public boolean isHoldingItem(Item i) {
		return isHoldingItem(i.getName());
	}

	/**
	 * Sets a status condition
	 * 
	 * @param c
	 *            the condition, of type enum
	 * @param n
	 *            A description of the cause, used by some conditions, but not
	 *            others. If the condition is infatuation, for example, this is the
	 *            target of the pokemon's affections
	 * @return true if successful, namely the pokemon hasn't already got a condition
	 */
	public boolean giveCondition(Condition c, String n, Battle arena) {

		// conditions controlled by booleans
		if (c == Condition.CONFUSE) {
			if (isConfused())
				return false;
			arena.log(getName() + " was confused!");
			this.confused = true;
			// count out 1-4 turns
			this.confuseCount = 1 + (int) Math.round(Math.random() * 3);
			if (held != null && held.getCategory() == Item.Category.BERRY) {
				if (((Berry) held).eat(this, arena)) {
					held = null;
				}
			}
			return true;
		}
		if (c == Condition.FLINCH) {
			this.flinch = true;
			return true;
		}
		if (c == Condition.INFATUATION) {
			if (inLove != null) {
				this.inLove = n;
				arena.log(getName() + " fell in love with " + n + "!");
				return true;
			}
			return false;
		}
		if (c == Condition.ENTRAP) {
			if (trappedIn == null) {
				this.trappedIn = n;
				this.trappedCount = 4 + (int) Math.round(Math.random());
				trapped = true;
				arena.log(getName() + " trapped by " + n + "!");
				return true;
			}
			return false;
		}
		if (c == Condition.SEEDED) {
			// grass types not effected by leech seed ;)
			if (!typeMatch(Type.GRASS) && !seeded) {
				seeded = true;
				arena.log(getName() + " was seeded!");
				return true;
			}
			return false;
		}
		if (c == Condition.CANT_ESCAPE) {
			if (!trapped) {
				trapped = true;
				arena.log(getName() + " can no longer escape!");
				return true;
			}
			return false;
		}

		// already got a condition?
		if (this.condition != Condition.NONE)
			return false;

		// Type defences
		switch (c) {
		case POISON:
			arena.log(getName() + " was poisoned!");
		case BAD_POISON:
			if (typeMatch(Type.POISON) || typeMatch(Type.STEEL))
				return false;
			break;
		case FREEZE:
			if (typeMatch(Type.ICE))
				return false;
			arena.log(getName() + " was frozen!");
			break;
		case BURN:
			if (typeMatch(Type.FIRE))
				return false;
			arena.log(getName() + " was burned!");
			break;

		case PARALYSE:
			if (typeMatch(Type.ELECTRIC))
				return false;
			arena.log(getName() + " was paralysed!");
			break;

		default:
			break;
		}
		this.condition = c;

		if (this.condition == Condition.BAD_POISON) {
			this.conditionCount = 1;
			arena.log(getName() + " was badly poisoned!");
		}
		if (this.condition == Condition.SLEEP) {
			arena.log(getName() + " fell asleep!");
			this.confused = false;
			this.inLove = null;
			if (n.equals("self"))
				this.conditionCount = 2;
			else
				this.conditionCount = 1 + (int) Math.round(Math.random() * 6);
		}

		if (held != null && held.getCategory() == Item.Category.BERRY) {
			if (((Berry) held).eat(this, arena)) {
				held = null;
			}
		}

		return true;
	}

	public boolean isPoisoned() {
		return this.condition == Condition.POISON;
	}

	public boolean isBadlyPoisoned() {
		return this.condition == Condition.BAD_POISON;
	}

	public boolean isBurned() {
		return this.condition == Condition.BURN;
	}

	public boolean isAsleep() {
		return this.condition == Condition.SLEEP;
	}

	public boolean isParalysed() {
		return this.condition == Condition.PARALYSE;
	}

	public boolean isFrozen() {
		return this.condition == Condition.FREEZE;
	}

	public boolean isConfused() {
		return this.confused;
	}

	public boolean isSeeded() {
		return this.seeded;
	}

	public boolean hasFlinched() {
		if (this.flinch) {
			this.flinch = false;
			return true;
		}
		return false;
	}

	public boolean isInfatuated() {
		return this.inLove != null;
	}

	public boolean isFine() {
		return this.condition == Condition.NONE && !this.confused;
	}

	public boolean isTrapped() {
		return this.trappedIn != null;
	}

	public boolean canEscape() {
		return !trapped;
	}

	/**
	 * The opposite to giveCondition, allows a 'fix' to be applied.
	 * 
	 * @param f
	 *            an enum describing the fix
	 * @return true if the fix has an effect i.e. applying Thaw to Burn -> False,
	 *         but Thaw + Freeze -> True
	 */
	public boolean giveFix(Condition.Fix f, Battle arena) {
		// healing case
		switch (f) {
		case ANTIDOTE:
			if (this.isPoisoned() || this.isBadlyPoisoned()) {
				this.condition = Condition.NONE;
				arena.log(name + " was cured of it's poisoning!");
				makeFriends((short) 5);
				return true;
			}
			return false;
		case COOL:
			if (this.isBurned()) {
				this.condition = Condition.NONE;
				arena.log(name + " was cured of it's burn!");
				makeFriends((short) 5);
				return true;
			}
			return false;
		case FALL_OUT:
			if (this.isInfatuated()) {
				arena.log(name + " is no longer in love with " + inLove + "!");
				this.inLove = null;
				return true;
			}
			return false;
		case FLEX:
			if (this.isParalysed()) {
				this.condition = Condition.NONE;
				arena.log(name + " was cured of it's paralysis!");
				makeFriends((short) 5);
				return true;
			}
			return false;
		case FULL:
			this.condition = Condition.NONE;
			this.confused = false;
			this.conditionCount = 0;
			this.confuseCount = 0;
			this.seeded = false;
			this.flinch = false;
			this.inLove = null;
			arena.log(name + " was cured of all status conditions!");
			makeFriends((short) 5);
			return true;
		case SNAP_OUT:
			if (this.isConfused()) {
				this.confused = false;
				arena.log(name + " was cured of it's confusion!");
				makeFriends((short) 5);
				return true;
			}
			return false;
		case THAW:
			if (this.isFrozen()) {
				this.condition = Condition.NONE;
				arena.log(name + " was thawed!");
				return true;
			}
			return false;
		case WAKE:
			if (this.isAsleep()) {
				this.condition = Condition.NONE;
				arena.log(name + " woke up!");
				return true;
			}
			return false;
		case BREAK_OUT:
			trapped = false;
			if (this.isTrapped()) {
				arena.log("The " + trappedIn + " died down.");
				this.trappedIn = null;
				return true;
			}
			return false;
		case ESCAPE:
			this.trapped = false;
			return true;
		default:
			return false;

		}
	}

	/**
	 * Checks a Pokemon has a given Condition
	 * 
	 * @param c
	 *            a Condition enum thing
	 * @return true if the Pokemon is afflicted with the given condition
	 */
	public boolean hasCondition(Condition c) {
		switch (c) {
		case BAD_POISON:
			return isBadlyPoisoned();
		case BURN:
			return isBurned();
		case CONFUSE:
			return isConfused();
		case FLINCH:
			return hasFlinched();
		case FREEZE:
			return isFrozen();
		case NONE:
			return isFine();
		case PARALYSE:
			return isParalysed();
		case POISON:
			return isPoisoned();
		case SLEEP:
			return isAsleep();
		case INFATUATION:
			return isInfatuated();
		case ENTRAP:
			return isTrapped();
		case CANT_ESCAPE:
			return !canEscape();
		case SEEDED:
			return isSeeded();
		}
		return false;
	}

	/**
	 * Calls on the pokemon to protect itself. Fails if used repeatedly
	 * 
	 * @return True if it has worked, false otherwise
	 */
	public boolean protect() {
		double level = Math.pow(0.5, protectCount);
		this.protect = Math.random() <= level;
		if (protect)
			this.protectCount += 2;
		return protect;
	}

	public boolean isProtecting() {
		if (this.protectCount > 0)
			this.protectCount -= 1;
		if (protect) {
			protect = false;
			return true;
		}
		return protect;
	}

	public boolean endure() {
		double level = Math.pow(0.5, protectCount);
		this.endure = Math.random() <= level;
		if (endure)
			this.protectCount += 2;
		return endure;
	}

	public boolean hasEndured() {
		return endure;
	}

	/**
	 * The pokemon cannot attack, sets isResting
	 */
	public void notAttack() {
		this.wait = true;
	}

	public boolean cantMove() {
		return this.wait;
	}

	public boolean isResting() {
		if (wait) {
			wait = false;
			return true;
		}
		return wait;
	}

	public void aim() {
		nomiss = 2;
	}

	/**
	 * The pokemon cannot miss if this returns true
	 * 
	 * @return
	 */
	public boolean nomiss() {
		return nomiss > 0;
	}

	public void endAim() {
		nomiss = 0;
	}

	/**
	 * Two turn attacks
	 * 
	 * @return
	 */
	public Attack chargingAttack() {
		return charging;
	}

	public void setCharging(Attack c) {
		charging = c;
	}

	public boolean isCharging() {
		return charging != null;
	}

	public void callAttack(Attack a) {
		this.nextAttack = a;
	}

	public Attack nextAttack() {
		Attack a = this.nextAttack;
		this.nextAttack = null;
		return a;
	}

	public Attack peekNextAttack() {
		return nextAttack;
	}

	/**
	 * Disappear before attacking
	 * 
	 * @param s
	 */
	public void hide(Battle.Place s) {
		this.hidingPlace = s;
	}

	public Battle.Place hidingPlace() {
		return hidingPlace;
	}

	/**
	 * Checks if a pokemon can currently attack
	 * 
	 * @param print
	 *            Print anything that is flagged
	 * @return true if the pokemon can attack
	 */
	public boolean canAttack(Attack a, Battle arena) {
		if (this.fainted()) {
			return false;
		}

		if (switched) {
			switched = false;
			return false;
		}
		if (isRunning()) {
			return false;
		}

		if (this.isResting()) {
			arena.log(getName() + " cannot attack!");
			return false;
		}
		if (this.hasFlinched()) {
			arena.log(getName() + " flinched!");
			return false;
		}
		if (this.isAsleep()) {
			if (a.getTiming().canUseWithCondition(condition)) {
				return true;
			}
			if (this.conditionCount > 0) {
				this.conditionCount--;
				arena.log(getName() + " is asleep!");
				return false;
			} else {
				this.condition = Condition.NONE;
				arena.log(getName() + " woke up!");
				return true;
			}
		}
		if (this.isFrozen()) {
			if (a.getTiming().canUseWithCondition(condition)) {
				return true;
			}
			if (Math.random() > 0.2) {
				arena.log(getName() + " is frozen!");
				return false;
			} else {
				this.condition = Condition.NONE;
				arena.log(getName() + " thawed out!");
				return true;
			}
		}
		if (this.isParalysed()) {
			arena.log(getName() + " is paralysed!");
			if (Math.random() < 0.25) {
				arena.log(getName() + " can't move!");
				return false;
			}
		}
		if (this.isConfused()) {
			arena.log(getName() + " is confused!");
			if (this.confuseCount > 0) {
				this.confuseCount--;
				if (Math.random() < 0.5) {
					arena.log(getName() + " hurt itself in it's confusion!");
					CONFUSED.getDamageEffect().harm(this, CONFUSED, this, arena);
					return false;
				}
			} else {
				this.condition = Condition.NONE;
				arena.log(getName() + " snapped out of it's confusion!");
				return true;
			}
		}
		if (this.isInfatuated()) {
			if (Math.random() < 0.5) {
				arena.log(getName() + " is immobilised by love!");
				return false;
			}
		}
		return true;
	}

	/**
	 * Does other damage to the pokemon, such as poison, weather related etc<br>
	 * Also resets one-turn effects
	 * 
	 * @param w
	 * @return true if damage accrued
	 */
	public boolean damagingFactors(Battle arena) {

		this.protect = false;
		this.flinch = false;
		this.physicalDamage = 0;
		this.specialDamage = 0;
		this.switched = false;
		if (nomiss > 0)
			this.nomiss--;

		if (fainted())
			return false;

		boolean damaged = false;
		if (this.isPoisoned()) {
			arena.log(getName() + " is hurt by poison!");
			int pain = (int) ((float) getHealthMax() * 0.125);
			this.damage(pain, Attack.Category.STATUS, arena);
			if (this.fainted()) {
				makeFriends((short) -10);
				return true;
			}
			damaged = true;
		}
		if (this.isBadlyPoisoned()) {
			arena.log(getName() + " is hurt by poison!");
			int pain = (int) ((float) getHealthMax() * (conditionCount / 16.f));
			this.damage(pain, Attack.Category.STATUS, arena);
			if (this.fainted()) {
				makeFriends((short) -10);
				return true;
			}
			this.conditionCount++;
			damaged = true;
		}
		if (this.isBurned()) {
			arena.log(getName() + " is hurt its burn!");
			int pain = (int) ((float) getHealthMax() * 0.0625);
			this.damage(pain, Attack.Category.STATUS, arena);
			if (this.fainted()) {
				makeFriends((short) -10);
				return true;
			}
			damaged = true;
		}
		if (this.isTrapped()) {
			if (this.trappedCount > 0) {
				arena.log(getName() + " is hurt by " + this.trappedIn + "!");
				int pain = (int) ((float) getHealthMax() * 0.0625);
				this.damage(pain, Attack.Category.STATUS, arena);
				if (fainted()) {
					return true;
				}
				damaged = true;
				this.trappedCount--;
			} else {
				arena.log("The " + this.trappedIn + " stopped");
				this.trappedIn = null;
			}
		}
		if (arena.getWeather() == Weather.HAIL) {
			if (!this.typeMatch(Type.ICE)
					&& (this.hidingPlace == Battle.Place.NOWHERE || this.hidingPlace == Battle.Place.SKY)) {
				arena.log(getName() + " is buffeted by hail!");
				int pain = (int) ((float) getHealthMax() * 0.125);
				this.damage(pain, Attack.Category.STATUS, arena);
				if (fainted()) {
					return true;
				}
				damaged = true;
			}
		}
		if (arena.getWeather() == Weather.SANDSTORM) {
			if (!(this.typeMatch(Type.GROUND) || this.typeMatch(Type.ROCK) || this.typeMatch(Type.STEEL))
					&& (this.hidingPlace == Battle.Place.NOWHERE || this.hidingPlace == Battle.Place.SKY)) {
				arena.log(getName() + " is buffeted by the sandstorm!");
				int pain = (int) ((float) getHealthMax() * 0.125);
				this.damage(pain, Attack.Category.STATUS, arena);
				if (fainted()) {
					return true;
				}
				damaged = true;
			}
		}

		this.endure = false;

		return damaged;
	}

	public boolean oppositeGenders(Pokemon p) {
		return this.gender.opposite(p.getGender());
	}

	public Attack getAttack(int n) {
		if (n >= 0 || n < attackSet.length)
			return this.attackSet[n];
		return null;
	}

	/**
	 * Use an attack an a pokemon in a battle scenario
	 * 
	 * @param attack
	 * @param defender
	 * @param arena
	 */
	public void attack(Attack attack, Pokemon defender, Battle arena) {
		if (attack.getName().equals(lastAttack)) {
			this.repeatCount += 1;
		} else {
			this.repeatCount = 1;
			this.lastAttack = attack.getName();
		}
		attack.getTiming().use(this, attack, defender, arena);
		if (attack.exhausted() && held != null && held.getCategory() == Item.Category.BERRY) {
			if (((Berry) held).eat(this, arena)) {
				held = null;
			}
		}
	}

	public int consecutiveTurnsUsed(String attack) {
		if (attack.equals(lastAttack))
			return this.repeatCount;
		return 0;
	}

	public void stopCountingConsecutiveUses() {
		this.repeatCount = 1;
	}

	/**
	 * Reveals the Pokemon's weaknesses
	 * 
	 * @return the Weaknesses
	 */
	public String getWeaknesses() {
		TypeCompare weak = new TypeCompare();
		return weak.typeComparison(this, "weakness");
	}

	public String getTypeEffects() {

		TypeCompare t = new TypeCompare();
		String e = "";
		e += "Weakness: " + t.typeComparison(this, "weak") + "\n";
		e += "Resistance: " + t.typeComparison(this, "res") + "\n";
		e += "No Effect: " + t.typeComparison(this, "no effect") + "\n";

		return e;
	}

	public boolean setAtk(Attack a, int n) {
		if (n >= 0 && n < NO_OF_ATTACKS) {
			this.attackSet[n] = a;
			return true;
		}
		return false;
	}

	public boolean learnAttack(Attack a) {
		if (knowsAttack(a.getName()))
			return false;
		for (int i = 0; i < NO_OF_ATTACKS; i++) {
			if (attackSet[i] == null) {
				attackSet[i] = a;
				return true;
			}
		}
		return false;
	}

	public boolean knowsAttack(String a) {
		for (int i = 0; i < NO_OF_ATTACKS; i++) {
			if (attackSet[i] != null) {
				if (attackSet[i].getName().equals(a))
					return true;
			}
		}
		return false;
	}

	public boolean canLearn(String a) {
		Collection<String[]> atks = this.learnedAttacks.values();
		for (String[] as : atks) {
			for (String at : as) {
				if (at.equals(a))
					return true;
			}
		}
		return false;
	}

	public int originalTrainer() {
		return this.originalTrainer;
	}

	public void setTrainer(Trainer t) {
		this.originalTrainer = t.getTrainerId();
	}

	public void setCreator(String n) {
		this.creator = n;
	}

	public void location(String r) {
		this.region = r;
	}

	public void setType3(Type t3) {
		this.type3 = t3;
	}

	public boolean setGender(Gender g) {

		// Sanity check on species genders
		if (gender_balance.equals("0") || g == Gender.GENDERLESS)
			return false;
		String[] vals = gender_balance.split("/");
		if (vals.length != 2)
			return false;
		if (vals[0].equals("0") && g == Gender.MALE)
			return false;
		if (vals[1].equals("0") && g == Gender.FEMALE)
			return false;

		this.gender = g;
		return true;
	}

	public void setNickName(String n) {
		this.nName = n;
	}

	public void setFormName(String f) {
		this.fName = f;
	}

	public String getFormName() {
		return fName;
	}

	public void setFriendship(short f) {
		this.friendship = f;
	}

	public short getFriendship() {
		return this.friendship;
	}

	public void makeFriends(short f) {
		this.friendship += f;

		if (friendship > 255) {
			friendship = 255;
		}
		if (friendship < 0) {
			friendship = 0;
		}
	}

	/**
	 * Used to signify what Type attack they want
	 * 
	 * @return One of this Pokemon's Types
	 */
	public Type preference() {
		if (type2 == Type.NONE) {
			return type1;
		}

		if (type3 == Type.NONE) {
			if (Math.random() > 0.5)
				return type1;
			return type2;
		}

		double t = Math.random();
		if (t > 0.66666666666666667)
			return type1;
		if (t > 0.33333333333333333)
			return type2;
		return type3;
	}

	/**
	 * Is a type the same as one of the Pokmeon's
	 * 
	 * @param type
	 *            the type
	 * @return
	 */
	public boolean typeMatch(Type type) {
		if (type == this.type1)
			return true;
		if (type == this.type2)
			return true;
		if (this.type3 != Type.NONE && type == this.type3)
			return true;
		return false;
	}

	/**
	 * Makes the Pokemon standard, any individual strengths and/or weaknesses are
	 * wiped
	 */
	public void normalise() {
		for (int d = 0; d < natureEffect.length; d++) {
			this.natureEffect[d] = 1;
		}
		for (int i = 0; i < ivs.length; i++) {
			this.ivs[i] = 0;
		}
		this.nature = "";
		this.setStats();
	}

	public void damage(int ouch) {
		damage(ouch, Attack.Category.STATUS, new Battle());
	}

	/**
	 * The pokemon loses some health
	 * 
	 * @param ouch
	 *            the amount of damaged caused
	 * @param type
	 *            the category of the damaging attack
	 */
	public int damage(int ouch, Attack.Category type, Battle arena) {

		Battle.Position pos = arena.positionOfPokemon(this);
		if (arena.defenceSet(pos, Battle.Defence.REFLECT) && type == Attack.Category.PHYSICAL) {
			ouch /= 2;
		}
		if (arena.defenceSet(pos, Battle.Defence.SCREEN) && type == Attack.Category.SPECIAL) {
			ouch /= 2;
		}

		if (this.rthp >= ouch)
			this.rthp -= ouch;
		else if (endure) {
			ouch = rthp - 1;
			this.rthp = 1;
			arena.log(getName() + " endured the hit!");
		} else {
			ouch = rthp;
			this.rthp = 0;
		}

		switch (type) {
		case PHYSICAL:
			this.physicalDamage += ouch;
			break;
		case SPECIAL:
			this.specialDamage += ouch;
			break;
		default:
			break;
		}

		if (held != null && held.getCategory() == Item.Category.BERRY) {
			Berry item = (Berry) held;
			if (item.eat(this, arena)) {
				held = null;
			}
		}

		if (rthp == 0) {
			makeFriends((short) -5);
			int m = arena.getLogMode();
			arena.setLogMethod(Battle.NONE);
			this.giveFix(Condition.Fix.FULL, arena);
			arena.setLogMethod(m);
			switchOut();
		}
		return ouch;
	}

	/**
	 * Gain some health
	 * 
	 * @param h
	 */
	public void heal(int h) {
		this.rthp += h;
		if (rthp > getHealthMax())
			this.rthp = this.getHealthMax();
	}

	/**
	 * Has the pokemon lost all its health?
	 * 
	 * @return
	 */
	public boolean fainted() {
		return (rthp <= 0);
	}

	/**
	 * The pokemon cannot attack
	 * 
	 * @return true if ^
	 */
	public boolean tired() {
		for (Attack a : attackSet) {
			if (a != null && !a.exhausted())
				return false;
		}
		return true;
	}

	/**
	 * Fully heal the pokemon
	 */
	public void revive(Battle arena) {
		this.rthp = this.getHealthMax();
		this.giveFix(Condition.Fix.FULL, arena);
		this.restore();
	}

	/**
	 * Resets the Pokemon's attacks
	 */
	public void restore() {
		for (Attack a : attackSet) {
			if (a != null)
				a.recharge();
		}
	}

	/**
	 * Resets the stats
	 */
	public void resetStats() {

		for (int i = 0; i < stats_mod.length; i++) {
			this.stats_mod[i] = 0;
		}
		this.critical = false;
		this.confused = false;
		this.flinch = false;
		this.protect = false;
		this.trapped = false;
		this.trappedIn = null;
		this.inLove = null;
		this.protectCount = 0;
		this.charging = null;
		this.hidingPlace = Battle.Place.NOWHERE;
		this.lastAttack = null;
		this.nextAttack = null;
		this.wait = false;
		this.repeatCount = 0;
		this.nomiss = 0;

	}

	public void switchOut() {
		this.critical = false;
		this.confused = false;
		this.flinch = false;
		this.protect = false;
		this.endure = false;
		this.seeded = false;
		this.trapped = false;
		this.trappedIn = null;
		this.inLove = null;
		this.protectCount = 0;
		this.charging = null;
		this.hidingPlace = Battle.Place.NOWHERE;
		this.lastAttack = null;
		this.nextAttack = null;
		this.wait = false;
		this.repeatCount = 0;
		this.nomiss = 0;
	}

	public void switchIn() {
		switched = true;
	}

	public boolean hasSwitched() {
		return switched;
	}

	private boolean switched = false;
	private boolean flee = false;

	public void run() {
		flee = true;
	}

	public boolean isRunning() {
		return flee;
	}

	public void ecsape() {
		flee = false;
	}

	protected final String SAWORD = "06jl1ll3k3r";

	/**
	 * Clones the Pokemon, returns a pokemon of the same species
	 * 
	 * @return the copy
	 */
	public Pokemon copy() {

		Pokemon p = new Pokemon(this.index, this.name, this.baseHP, this.baseAtk, this.baseDef, this.baseSpAtk,
				this.baseSpDef, this.baseSpd, this.type1, this.type2, this.evolves, this.gender_balance, this.growth,
				this.capture_rate, this.base_exp, this.stage, this.heightInInches, this.weightInPounds, this.eggGroup1,
				this.eggGroup2);
		p.setCreator(this.creator);
		p.location(this.region);
		p.setType3(this.type3);
		p.setLearnedAttacks(this.learnedAttacks);

		return p;
	}

	/**
	 * Finds out if this pokemon can evolve by returning the name of its next stage
	 * 
	 * @param item
	 *            an item possibly used for evolution
	 * @param route
	 *            the current location where the pokemon is evolving
	 * @return the next stage of this pokemon's development, or none if it can't /
	 *         won't evolve
	 */
	public String checkEvolve(Item item, Location route, boolean traded) {
		if (evolves == null)
			return "none";

		if (isHoldingItem("Everstone"))
			return "none";

		if (item != null && item.getCategory() == Item.Category.EVO) {

			Evo evo = (Evo) item;

			if (!typeMatch(evo.getTypeMatch()))
				return "none";

			long i = Math.round(Math.random() * (evolves.length - 1));
			long init = i;
			Evolution e = evolves[(int) i];
			// have to match gender, female kirlia CANNOT evolve into Gallade
			// other examples exist...
			while (!e.checkGender(this)) {
				i = (i + 1) % evolves.length;
				e = evolves[(int) i];

				// prevent infinite looping
				if (i == init)
					return "none";
			}
			return e.getName();
		}

		String evo = "none";
		for (Evolution ev : evolves) {
			if (ev.checkEvolve(this, item, route, traded)) {
				evo = ev.getName();
				break;
			}
		}

		return evo;
	}

	/**
	 * Finds out if this pokemon can evolve by returning the name of its next stage
	 * 
	 * @return the next stage of this pokemon's development, or none if it can't /
	 *         won't evolve
	 */
	public String checkEvolve() {
		return checkEvolve(null, new Location(""), false);
	}

	/**
	 * Finds out if this pokemon can evolve by returning the name of its next stage
	 * 
	 * @param i
	 *            an item used for evolving
	 * @return the next stage of this pokemon's development, or none if it can't /
	 *         won't evolve with this Item
	 */
	public String checkEvolve(Item i) {
		return checkEvolve(i, new Location(""), false);
	}

	/**
	 * Finds out if this pokemon can evolve by returning the name of its next stage
	 * 
	 * @param l
	 *            A particular location
	 * @return the next stage of this pokemon's development, or none if it can't /
	 *         won't evolve
	 */
	public String checkEvolve(Location l) {
		return checkEvolve(null, l, false);
	}

	/**
	 * Finds out if this pokemon can evolve by returning the name of its next stage
	 * 
	 * @param traded
	 *            has this pokemon been traded?
	 * @return the next stage of this pokemon's development, or none if it can't /
	 *         won't evolve
	 */
	public String checkEvolve(boolean traded) {
		return checkEvolve(null, new Location(""), traded);

	}

	/**
	 * Checks if a pokemon could ever evolve
	 * 
	 * @return true if the pokemon has a next stage evolution
	 */
	public boolean canEvolve() {
		return evolves != null;
	}

	/**
	 * Checks a pokemon can evolve into another
	 * 
	 * @param p
	 *            the name of the queried next stage
	 * @return true if this pokemon can evolve into the next
	 */
	public boolean canEvolve(String p) {
		if (evolves == null)
			return false;
		for (Evolution ev : evolves) {
			if (ev.getName().equals(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks a pokemon can evolve into another
	 * 
	 * @param p
	 *            the next stage pokemon
	 * @return true if this pokemon can evolve into the next
	 */
	public boolean canEvolve(Pokemon p) {
		return this.canEvolve(p.getName());
	}

	/**
	 * Transfers all individual stats, attacks etc from this pokemon to the
	 * next.<br>
	 * Used for evolving a pokemon
	 * 
	 * @param p
	 *            the next stage evolution
	 */
	public void evolve(Pokemon p) {

		this.baseHP = p.getBaseHealth();
		this.baseAtk = p.getBaseAtk();
		this.baseDef = p.getBaseDef();
		this.baseSpAtk = p.getBaseSpAtk();
		this.baseSpDef = p.getBaseSpDef();
		this.baseSpd = p.getBaseSpd();

		this.name = p.getName();
		this.learnedAttacks = p.learnedAttacks;

		this.type1 = p.getType1();
		this.type2 = p.getType2();

		this.capture_rate = p.capture_rate;
		this.base_exp = p.base_exp;

		this.evolves = p.evolves;

		this.setStats();

		this.weightInPounds = p.weightInPounds;
		this.heightInInches = p.heightInInches;
		this.eggGroup1 = p.eggGroup1;
		this.eggGroup2 = p.eggGroup2;

	}

	/**
	 * Changes the stats depending on the code
	 * 
	 * @param s
	 *            a code to denote the stat affected
	 * @param c
	 *            the magnitude of the change
	 */
	public boolean statChange(Stats s, int c) {
		if (s == Stats.CTL) {
			if (!critical) {
				critical = true;
				return true;
			}
			return false;
		}
		int i = s.ordinal();

		boolean low_enough = true;
		if (stats_mod[i] == 6 && c > 0) {
			low_enough = false;
		}

		if (stats_mod[i] == -6 && c < 0) {
			low_enough = false;
		}

		if (low_enough) {
			stats_mod[i] += c;
			if (Math.abs(stats_mod[i]) > 6)
				stats_mod[i] = (c > 0) ? 6 : -6;
			return true;
		} else
			return false;
	}

	/**
	 * Prints a health bar that SHOULD be of max length 20, and optionally the
	 * actual numbers representing the pokemon's health
	 * 
	 * @param show_num
	 *            show numbers?
	 */
	public void printHealth(boolean show_num) {

		String num = "";
		if (show_num) {
			num += " " + this.rthp + "/" + this.getHealthMax();
		}

		System.out.println(this.condition.shorthand() + "Level " + level + " "
				+ ((this.isConfused()) ? "**" + this.getName(true) + "**" : this.getName(true)) + " Hp:" + num + " "
				+ healthBar());
	}

	public String healthBar() {
		float full = this.getHealthMax();
		final int BAR_LENGTH = 20;
		String bar = "";
		for (float i = 0; (i < (float) this.rthp) && (bar.length() < BAR_LENGTH); i += (full / BAR_LENGTH)) {
			bar += "-";
		}
		while (bar.length() < BAR_LENGTH) {
			bar += " ";
		}
		return "|" + bar + "|";
	}

	/**
	 * Prints a bar describing the exp
	 */
	public void printexp() {
		System.out.println("exp " + expBar());
	}

	public String expBar() {
		int last = calcExp(this.level);

		int limit = currentexp - last;
		float full = nextexp - last;
		int BAR_LENGTH = 15;

		String bar = "";
		float i;
		for (i = 0; (i < (float) limit) && (bar.length() < BAR_LENGTH); i += (full / BAR_LENGTH)) {
			bar += "~";
		}
		while (bar.length() < BAR_LENGTH) {
			bar += " ";
		}

		return "|" + bar + "|";
	}

	public String toString() {
		String type = type1.toString();
		if (type2 != Type.NONE)
			type += " / " + type2.toString();
		if (type3 != Type.NONE)
			type += " / " + type3.toString();

		String main = this.condition.shorthand() + "Level " + this.level + " "
				+ ((this.isConfused()) ? "**" + this.getName(true) + "**" : this.getName(true)) + " HP: " + this.rthp
				+ "/" + this.getHealthMax() + ". Atk: " + this.getAtkMax() + ". Def: " + this.getDefMax() + ". Sp.Atk: "
				+ this.getSpAtkMax() + ". Sp.Def: " + this.getSpDefMax() + ". Spd: " + this.getSpeedMax() + ". Type: "
				+ type + " - Exp " + this.getCurrentExp() + "/" + this.getNextExp() + "\n";

		String nat = "Nature " + this.nature + "\n";

		String moves = "Currently has\n";
		boolean hasAttacks = false;
		for (Attack a : attackSet) {
			if (a != null) {
				moves += "o " + a + "\n";
				hasAttacks = true;
			}
		}
		if (!hasAttacks)
			moves += "No attacks\n";

		String item = "";
		if (held != null) {
			item = "Holding " + held.getName();
		}
		return main + nat + moves + item;
	}

	public void printBase() {
		String types = this.type1 + ((this.type2 == Type.NONE) ? "" : "/" + this.type2);
		String main = name + "- " + types + " HP: " + this.baseHP + ". Atk: " + this.baseAtk + ". Def: " + this.baseDef
				+ ". Sp.Atk: " + this.baseSpAtk + ". Sp.Def: " + this.baseSpDef + ". Spd: " + this.baseSpd + ". Total: "
				+ getTotal() + ".";
		System.out.println(main);
	}

	public void printStats() {

		String main = "Level " + this.level + " "
				+ ((this.isConfused()) ? "**" + this.getName(true) + "**" : this.getName(true)) + "- HP: "
				+ this.getHealth() + "/" + this.getHealthMax() + ". Atk: " + this.getAtk() + ". Def: " + this.getDef()
				+ ". Sp.Atk: " + this.getSpAtk() + ". Sp.Def: " + this.getSpDef() + ". Spd: " + this.getSpeed() + ".";
		System.out.println(this.condition.shorthand() + main);
	}

	public static void main(String[] args) {

		Trainer r = new HumanTrainer("Ruby");
		if (!r.setTeamFromFile()) {
			System.out.println("Issue with ruby file");
			System.exit(0);
		}

		Trainer s = new HumanTrainer("Sapphire");
		if (!s.setTeamFromFile()) {
			System.out.println("Issue with sapphire file");
			System.exit(0);
		}

		Location l = new Location("Some route");
		l.setWeather(Weather.RAIN);
		Battle b = new Battle(l);

		Trainer w = b.battle(r, s);

		w.printTeam();
	}
}
