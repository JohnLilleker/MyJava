package joreli;

import joreli.Condition.Fix;
import joreli.Location.Terrain;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.List;

/* Created: 26/3/14
 * Last Modified: 9/7/17 (Printing logic updates)
/**
 * The class for any Pokemon battle. It includes Trainer-Trainer,
 * Pokemon-Pokemon and Trainer-Pokemon
 *
 * @author JB227
 */
public class Battle {

	private Weather weather;
	private int weatherCount;
	private Pokemon top = null;
	private Pokemon bottom = null;
	private boolean gainexpT = false;
	private boolean gainexpB = false;
	private Location where;
	private Trainer topTrainer = null;
	private Trainer bottomTrainer = null;
	private Random rand = new Random();

	/**
	 * No information is displayed or stored
	 */
	public static final int NONE = 0x0;
	/**
	 * Messages are sent to a specified PrintStream.<br>
	 * Default System.out but can be changed via {@link #setPrintStream(PrintStream)
	 * setPrintStream}
	 */
	public static final int PRINT = 0x1;
	/**
	 * Messages are stored internally for use later
	 */
	public static final int LOG = 0x2;
	private int logMode = PRINT;
	private List<String> log = new ArrayList<>();
	private int newestLog = 0;
	private PrintStream output = System.out;

	public Battle() {
		this(new Location("Battle arena", Terrain.BUILDING));
	}

	public Battle(Location l) {
		this.setWeather(l.getWeather());
		this.where = l;
	}

	public enum Position {
		TOP, BOTTOM
	}

	public Position positionOfPokemon(Pokemon p) {
		return (p.equals(top)) ? Position.TOP : Position.BOTTOM;
	}

	public Pokemon getPokemonAtPos(Position p) {
		return (p == Position.TOP) ? top : bottom;
	}

	public Trainer getTrainerForPos(Position p) {
		return (p == Position.TOP) ? topTrainer : bottomTrainer;
	}

	public enum Place {
		NOWHERE, GROUND, SKY, WATER, ETHER
	}

	public enum Trap {
		ROCK, SPIKES, TOXIC
	}

	public boolean setTrap(Trap trap, Position pos) {
		TrapSide t = null;
		switch (pos) {
		case BOTTOM:
			t = trapB;
			break;
		case TOP:
			t = trapT;
			break;
		}
		return t.setTrap(trap);
	}

	public void removeTrap(Position pos) {
		TrapSide t = null;
		switch (pos) {
		case BOTTOM:
			t = trapB;
			break;
		case TOP:
			t = trapT;
			break;
		}
		t.reset();
	}

	private class TrapSide {

		private boolean rock = false;
		private int spikes = 0;
		private int tox = 0;

		protected boolean setTrap(Trap t) {
			switch (t) {
			case TOXIC:
				if (tox < 2) {
					tox++;
					return true;
				}
				break;
			case ROCK:
				if (!rock) {
					rock = true;
					return true;
				}
				break;
			case SPIKES:
				if (spikes < 3) {
					spikes++;
					return true;
				}
				break;

			}
			return false;
		}

		public void reset() {
			rock = false;
			spikes = 0;
			tox = 0;
		}

		public String visual() {
			String traps = "";

			if (rock) {
				traps += "*";
			}
			for (int i = 0; i < spikes; i++) {
				traps += "^";
			}
			for (int j = 0; j < tox; j++) {
				traps += "-";
			}

			return traps;
		}

		public void effect(Pokemon p, Battle arena) {

			if (rock) {
				// 1/8 * TypeCompare
				double amount = 0.125 * (new TypeCompare()).typeCompare(p, Type.ROCK);
				int damage = (int) (p.getHealthMax() * amount);
				p.damage(damage, Attack.Category.STATUS, arena);
				arena.log(p.getName() + " was hurt by Steath Rock!");

			}
			if (spikes > 0) {
				if (!TypeCompare.isImmune(p, Type.GROUND)) {
					double amount = 0.125 + (0.0625 * (spikes - 1));
					int damage = (int) (p.getHealthMax() * amount);
					p.damage(damage, Attack.Category.STATUS, arena);
					arena.log(p.getName() + " was hurt by spikes!");
				}
			}
			if (tox > 0) {
				if (!TypeCompare.isImmune(p, Type.GROUND)) {
					if (p.typeMatch(Type.POISON)) {
						tox = 0;
						return;
					}
					Condition howBad = (tox == 1) ? Condition.POISON : Condition.BAD_POISON;
					if (p.giveCondition(howBad, "", arena)) {
						String c = (howBad == Condition.POISON) ? "Poisoned" : "Badly Poisoned";
						arena.log(p.getName() + " was " + c + "ed by Toxic Spikes!");
					}
				}
			}
		}
	}

	private TrapSide trapT = new TrapSide();
	private TrapSide trapB = new TrapSide();

	private void resetTraps() {
		trapT.reset();
		trapB.reset();
	}

	public enum Defence {
		TAILWIND, SAFEGUARD, MIST, REFLECT, SCREEN
	}

	private class DefenceSide {
		private boolean wind = false;
		private int windCount = 0;
		private boolean safe = false;
		private int safeCount = 0;
		private boolean mist = false;
		private int mistCount = 0;
		private boolean refl = false;
		private int refCount = 0;
		private boolean scrn = false;
		private int scrCount = 0;

		public boolean setDefence(Defence d, Battle arena) {
			boolean set = false;
			switch (d) {
			case MIST:
				if (!mist) {
					mistCount = 5;
					set = true;
					mist = true;
					arena.log("Mist appeared");
				}
				break;
			case REFLECT:
				if (!refl) {
					refCount = 5;
					set = true;
					refl = true;
					arena.log("A barrier appeared");
				}
				break;
			case SAFEGUARD:
				if (!safe) {
					safeCount = 5;
					set = true;
					safe = true;
					arena.log("A barrier appeared");
				}
				break;
			case SCREEN:
				if (!scrn) {
					scrCount = 5;
					set = true;
					scrn = true;
					arena.log("A barrier appeared");
				}
				break;
			case TAILWIND:
				if (!wind) {
					windCount = 3;
					set = true;
					wind = true;
					arena.log("A Tailwind blew");
				}
				break;

			}
			return set;
		}

		public boolean defenceSet(Defence d) {
			switch (d) {
			case MIST:
				return mist;
			case REFLECT:
				return refl;
			case SAFEGUARD:
				return safe;
			case SCREEN:
				return scrn;
			case TAILWIND:
				return wind;
			}
			return false;
		}

		public void removeDefences(Defence[] ds, Battle arena) {
			for (Defence d : ds) {
				switch (d) {
				case MIST:
					if (mist) {
						mist = false;
						arena.log("The mist disapated!");
					}
					break;
				case REFLECT:
					if (refl) {
						refl = false;
						arena.log("The barrier broke!");
					}
					break;
				case SAFEGUARD:
					if (safe) {
						safe = false;
						arena.log("The barrier disappeared");
					}
					break;
				case SCREEN:
					if (scrn) {
						scrn = false;
						arena.log("The screen broke!");
					}
					break;
				case TAILWIND:
					if (wind) {
						wind = false;
						arena.log("The wind died down");
					}
					break;

				}
			}
		}

		public void countDown(Battle arena) {
			if (windCount > 0) {
				windCount--;
				if (windCount == 0) {
					removeDefences(new Defence[] { Defence.TAILWIND }, arena);
				}
			}
			if (safeCount > 0) {
				safeCount--;
				if (safeCount == 0) {
					removeDefences(new Defence[] { Defence.SAFEGUARD }, arena);
				}
			}
			if (scrCount > 0) {
				scrCount--;
				if (scrCount == 0) {
					removeDefences(new Defence[] { Defence.SCREEN }, arena);
				}
			}
			if (refCount > 0) {
				refCount--;
				if (refCount == 0) {
					removeDefences(new Defence[] { Defence.REFLECT }, arena);
				}
			}
			if (mistCount > 0) {
				mistCount--;
				if (mistCount == 0) {
					removeDefences(new Defence[] { Defence.MIST }, arena);
				}
			}
		}
	}

	private DefenceSide defT = new DefenceSide();
	private DefenceSide defB = new DefenceSide();

	public boolean setDefence(Position pos, Defence d) {
		if (pos == Position.TOP)
			return defT.setDefence(d, this);
		return defB.setDefence(d, this);
	}

	public boolean defenceSet(Position pos, Defence d) {
		if (pos == Position.TOP)
			return defT.defenceSet(d);
		return defB.defenceSet(d);
	}

	public void removeDefences(Position pos, Defence[] ds) {
		if (pos == Position.TOP)
			defT.removeDefences(ds, this);
		else
			defB.removeDefences(ds, this);
	}

	private class DelayedAttack {
		private Attack attack;
		private int damage;
		private int turns;
		private Position pos;

		protected DelayedAttack(Attack a, int d, int t, Position p) {
			this.attack = a;
			this.damage = d;
			this.turns = t;
			this.pos = p;
		}

		protected boolean use(Battle arena) {
			if ((turns--) == 0) {
				if (damage > 0) {
					Pokemon defender = arena.getPokemonAtPos(pos);
					arena.log(defender.getName() + " was damaged by " + attack.getName());
					defender.damage(damage, attack.getCategory(), arena);
				} else
					arena.log(attack.getName() + " failed...");
				return true;
			}
			return false;
		}
	}

	private ArrayList<DelayedAttack> futureAttacks = new ArrayList<>();

	public void enqueueAttack(Attack attack, int damage, int turns, Position pos) {
		futureAttacks.add(new DelayedAttack(attack, damage, turns, pos));
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather w) {
		this.weather = w;
		if (w != Weather.CLEAR)
			this.weatherCount = 5;
	}

	public Location getLocation() {
		return where;
	}

	public void setExpGainTop(boolean exp) {
		this.gainexpT = exp;
	}

	public void setExpGainBottom(boolean exp) {
		this.gainexpB = exp;
	}

	public void setExpGain(boolean exp, Position pos) {
		switch (pos) {
		case BOTTOM:
			this.gainexpB = exp;
			break;
		case TOP:
			this.gainexpT = exp;
			break;
		}
	}

	public boolean topExpSet() {
		return gainexpT;
	}

	public boolean bottomExpSet() {
		return gainexpB;
	}

	/**
	 * Takes a String and logs the message. Behaviour depends on the mode set via
	 * setLogMethod
	 * 
	 * @param msg
	 *            a String
	 */
	public void log(String msg) {
		switch (logMode) {
		case LOG:
			log.add(msg);
			break;
		case PRINT:
			output.println(msg);
			break;
		default:
			break;

		}
	}

	/**
	 * When the log method is {@link LOG}, any prints are stored. This allows access
	 * 
	 * @return a list of all loggable material so far
	 */
	public List<String> getLogs() {
		if (logMode != LOG)
			return null;
		return log;
	}

	/**
	 * wipes the stored logs
	 */
	public void clearLog() {
		log.clear();
		newestLog = 0;
	}

	/**
	 * When the log method is {@link LOG}, this gets any new logs since the last
	 * method call
	 * 
	 * @return a list of unseen logs
	 */
	public ArrayList<String> getLatestLogs() {
		if (logMode != LOG)
			return null;
		int size = log.size();
		ArrayList<String> logs = new ArrayList<String>(size - this.newestLog);
		for (int i = newestLog; i < size; i++) {
			logs.add(log.get(i));

		}
		newestLog = size;
		return logs;
	}

	/**
	 * Sets how the battle shows information
	 * 
	 * @param mode
	 *            either NONE (no information), PRINT (any messages are forwarded to
	 *            a PrintStream), LOG (messages are stored in a List and accessible
	 *            by methods getLatestLogs() and getLogs())
	 */
	public void setLogMethod(int mode) {
		logMode = mode;
	}

	/**
	 * Returns the Log mode currently used
	 * 
	 * @return the int code
	 */
	public int getLogMode() {
		return logMode;
	}

	/**
	 * Sets a PrintStream for displaying messages if the log mode is set to
	 * {@link PRINT}
	 * 
	 * @param ps
	 *            a PrintStream object
	 */
	public void setPrintStream(PrintStream ps) {
		output = ps;
	}

	/**
	 * Orchestrates a battle between 2 pokemon
	 *
	 * @param p1
	 *            a battling pokemon
	 * @param p2
	 *            the other pokemon
	 * @return the winner, unless both pokemon fainted in which case null is
	 *         returned
	 */
	public Pokemon battle(Pokemon p1, Pokemon p2) {

		this.setWeather(where.getWeather());
		int turn = 1;

		top = p1;
		bottom = p2;

		this.battleView();
		while (!(top.fainted() || bottom.fainted())) {

			if (this.logMode == PRINT)
				System.out.println("Turn " + (turn++) + " -------------------------\n");

			top.callAttack(this.comChoice(top));
			bottom.callAttack(this.comChoice(bottom));

			doAttacks();
			if (top.isRunning() || bottom.isRunning()) {
				break;
			}
		}

		Pokemon winner = null;
		if (!top.fainted() || bottom.isRunning())
			winner = top;
		if (!bottom.fainted() || top.isRunning())
			winner = bottom;

		this.clearAll();

		return winner;
	}

	/**
	 * Facilitates a Battle between two trainers
	 *
	 * @param t1
	 *            A subclass of Trainer
	 * @param t2
	 *            Another Trainer object
	 * @return the winner, if one exists
	 */
	public Trainer battle(Trainer t1, Trainer t2) {

		this.setWeather(where.getWeather());
		top = t1.getLead();
		bottom = t2.getLead();
		topTrainer = t1;
		bottomTrainer = t2;

		int turn = 1;
		log(topTrainer.getName() + " sent out " + top.getName() + "!");
		log(bottomTrainer.getName() + " sent out " + bottom.getName() + "!");
		trainerBattleView();

		while (topTrainer.canBattle() && bottomTrainer.canBattle()) {

			while (!(top.fainted() || bottom.fainted())) {

				if (this.logMode == PRINT)
					System.out.println("Turn " + (turn++) + " -------------------------\n");

				Trainer.Response res1 = topTrainer.action(top);
				Trainer.Response res2 = bottomTrainer.action(bottom);

				if (this.parseResponse(top, topTrainer, res1, Position.TOP)) {
					clearAll();
					return bottomTrainer;
				}
				if (this.parseResponse(bottom, bottomTrainer, res2, Position.BOTTOM)) {
					clearAll();
					return topTrainer;
				}

				doAttacks();
			}

			// If a Pokemon faints another is sent out
			if (top.fainted()) {
				if (!bottom.fainted() && gainexpB) {
					bottomTrainer.levelUp(bottom, (int) (top.expYield() * 1.5), this);
				}
				top.resetStats();
				if (topTrainer.canBattle()) {
					top = topTrainer.selectPokemon(top);
					log(topTrainer.getName() + " sent out " + top.getName() + "!");
					trapT.effect(top, this);
				}
			}

			if (bottom.fainted()) {
				if (!top.fainted() && gainexpT) {
					topTrainer.levelUp(top, (int) (bottom.expYield() * 1.5), this);
				}
				bottom.resetStats();
				if (bottomTrainer.canBattle()) {
					bottom = bottomTrainer.selectPokemon(bottom);
					log(bottomTrainer.getName() + " sent out " + bottom.getName() + "!");
					trapB.effect(bottom, this);
				}
			}
			this.trainerBattleView();
		}

		Trainer winner = null;

		if (topTrainer.canBattle())
			winner = topTrainer;
		if (bottomTrainer.canBattle())
			winner = bottomTrainer;

		this.clearAll();

		log(winner.getName() + " won!");

		return winner;
	}

	private boolean parseResponse(Pokemon p, Trainer t, Trainer.Response res, Position pos) {
		switch (res.action()) {
		case ATTACK:
			p.callAttack(res.attack());
			break;
		case ITEM:
			log(t.getName() + " used a " + res.item().getName());
			p.useItem(res.item(), where);
			break;
		case RUN:
			log(t.getName() + " conceded...");
			this.clearAll();
			return true;
		case SWITCH:
			this.switchIn(res.pokemon(), pos);
			break;
		}
		return false;
	}

	/**
	 * A method describing a battle akin to an encounter with a wild pokemon
	 *
	 * @param t1
	 *            a Trainer subclass object
	 * @param wild
	 *            a Pokemon
	 * @return true if the trainer wins
	 */
	public boolean battle(Trainer t1, Pokemon wild) {

		this.setWeather(where.getWeather());

		top = t1.getLead();
		bottom = wild;
		topTrainer = t1;

		int turn = 1;
		log(topTrainer.getName() + " sent out " + top.getName() + "!");
		trainerBattleView();

		while (topTrainer.canBattle() && !bottom.fainted()) {

			while (!top.fainted() && !bottom.fainted()) {

				if (this.logMode == PRINT)
					System.out.println("Turn " + (turn++) + " -------------------------\n");

				Trainer.Response res = topTrainer.action(top);
				if (this.parseResponse(top, topTrainer, res, Position.TOP)) {
					clearAll();
					return false;
				}

				bottom.callAttack(comChoice(bottom));

				this.doAttacks();
				if (bottom.isRunning()) {
					break;
				}

			}
			if (bottom.isRunning()) {
				break;
			}

			if (top.fainted()) {
				if (topTrainer.canBattle()) {
					top.resetStats();
					top = topTrainer.selectPokemon(top);
					log(topTrainer.getName() + " sent out " + top.getName() + "!");
					trapT.effect(top, this);
				}
			}
			this.trainerBattleView();
		}

		if (bottom.fainted() && gainexpT) {
			topTrainer.levelUp(top, bottom.expYield(), this);
		}

		this.clearAll();

		return t1.canBattle();

	}

	public void switchIn(Pokemon p, Position pos) {

		top.giveFix(Fix.FALL_OUT, this);
		bottom.giveFix(Fix.FALL_OUT, this);

		log(this.getTrainerForPos(pos).getName() + " sent out " + p.getName());
		switch (pos) {
		case BOTTOM:
			bottom.switchOut();
			top.giveFix(Fix.BREAK_OUT, this);
			bottom = p;
			trapB.effect(bottom, this);
			bottom.switchIn();
			break;
		case TOP:
			top.switchOut();
			bottom.giveFix(Fix.BREAK_OUT, this);
			top = p;
			trapT.effect(top, this);
			top.switchIn();
			break;
		}
	}

	private void doAttacks() {

		Position quickest, slowest;
		Attack first, second;

		Attack a1 = top.nextAttack();
		Attack a2 = bottom.nextAttack();

		if (a1 == null && a2 == null)
			return;

		if (a1 == null) {
			if (bottom.canAttack(a2, this)) {
				bottom.attack(a2, top, this);
			}

			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

			if (checkFainted())
				return;
		} else if (a2 == null) {

			if (top.canAttack(a1, this)) {
				top.attack(a1, bottom, this);
			}

			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

			if (checkFainted())
				return;
		} else {
			if (a1.getPriority() != a2.getPriority()) {
				if (a1.getPriority() < a2.getPriority()) {
					quickest = Position.BOTTOM;
					slowest = Position.TOP;
					first = a2;
					second = a1;
				} else {
					quickest = Position.TOP;
					slowest = Position.BOTTOM;
					first = a1;
					second = a2;
				}
			} else {

				// speed
				int tSpd = top.getSpeed();
				if (defT.defenceSet(Defence.TAILWIND)) {
					tSpd *= 2;
				}
				int bSpd = bottom.getSpeed();
				if (defB.defenceSet(Defence.TAILWIND)) {
					bSpd *= 2;
				}

				if (tSpd < bSpd) {
					quickest = Position.BOTTOM;
					slowest = Position.TOP;
					first = a2;
					second = a1;
				} else {
					quickest = Position.TOP;
					slowest = Position.BOTTOM;
					first = a1;
					second = a2;
				}
			}

			if (this.getPokemonAtPos(quickest).canAttack(a1, this)) {
				this.getPokemonAtPos(quickest).attack(first, this.getPokemonAtPos(slowest), this);
			}

			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

			if (checkFainted())
				return;

			if (this.getPokemonAtPos(slowest).canAttack(a2, this)) {
				this.getPokemonAtPos(slowest).attack(second, this.getPokemonAtPos(quickest), this);
			}

			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

			if (checkFainted())
				return;
		}

		boolean seeded = false;
		if (top.isSeeded()) {
			seeded = true;
			// do 1/8 damage and give to bottom
			log(top.getName() + " is hurt by leech seed!");

			int dam = (int) (top.getHealthMax() * 0.125);
			if (dam > top.getHealth())
				dam = top.getHealth();
			top.damage(dam);
			if (top.fainted())
				log(top.getName() + " fainted!");

			if (!bottom.fainted() && bottom.getHealth() < bottom.getHealthMax()) {
				bottom.heal(dam);
				log(bottom.getName() + " absorbed " + top.getName() + "'s health!");
			}
		}
		if (bottom.isSeeded()) {
			seeded = true;
			log(bottom.getName() + " is hurt by leech seed!");

			int dam = (int) (bottom.getHealthMax() * 0.125);
			if (dam > bottom.getHealth())
				dam = bottom.getHealth();
			bottom.damage(dam);
			if (bottom.fainted())
				log(bottom.getName() + " fainted!");

			if (!top.fainted() && top.getHealth() < top.getHealthMax()) {
				top.heal(dam);
				log(top.getName() + " absorbed " + bottom.getName() + "'s health!");

			}
		}
		if (seeded)
			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

		defT.countDown(this);
		defB.countDown(this);

		Iterator<DelayedAttack> iter = futureAttacks.iterator();
		while (iter.hasNext()) {
			DelayedAttack d = iter.next();
			if (d.use(this)) {
				if (topTrainer == null)
					battleView();
				else
					trainerBattleView();
				iter.remove();
				if (checkFainted())
					return;
			}
		}

		boolean hurt1 = top.damagingFactors(this);
		boolean hurt2 = bottom.damagingFactors(this);
		if (hurt1 || hurt2) {

			if (topTrainer == null)
				battleView();
			else
				trainerBattleView();

			if (top.fainted()) {
				log(top.getName() + " fainted");
			}
			if (bottom.fainted()) {
				log(bottom.getName() + " fainted");
			}

		}
		if (weatherCount > 0) {
			if ((--weatherCount) == 0) {
				switch (weather) {
				case HAIL:
					log("The hail cleared");
					break;
				case RAIN:
					log("The rain cleared");
					break;
				case SANDSTORM:
					log("The sandstorm died down");
					break;
				case SUN:
					log("The harsh sunlight died down");
					break;
				default:
					break;
				}
				this.weather = Weather.CLEAR;
			}
		}
	}

	private boolean checkFainted() {
		if (top.fainted()) {
			log(top.getName() + " fainted");
			bottom.giveFix(Condition.Fix.FALL_OUT, this);
			bottom.giveFix(Condition.Fix.BREAK_OUT, this);
			bottom.giveFix(Condition.Fix.ESCAPE, this);
			bottom.damagingFactors(this);
			if (bottom.fainted()) {
				log(bottom.getName() + " fainted");
			}
			return true;
		}
		if (bottom.fainted()) {
			log(bottom.getName() + " fainted");
			top.giveFix(Condition.Fix.FALL_OUT, this);
			top.giveFix(Condition.Fix.BREAK_OUT, this);
			top.giveFix(Condition.Fix.ESCAPE, this);
			top.damagingFactors(this);
			if (top.fainted()) {
				log(top.getName() + " fainted");
			}
			return true;
		}
		return false;
	}

	private Attack comChoice(Pokemon p) {

		if (p.tired())
			return Pokemon.cantAttack();

		if (p.isCharging())
			return p.chargingAttack();

		int st = rand.nextInt(Pokemon.NO_OF_ATTACKS);

		boolean picked = false;
		Attack at;
		do {
			at = p.getAttack(st);
			if (at != null && !at.exhausted()) {
				picked = true;
			} else {
				st++;
				if (st == Pokemon.NO_OF_ATTACKS)
					st = 0;
			}
		} while (!picked);
		return at;
	}

	private void battleView() {
		if (logMode != PRINT)
			return;
		String w = (weather != Weather.CLEAR) ? weather.toString() : "";
		String upper = "\n_____" + w;
		while (upper.length() < 25)
			upper += "_";
		System.out.println(upper);
		System.out.println("");
		top.printHealth(true);
		System.out.println(trapT.visual());
		System.out.println("");
		bottom.printHealth(true);
		System.out.println(trapB.visual());
		System.out.println("_________________________");
		System.out.println("");
	}

	private void trainerBattleView() {
		if (logMode != PRINT)
			return;
		String w = (weather != Weather.CLEAR) ? weather.toString() : "";
		String upper = "\n_____" + w;
		while (upper.length() < 25)
			upper += "_";
		System.out.println(upper);
		System.out.println("");

		topTrainer.printBattleInfo();
		top.printHealth(topTrainer.isHuman());
		if (topTrainer.isHuman() && this.gainexpT)
			top.printexp();

		System.out.println(trapT.visual());
		System.out.println("");
		if (bottomTrainer == null) {
			bottom.printHealth(false);
		} else {
			bottomTrainer.printBattleInfo();
			bottom.printHealth(bottomTrainer.isHuman());
			if (bottomTrainer.isHuman() && this.gainexpB)
				bottom.printexp();
		}
		System.out.println(trapB.visual());
		System.out.println("_________________________");
		System.out.println("");
	}

	private void clearAll() {
		futureAttacks.clear();
		top.ecsape();
		bottom.ecsape();
		top.resetStats();
		bottom.resetStats();
		if (topTrainer != null)
			topTrainer.restore();
		if (bottomTrainer != null)
			bottomTrainer.restore();
		top = null;
		bottom = null;
		topTrainer = null;
		bottomTrainer = null;
		resetTraps();
	}

	protected static final String R_LEDGE = "RELish";

	public static void main(String[] args) {
		Trainer john = new HumanTrainer("John");

		Pokedex dex = new Pokedex();

		Pokemon start = dex.pokeSearch("Charmander");

		start.setLevel(15);

		john.catchPokemon(start);

		start.gainExp(1000);

		start.learnAttack(dex.attackSearch("ember"));
		start.learnAttack(dex.attackSearch("metal claw"));
		start.learnAttack(dex.attackSearch("scratch"));
		start.learnAttack(dex.attackSearch("smokescreen"));

		Pokemon wild = dex.pokeSearch("Spinarak");
		wild.setLevel(18);

		wild.learnAttack(dex.attackSearch("poison sting"));
		wild.learnAttack(dex.attackSearch("venoshock"));
		wild.learnAttack(dex.attackSearch("String shot"));
		wild.learnAttack(dex.attackSearch("Leech Life"));

		Battle b = new Battle();
		// b.setExpGain(true);

		System.out.println(start);

		b.battle(john, wild);

		System.out.println(start);
	}
}
