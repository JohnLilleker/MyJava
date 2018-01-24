package wargames;

import java.util.ArrayList;
import java.util.Collections;

import jb.SimpleImage;
import joreli.*;

/* Created: 15/9/2014
 * Last Modified: 22/11/2017 (Changes to jb package)
 */
/**
 * A basic soldier, can have a bow or not
 * 
 * @author JB227
 */
public class Unit {

	private SimpleImage pic;
	private Pokemon soldier;
	// where it is
	private int[] coordinates;
	// where it goes, max {1*rank, 1*rank}
	private int[] orders = { 0, 0 };
	private ArrayList<Unit> bait = new ArrayList<>();
	private String homeland;
	private String allegiance;
	// boolean makes it easier, could be an int, different arrows
	private boolean[] quiver;
	private boolean ranged = false;
	/**
	 * The level of actual movement
	 */
	protected static final int CHARGE = 10;
	/**
	 * Maximum range of the bow
	 */
	protected static final int RANGE = 150;

	/**
	 * 
	 * @param p
	 *            the image drawn, should be 5x5 Pix size 3
	 * @param w
	 *            the Pokemon soldier
	 * @param r
	 *            the rank
	 * @param start
	 *            initial coordinates
	 * @param a
	 *            An origin for the unit, from whence they came.
	 */
	public Unit(SimpleImage p, Pokemon w, int[] start, String a) {
		this.pic = p;
		this.soldier = w;
		this.coordinates = start;
		this.pic.setCoordinates(start[0], start[1]);
		this.allegiance = a;
		this.homeland = a;
	}

	/**
	 * Similar to the other constructor, but the Unit can be ranged now
	 * 
	 * @param p
	 *            the Image
	 * @param w
	 *            the Pokemon
	 * @param start
	 *            the inital coordinates
	 * @param a
	 *            the origin, where they began, Mercenary or Hero for example
	 * @param archer
	 *            if the Unit is ranged, the number of arrows is 5*rank
	 */
	public Unit(SimpleImage p, Pokemon w, int[] start, String a, boolean archer) {
		this(p, w, start, a);
		ranged = archer;
		if (ranged) {
			quiver = new boolean[7];
			for (int i = 0; i < quiver.length; i++) {
				quiver[i] = true;
			}
		}
	}

	public Pokemon getSoldier() {
		return soldier;
	}

	public SimpleImage getImage() {
		return pic;
	}

//	public int getRank() {
//		return rank;
//	}

	public boolean archer() {
		return this.ranged;
	}

	public String getAllegiance() {
		return this.allegiance;
	}

	/**
	 * 
	 * @return The number of arrows left
	 */
	public int arrows() {
		if (ranged) {
			int stock = 0;
			for (boolean arrow : quiver) {
				if (arrow)
					stock++;
			}
			return stock;
		}
		return 0;
	}

	/**
	 * Upgrades the current Unit to an archer
	 */
	public void arm() {
		if (!ranged) {
			ranged = true;
			quiver = new boolean[7];
			for (int i = 0; i < quiver.length; i++) {
				quiver[i] = true;
			}
		}
	}

	/**
	 * Reveals the location of the Unit, in array form
	 * 
	 * @return location {x, y} array form
	 */
	public int[] location() {
		int[] c = { coordinates[0], coordinates[1] };
		return c;
	}

	public void orders(int[] o) {
		this.orders = o;
	}

	/**
	 * Updates everything from the orders and resets them, the orders that is
	 */
	public void move() {
		this.coordinates[0] += CHARGE * this.orders[0];
		this.coordinates[1] += CHARGE * this.orders[1];
		pic.move(CHARGE * this.orders[0], CHARGE * this.orders[1]);
		this.orders[0] = 0;
		this.orders[1] = 0;
	}

	/**
	 * Sets the position immediately
	 * 
	 * @param newloc
	 *            {new x, new y}
	 */
	public void drop(int[] newloc) {
		this.coordinates[0] = newloc[0];
		this.coordinates[1] = newloc[1];
		this.pic.setCoordinates(newloc[0], newloc[1]);
	}

	public int[] centre() {
		int[] loc = new int[2];
		// find the centre of the SimpleImage, used in BattleField(Unit)
		// coordinates is the bottom left
		loc[0] = this.coordinates[0] + 8;
		loc[1] = this.coordinates[1] + 8;

		return loc;
	}

	public void joinArmy(String a) {
		this.allegiance = a;
	}

	/**
	 * Works out if another Unit is from the same Army based on colour
	 * 
	 * @param o
	 *            the other Unit
	 * @return true if friends, false otherwise
	 */
	public boolean friendOrFoe(Unit o) {
		return (this.getAllegiance().equals(o.getAllegiance()));
	}

	/**
	 * How fast this unit can move, based on the pokemon
	 * @return max distance this unit can move in a turn
	 */
	public int getSpeed() {
		int speed = this.soldier.getSpeed() / 50;
		if (speed < 0) speed = 1;
		return speed;
	}
	
	/**
	 * Simplifies the distance between Units
	 * 
	 * @param e
	 *            the target coordinates
	 * @return a sensible movement vector
	 */
	public int[] decide(int[] e) {

		int[] move = { 0, 0 };
		int[] me = centre();

		move[0] = e[0] - me[0];
		move[1] = e[1] - me[1];

		// this is roughly the number of moves needed
		move[0] = move[0] / Unit.CHARGE;
		move[1] = move[1] / Unit.CHARGE;

		int mul = getSpeed();

		// clever stuff
		double work[] = { (double) move[0], (double) move[1] };

		// make work[] positive, then convert the return statement

		boolean w0n = false, w1n = false;

		if (work[0] < 0) {
			work[0] = -work[0];
			w0n = true;
		}
		if (work[1] < 0) {
			work[1] = -work[1];
			w1n = true;
		}

		double divi = 0;
		if (work[0] > work[1]) {

			while (work[0] < mul && mul > 0)
				mul--;

			divi = work[0] / (double) mul;
			divi = work[1] / divi;

			// this is the double matrix {mul : divi}
			move[0] = mul;
			move[1] = (int) divi;
			if (w0n)
				move[0] = -move[0];
			if (w1n)
				move[1] = -move[1];
			return move;
		} else {
			while (work[1] < mul && mul > 0)
				mul--;

			divi = work[1] / (double) mul;
			divi = work[0] / divi;

			move[1] = mul;
			move[0] = (int) divi;
			if (w0n)
				move[0] = -move[0];
			if (w1n)
				move[1] = -move[1];
			return move;
		}
	}

	/**
	 * Adds to the list of kills
	 * 
	 * @param d
	 *            the victim
	 */
	public void claim(Unit d) {
		bait.add(d);
	}

	/**
	 * The list of Units that fell by this Unit's hand, or whatever they have
	 * 
	 * @return an Array of the Units slain
	 */
	public Unit[] killList() {
		Unit[] kill = new Unit[bait.size()];

		for (int i = 0; i < kill.length; i++) {
			kill[i] = bait.get(i);
		}
		return kill;
	}

	/**
	 * 
	 * @return The number of kills
	 */
	public int kills() {
		return bait.size();
	}

	/**
	 * Notch! Draw! Loose! Fires an Arrow if possible
	 * 
	 * @return true if the Arrow is let fly, false if either they have no arrows
	 *         or no bow
	 */
	public boolean fire() {
		if (ranged) {
			if (!quiver[0])
				return false;
			for (int i = (quiver.length - 1); i >= 0; i--) {
				if (quiver[i]) {
					quiver[i] = false;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The Unit is hurt and can even die()
	 * 
	 * @param critical
	 *            if true, the damage is doubled
	 */
	public void hit(boolean critical) {
		
		// should do 10% or 20% damage to soldier
		
		int hurt = (int) (this.getSoldier().getHealthMax() * ((critical) ? 0.25 : 0.1));
		
		this.soldier.hurt(hurt);
	}

	/**
	 * Reverses die() and resets armour
	 */
	public void revive() {
		this.rest();
		this.soldier.revive(null);
	}

	/**
	 * Resets the armour
	 */
	public void rest() {
		this.soldier.resetStats();
		this.soldier.restore();
	}

	/**
	 * If they have a bow, the quivers are restocked
	 */
	public void restock() {
		if (ranged) {
			for (int arrow = 0; arrow < quiver.length; arrow++) {
				if (!quiver[arrow])
					quiver[arrow] = true;
			}
		}
	}

	public boolean isDead() {
		return soldier.fainted();
	}

	/**
	 * A Unit can create another... and the Pokemon are related i.e. a pre
	 * evolve, evolution or the same
	 * 
	 * @param direction
	 *            where the new Unit is relative to this one, {0, 1} is above
	 *            for example. The size doesn't matter only the sign
	 * @return the new Unit
	 */
	public Unit spawn(int[] direction) {

		Pokedex relative = new Pokedex();
		// same colour
		SimpleImage p = this.getImage().copy();

		// related Pokemon
		ArrayList<Pokemon> relatives = relative.pokemonRelatedTo(soldier);
		Collections.shuffle(relatives);
		Pokemon sold = relatives.get(0);
		int lvl = relative.minimumLevel(sold);
		sold.setLevel((lvl > soldier.getLevel()) ? soldier.getLevel() : lvl);
		relative.setAttacks(sold);

		// relational position
		int[] area = new int[2];
		for (int i = 0; i < 2; i++) {
			int move = 15;
			if (direction[i] == 0)
				move = 0;
			if (direction[i] < 0)
				move = -15;

			area[i] = this.coordinates[i] + move;
		}

		String or = this.homeland;

		// Same rank and weapon, meaning bow
		return new Unit(p, sold, area, or, this.ranged);
	}

	public String toString() {

		String desc = this.soldier.getName() + "Level : " + soldier.getLevel() + " origin " + this.allegiance + ", kills "
				+ bait.size();

		if (ranged)
			desc += ", number of arrows " + this.arrows();

		if (this.isDead())
			desc += "-inactive";

		return desc;
	}
}
