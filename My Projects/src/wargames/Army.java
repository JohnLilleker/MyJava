package wargames;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import jb.*;
import joreli.*;

/* Created: 15/9/2014
 * Last Modified: 22/11/2017 (Changes to SimpleImage and unit)
 */
/**
 * A group of Units of different strength
 * 
 * @author JB227
 */
public class Army {

	private ArrayList<Unit> men = new ArrayList<>();
	private String nation;

	// TODO - fix constructors and pokemon creation
	// Constructors could take a predicate?
	// Levels...
	// pass in constructor a single level or a range?
	
	/**
	 * 
	 * @param s
	 *            the number of Units
	 * @param p
	 *            the colour of the troops
	 * @param start
	 *            coordinates of the starting area [0][1] bottom left, [2][3]
	 *            top right
	 * @param archers
	 *            the probability of a soldier being a archer, 0-1
	 */
	public Army(String n, int s, Color p, int[] start, double archers) {
		this.nation = n;
		// for every 10 men, 9 rank 1 and a rank 2
		// for every 50 men, 1 rank 3
		// and for the maximum 100 men, there will be a rank 4

		int count = 0;
		while (s > 0) {

			count++;
			int r;

			if (count % 100 == 0)
				r = 4;
			else if (count % 50 == 0 || count % 99 == 0)
				r = 3;
			else if (count % 10 == 0 || count % 49 == 0)
				r = 2;
			else
				r = 1;

			SimpleImage i = standard(p, r);
			Pokemon w = recruit(r, null);
			int[] co = space(start, count);
			boolean bowman = Math.random() < archers;
			// archers have a black stripe
			if (bowman) {
				for (int str = 0; str < 5; str++) {
					i.colourIn(4, str, Color.black);
				}
			}
			men.add(new Unit(i, w, co, this.nation, bowman));
			s--;
			if (s == 0)
				break;

		}
	}

	/**
	 * 
	 * @param s
	 *            the number of Units
	 * @param t
	 *            A specific, favourable type for the warriors
	 * @param p
	 *            a sample for the colour of the troops
	 * @param start
	 *            coordinates of the starting area [0][1] bottom left, [2][3]
	 *            top right
	 * @param archers
	 *            the approximate ratio of archers, 0-1
	 */
	public Army(String n, int s, Type type, Color p, int[] start, double archers) {
		this.nation = n;
		// the same, but the Pokemon are specially selected by types
		// ranks 3 and 4 may get random legends
		// (no grass or rock legends so avoiding a problem before it occurs)
		int count = 0;
		while (s > 0) {

			count++;
			int r;
			if (count % 100 == 0)
				r = 4;
			else if (count % 50 == 0 || count % 99 == 0)
				r = 3;
			else if (count % 10 == 0 || count % 49 == 0)
				r = 2;
			else
				r = 1;

			SimpleImage i = standard(p, r);
			Pokemon w = recruit(r, type);
			int[] co = space(start, count);
			boolean bowman = Math.random() < archers;
			// archers have a black stripe
			if (bowman) {
				for (int str = 0; str < 5; str++) {
					i.colourIn(4, str, Color.black);
				}
			}
			men.add(new Unit(i, w, co, this.nation, bowman));
			s--;
			if (s == 0)
				break;
		}
	}

	/**
	 * 
	 * @param n
	 *            name of the Army
	 * @param s
	 *            the initial number of service-Units
	 * @param p
	 *            the Colour
	 * @param start
	 *            the start grid
	 * @param archers
	 *            the rough percentage of archer, 0 none, 1 means all
	 */
	public Army(String n, int s, Color p, int[] start, String reg, double archers) {

		this.nation = n;

		int count = 0;
		while (s > 0) {

			count++;
			int r;

			if (count % 100 == 0)
				r = 4;
			else if (count % 50 == 0 || count % 99 == 0)
				r = 3;
			else if (count % 10 == 0 || count % 49 == 0)
				r = 2;
			else
				r = 1;

			SimpleImage i = standard(p, r);
			Pokemon w = local(reg, r);
			int[] co = space(start, count);
			boolean bowman = Math.random() < archers;
			if (bowman) {
				for (int str = 0; str < 5; str++) {
					i.colourIn(4, str, Color.black);
				}
			}
			Unit m = new Unit(i, w, co, this.nation, bowman);
			men.add(m);
			s--;
			if (s == 0)
				break;
		}
	}

	/**
	 * Creates an army from an Array, just give them a name and they will sort
	 * themselves out
	 * 
	 * @param n
	 *            the name of the Army
	 * @param m
	 *            the Array, if they have different markings, the first controls
	 *            the Standard
	 */
	public Army(String n, Unit[] m) {
		this.nation = n;

		for (Unit u : m) {
			u.joinArmy(this.nation);
			men.add(u);
		}
	}

	public String getName() {
		return nation;
	}

	/**
	 * Gives the Units new coordinates to start from
	 * 
	 * @param start
	 *            the new start area [0], [1] bottom left [2],[3] top right
	 */
	public void redeploy(int[] start) {

		int num = 0;
		for (Unit u : men) {
			num++;
			int[] location = this.space(start, num);
			u.drop(location);
		}
	}

	/**
	 * Everyone moves, they should have their orders or they won't budge
	 */
	public void charge() {
		for (Unit s : men) {
			s.move();
		}
	}

	public Unit getUnit(int n) {
		return men.get(n);
	}

	/**
	 * Creates a group of Units, still part of the army but can be moved
	 * separately
	 * 
	 * @param s
	 *            the size of the group
	 * @return an Array of Units. If the parameters are too big, the array may
	 *         be shorter than expected.
	 */
	public Unit[] detach(int s) {
		Unit[] group = new Unit[s];

		for (int i = 0; i < (s); i++) {
			if (i > this.garrison())
				break;
			group[i] = this.getUnit(i);
		}

		if (group[s - 1] == null) {
			int truesize = 0;
			for (Unit u : group) {
				if (u != null)
					truesize++;
			}
			Unit[] real = new Unit[truesize];
			for (int i = 0; i < truesize; i++) {
				real[i] = group[i];
			}
			group = real;
		}
		return group;
	}

	/**
	 * The size of the Army at full strength, counting the dead as well
	 * 
	 * @return the full strength of the Army
	 */
	public int garrison() {
		return men.size();
	}

	/**
	 * The number of active or "living" Units
	 * 
	 * @return How many Units aren't dead
	 */
	public int strength() {

		int s = 0;
		for (Unit u : men) {
			if (!u.isDead())
				s++;
		}
		return s;
	}

	/**
	 * Is anybody alive?
	 * 
	 * @return true if no-one is left
	 */
	public boolean destroyed() {
		return this.strength() == 0;
	}

	public String toString() {
		return nation + "- " + this.garrison() + " men, " + this.strength() + " active";
	}

	/**
	 * A form of the toString() that states every individual Unit
	 * 
	 * @return the toString plus every Unit separated by \n
	 */
	public String rollCall() {
		String head = this.toString() + ":\n";
		for (Unit u : men) {
			head += u.toString() + "\n";
		}
		return head;
	}

	/**
	 * The relative strength of the army
	 * 
	 * @return a String showing the highest level warrior, type balance and average level
	 */
	public String intel() {
		
		// A bit of fiddling, but got there in the end
		String power = "No Highest Level";
		Optional<Unit> strongest = men.parallelStream().max((Unit u1, Unit u2) -> {
			int l1 = u1.getSoldier().getLevel();
			int l2 = u2.getSoldier().getLevel();
			return Integer.compare(l1, l2);
		});
		if (strongest.isPresent()) {
			power = strongest.get().toString();
		}
		
		// one liner average!
		double average = men.parallelStream().mapToInt(u -> u.getSoldier().getLevel()).average().getAsDouble();
		
		// number of dead, half dead and healthy
		long dead = men.parallelStream().filter(u -> u.isDead()).count();
		long half = men.parallelStream().filter(u -> ((double) u.getSoldier().getHealth())/u.getSoldier().getHealthMax() <= 0.5).count();
		long full = men.parallelStream().filter(u ->  u.getSoldier().getHealth() == u.getSoldier().getHealthMax()).count();
		String health = String.format("%d/%d/%d", dead, half, full);
		
		// type balance?
		// could be a bit tricky...
		// use a map?
		
		Map<Type, Integer> types = new HashMap<>();
		for (Unit u : men) {
			Pokemon p = u.getSoldier();
			Type t1 = p.getType1(), t2 = p.getType2();
			if (types.containsKey(t1)) {
				types.put(t1, types.get(t1)+1);
			}
			else {
				types.put(t1, 1);
			}
			if (t2 != Type.NONE) {
				if (types.containsKey(t2)) {
					types.put(t2, types.get(t2)+1);
				}
				else {
					types.put(t2, 1);
				}
			}
		}
		
		String soFar = String.format("Strongest - %s\nAverage Level - %.1f\nHealth - %s\n", power, average, health);
		String balance = "";
		for (Map.Entry<Type, Integer> entry : types.entrySet()) {
			balance += String.format("%s - %d\n", entry.getKey(), entry.getValue());
		};
		return soFar + "Type balance\n" + balance;
	}

	/**
	 * Adds a Unit to the Army and gives them the friend-or-foe marking
	 * 
	 * @param m
	 *            the new recruit
	 */
	public void recruit(Unit m) {
		m.joinArmy(getName());
		men.add(m);
	}

	/**
	 * Joins another Army to this one. The allies gain the friend-or-foe marking
	 * 
	 * @param a
	 *            the new allies
	 */
	public void merge(Army a) {
		for (int m = 0; m < a.garrison(); m++) {
			this.recruit(a.getUnit(m));
		}
	}

	/**
	 * Tries to discharge a Unit
	 * 
	 * @param s
	 *            the soldier
	 * @return true if it worked
	 */
	public boolean discharge(Unit s) {
		return men.remove(s);
	}

	/**
	 * Gets rid of the dead Units
	 * 
	 * @return the losses
	 */
	public int bury() {

		int dead = 0;

		Iterator<Unit> reaper = men.iterator();

		while (reaper.hasNext()) {
			Unit fate = reaper.next();
			if (fate.isDead()) {
				reaper.remove();
				dead++;
			}
		}
		return dead;
	}

	/**
	 * Brings back the dead... but only the number given
	 * 
	 * @param r
	 *            the number of men to be revived
	 */
	public void revive(int r) {
		for (Unit w : men) {
			if (w.isDead()) {
				w.revive();
				r--;
				if (r == 0)
					break;
			}
		}
	}

	/**
	 * Replenishes the armour of everyone not dead
	 */
	public void rest() {
		for (Unit u : men) {
			if (!u.isDead()) {
				u.rest();
			}
		}
	}

	/**
	 * The archers get their arrows back, if they aren't dead
	 */
	public void collectArrows() {
		for (Unit u : men) {
			if (u.archer() && !u.isDead()) {
				u.restock();
			}
		}
	}

	/**
	 * Most distinguished Unit (most kills)
	 * 
	 * @return the Unit with the most kills
	 */
	public Unit warHero() {
		Unit hero = men.get(0);
		for (Unit u : men) {
			if (u.kills() > hero.kills())
				hero = u;
		}
		return hero;
	}

	/**
	 * Creates the SimpleImage for the Unit, based on rank
	 * 
	 * @param p
	 *            a sample of a colour
	 * @param r
	 *            the rank
	 * @return the SimpleImage
	 */
	private SimpleImage standard(Color p, int r) {

		SimpleImage u = new SimpleImage(5, 5, 3);
		u.fill(p);

		Color c = Color.black;
		switch (r) {
		case 1:
			u.colourIn(3, 1, c);
			u.colourIn(3, 3, c);
			break;

		case 2:
			u.colourIn(2, 0, c);
			u.colourIn(3, 1, c);
			u.colourIn(2, 2, c);
			u.colourIn(1, 3, c);
			u.colourIn(2, 4, c);
			break;

		case 3:
			u.colourIn(2, 0, c);
			u.colourIn(3, 1, c);
			u.colourIn(2, 2, c);
			u.colourIn(3, 3, c);
			u.colourIn(2, 4, c);
			break;

		case 4:
			u.colourIn(1, 1, c);
			u.colourIn(3, 1, c);
			u.colourIn(2, 2, c);
			u.colourIn(3, 3, c);
			break;
		}
		return u;
	}

	/**
	 * Creates the Pokemon for each Unit
	 * 
	 * @param r
	 *            the rank
	 * @param t
	 *            the type if given, null otherwise
	 * @return the Pokemon, all set up and battle ready
	 */
	private Pokemon recruit(int r, Type t) {

		Pokedex pm = new Pokedex(true);

		Pokemon p = pm.randomPokemon(t);

		// for Pokemon
		// rank 1, 75% seed 1, else seed 2
		// rank 2, 85% seed 2, else seed 1
		// rank 3, 85% seed 3, else seed 4
		// rank 4, 50/50 seed 4 or 5
		
		// UPDATE!!!
		// change seed to Level!
		// I'm so smart...
/*
		int l;
		switch (r) {
		case 1:
			if (Math.random() < 0.75)
				l = 1;
			else
				l = 2;
			if (t == null)
				p = pm.randomPokemon(l);
			else
				p = pm.randomPokemon(Type.fromString(t), l);
			break;

		case 2:
			if (Math.random() < 0.85)
				l = 2;
			else
				l = 1;
			if (t == null)
				p = pm.randomPokemon(l);
			else
				p = pm.randomPokemon(Type.fromString(t), l);
			break;

		case 3:
			if (Math.random() < 0.85)
				l = 3;
			else
				l = 4;
			if (t == null || l == 4)
				p = pm.randomPokemon(l);
			else
				p = pm.randomPokemon(Type.fromString(t), l);
			break;

		case 4:
			if (Math.random() < 0.5)
				l = 4;
			else
				l = 5;
			p = pm.randomPokemon(l);
			break;
		}
		*/
		p.setLevel(50);
		pm.setAttacks(p);
		return p;
	}

	private Pokemon local(String r, int l) {

		Pokedex area = new Pokedex();
		Pokemon local = null;
		local = area.pokemonWhere(p -> p.home().equals(r));
		int s = 0;
/*		switch (l) {
		case 1:
			if (Math.random() < 0.75)
				s = 1;
			else
				s = 2;
			local = area.pokemonWhere(p -> p.home().equals(r));
			break;

		case 2:
			if (Math.random() < 0.85)
				s = 2;
			else
				s = 1;
			local = area.randomByRegion(r, s);
			break;

		case 3:
			if (Math.random() < 0.85)
				s = 3;
			else
				s = 4;
			local = area.randomByRegion(r, s);
			break;

		case 4:
			if (Math.random() < 0.5)
				s = 4;
			else
				s = 5;
			local = area.randomByRegion(r, s);
			break;
		}
		*/
		local.setLevel(50);
		area.setAttacks(local);
		return local;
	}

	private int[] space(int[] area, int n) {

		int x1 = area[0];
		int y1 = area[1];
		int x2 = area[2];
		int y2 = area[3];

		int[] coord = { x1, y1 };
		int room = 15;

		for (int i = 0; i < n; i++) {

			coord[0] += room;
			if (coord[0] > x2) {
				coord[0] = x1;
				coord[1] += room;
				if (coord[1] > y2) {
					coord[1] = y1;
					room++;
				}
			}
		}
		return coord;
	}
}
