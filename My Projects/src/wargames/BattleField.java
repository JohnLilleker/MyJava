package wargames;

import java.awt.Color;

import jb.*;
import joreli.*;

/* Created: 15/9/2014
 * Last Modified: 19/2/2015 (MathToolKit.euclidDist, better than redefining it, Units think for themselves)
 * Next update: transfer nearest to the Unit level, given all enemy armies
 */
/**
 * An arena for war...
 * 
 * @author JB227
 */
public class BattleField {

	private Graphic field;
	private Army[] enemies;
	private int height;
	private int width;
	private Location where;

	/**
	 * 
	 * @param h
	 *            height of the field
	 * @param w
	 *            width of the arena
	 * @param e
	 *            the opposing armies in an array, allies should be merged
	 * @param loc
	 *            the name of the field of war
	 */
	public BattleField(int w, int h, Army[] e, String loc) {
		this(w, h, e, loc, Weather.CLEAR);
	}
	public BattleField(int w, int h, Army[] e, String loc, Weather weather) {
		this.field = new Graphic(w, h, loc);
		this.where = new Location(loc);
		where.setWeather(weather);
		this.height = h;
		this.width = w;
		if (e.length > 1) {
			this.enemies = e;
		}

		if (e.length == 0) {
			this.enemies = new Army[2];
			int[] s1 = { 0, 0, h / 4, w / 4 };
			int[] s2 = { 3 * h / 4, 3 * w / 4, h, w };

			this.enemies[0] = new Army("Red", 50, new Color(200, 0, 0), s1, 0);
			this.enemies[1] = new Army("Blue", 50, new Color(0, 0, 200), s2, 0);
		}

		if (e.length == 1) {
			this.enemies = new Army[2];
			this.enemies[0] = e[0];
			int s[] = { h / 4, w / 4, h / 2, w / 2 };

			this.enemies[1] = new Army("Green", 50, new Color(0, 200, 0), s, 0.25);
		}

	}

	/**
	 * View the winning faction
	 * 
	 * @return the surviving army, or null if the battle isn't over
	 */
	public Army conquerors() {
		if (end()) {
			for (Army w : enemies) {
				if (w.strength() > 0)
					return w;
			}
		}
		return null;
	}

	/**
	 * Finds the nearest enemy
	 * 
	 * @param s
	 *            the curious soldier
	 * @return the closest enemy
	 */
	private Unit nearest(Unit s) {

		int[] me = s.centre();
		Pair<Unit, Integer> near = new Pair<>(s, Integer.MAX_VALUE); // the
																		// nearest
																		// Unit
																		// and
																		// distance,
																		// initially
																		// this
																		// unit
																		// and
																		// width
																		// of
																		// field

		for (Army a : enemies) {

			if (!s.friendOrFoe(a.getUnit(0))) { // enemy army
				if (!a.destroyed()) { // there is at least 1 person left
					for (int u = 0; u < a.garrison(); u++) {
						Unit en = a.getUnit(u);
						if (!en.isDead()) { // alive

							if (s.getImage().overlaps(en.getImage()))
								return en; // if they are touching, they must be
											// the closest
							int distance = (int) MathsToolKit.euclidDist(me, en.centre());
							if (distance < near.second()) { // if closer than
															// the known nearest
								near.first(en);
								near.second(distance);
							}
						}
					}
				}
			}
		}
		return (Unit) near.first();
	}

	/**
	 * Spots if an enemy is in range
	 * 
	 * @param archer
	 * @return the nearest enemy if in range, else null
	 */
	private Unit inRange(Unit archer) {

		if (archer.arrows() == 0)
			return null;

		Unit u = nearest(archer);

		if (archer.getImage().overlaps(u.getImage()))
			return null;

		if (MathsToolKit.euclidDist(archer.centre(), u.centre()) <= Unit.RANGE)
			return u;

		return null;
	}

	/**
	 * The size of the arena
	 * 
	 * @return {width, height}
	 */
	public int[] dimensions() {
		int[] size = { this.width, this.height };
		return size;
	}

	/**
	 * The Unit with the highest overall kills
	 * 
	 * @return the Unit
	 */
	public Unit warHero() {
		Unit hero = enemies[0].warHero();

		for (Army a : enemies) {
			Unit challenge = a.warHero();
			if (challenge.kills() > hero.kills())
				hero = challenge;
		}

		return hero;
	}

	/**
	 * Let the battle commence!
	 */
	public void war() {

		// initial position
		drawField();

		long wait = 200;

		try {
			while (!end()) {
				for (Army a : enemies) {
					if (!a.destroyed()) {
						for (int s = 0; s < a.garrison(); s++) {
							// get orders, if they can
							Unit sol = a.getUnit(s);
							if (!sol.isDead()) {
								if (sol.archer() && (inRange(sol) != null)) {
									int[] stay = { 0, 0 };
									sol.orders(stay);
								} else
									sol.orders(sol.decide(nearest(sol).centre()));
							}
						}

						// mass movement
						a.charge();
						Thread.sleep(wait);
						field.clear();
						drawField();

						// attack if overlap
						for (int u = 0; u < a.garrison(); u++) {
							Unit sol = a.getUnit(u);
							if (!sol.isDead()) {
								if (sol.archer() && (inRange(sol) != null) && (sol.arrows() != 0)) { // bowman,
																										// in
																										// range
																										// and
																										// loaded
									loose(sol, inRange(sol));
									sol.fire(); // fire!
								} else {
									Unit ene = contact(sol);
									if (ene != null) {
										clash(sol, ene);
									}
								} // close combat, even archers are trained to
									// fight hand-to-hand
							}
						}
						Thread.sleep(wait);
						field.clear();
						drawField();
					}
				}
				// next player
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// until only one stands
	}

	/**
	 * After two Units meet, either they fight in a Pokemon battle and the loser
	 * is declared dead or it is a battle of luck and the loser is merely hurt
	 * 
	 * @param a
	 *            the Unit who moved first
	 * @param b
	 *            their opponent
	 */
	private void clash(Unit a, Unit b) {
		Battle fight = new Battle(where);
		fight.setLogMethod(Battle.NONE);
		Pokemon ap = a.getSoldier();
		Pokemon bp = b.getSoldier();

		fight.battle(ap, bp);

		if (b.isDead()) {
			a.claim(b);
		}
		if (a.isDead()) {
			b.claim(a);
		}
	}

	private boolean end() {
		int survive = enemies.length;
		for (Army a : enemies) {
			if (a.strength() == 0)
				survive--;
		}
		if (survive == 1)
			return true; // only one army stands...
		return false;
	}

	private void drawField() {
		for (Army a : enemies) {
			for (int d = 0; d < a.garrison(); d++) {
				Unit s = a.getUnit(d);
				if (!s.isDead())
					field.drawImage(s.getImage());
			}
		}
	}

	/**
	 * An Arrow is fired at the Enemy, possibly killing them no skill required
	 * 
	 * @param a
	 *            the archer
	 * @param t
	 *            the target
	 */
	private void loose(Unit a, Unit t) {
		double arrow = Math.random();
		if (arrow > 0.25 && arrow < 0.95) {// hit
			field.setColour(200, 200, 50);
			field.drawLine(a.centre()[0], a.centre()[1], t.centre()[0], t.centre()[1]);
			t.hit(false);
			if (t.isDead())
				a.claim(t);
		}
		if (arrow >= 0.95) {// head-shot
			field.setColour(200, 50, 50);
			field.drawLine(a.centre()[0], a.centre()[1], t.centre()[0], t.centre()[1]);
			t.hit(true);
			if (t.isDead())
				a.claim(t);
		}
		if (arrow <= 0.25) {// missed
			field.setColour(100, 200, 200);
			field.drawLine(a.centre()[0], a.centre()[1], t.location()[0], t.location()[1]);
		}
	}

	/**
	 * Is a Unit in contact with another?
	 * 
	 * @param u
	 *            the one about which you wish to enquire
	 * @return the touching Unit, or null if the Unit is alone
	 */
	private Unit contact(Unit u) {
		for (Army a : enemies) {
			if (!u.friendOrFoe(a.getUnit(0))) {
				for (int e = 0; e < a.garrison(); e++) {
					if (u.getImage().overlaps(a.getUnit(e).getImage()) && !a.getUnit(e).isDead()) {
						return a.getUnit(e);
					}
				}
			}
		}
		return null;
	}
}
