package joreli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Scanner;

/* Created: 24/4/14
 * Last Modified: 24/3/17 (Type enum)
 */

/**
 * A class to compare types and return effects such as resistance or weakness
 *
 * @author JB227
 */
public class TypeCompare {

	private ArrayList<Type> weak = new ArrayList<>();
	private ArrayList<Type> res = new ArrayList<>();
	private ArrayList<Type> not = new ArrayList<>();

	/**
	 * Finds the weaknesses and strengths of a certain type and stores them in lists
	 *
	 * @param t
	 *            the Type
	 */
	public void findWeakness(Type t) {
		// Clears in case of previous usage
		weak.removeAll(weak);
		res.removeAll(res);
		not.removeAll(not);

		// Switch the type and add weaknesses and otherwise to the lists
		switch (t) {
		case NORMAL:
			weak.add(Type.FIGHTING);
			not.add(Type.GHOST);
			break;

		case FIGHTING:
			weak.add(Type.FLYING);
			weak.add(Type.PSYCHIC);
			weak.add(Type.FAIRY);
			res.add(Type.ROCK);
			res.add(Type.BUG);
			res.add(Type.DARK);
			break;

		case GHOST:
			weak.add(Type.GHOST);
			weak.add(Type.DARK);
			res.add(Type.POISON);
			res.add(Type.BUG);
			res.add(Type.LIFE);
			not.add(Type.NORMAL);
			not.add(Type.FIGHTING);
			break;

		case FIRE:
			weak.add(Type.WATER);
			weak.add(Type.GROUND);
			weak.add(Type.ROCK);
			res.add(Type.GRASS);
			res.add(Type.BUG);
			res.add(Type.STEEL);
			res.add(Type.FIRE);
			res.add(Type.FAIRY);
			res.add(Type.LIFE);
			res.add(Type.ICE);
			break;

		case WATER:
			weak.add(Type.GRASS);
			weak.add(Type.ELECTRIC);
			weak.add(Type.LIFE);
			res.add(Type.STEEL);
			res.add(Type.FIRE);
			res.add(Type.WATER);
			res.add(Type.ICE);
			break;

		case GRASS:
			weak.add(Type.FIRE);
			weak.add(Type.POISON);
			weak.add(Type.FLYING);
			weak.add(Type.BUG);
			weak.add(Type.ICE);
			weak.add(Type.LIFE);
			res.add(Type.GROUND);
			res.add(Type.WATER);
			res.add(Type.GRASS);
			res.add(Type.ELECTRIC);
			break;

		case LIFE:
			weak.add(Type.GHOST);
			weak.add(Type.DARK);
			weak.add(Type.FIRE);
			res.add(Type.LIFE);
			res.add(Type.WATER);
			res.add(Type.GRASS);
			res.add(Type.POISON);
			res.add(Type.FAIRY);
			break;

		case FAIRY:
			weak.add(Type.LIFE);
			weak.add(Type.POISON);
			weak.add(Type.STEEL);
			res.add(Type.FIGHTING);
			res.add(Type.BUG);
			res.add(Type.DARK);
			not.add(Type.DRAGON);
			break;

		case STEEL:
			weak.add(Type.FIGHTING);
			weak.add(Type.GROUND);
			weak.add(Type.FIRE);
			res.add(Type.LIFE);
			res.add(Type.NORMAL);
			res.add(Type.FLYING);
			res.add(Type.ROCK);
			res.add(Type.BUG);
			res.add(Type.STEEL);
			res.add(Type.GRASS);
			res.add(Type.ICE);
			res.add(Type.DRAGON);
			res.add(Type.FAIRY);
			res.add(Type.PSYCHIC);
			not.add(Type.POISON);
			break;

		case POISON:
			weak.add(Type.PSYCHIC);
			weak.add(Type.GROUND);
			res.add(Type.FIGHTING);
			res.add(Type.POISON);
			res.add(Type.BUG);
			res.add(Type.GRASS);
			res.add(Type.FAIRY);
			break;

		case ROCK:
			weak.add(Type.FIGHTING);
			weak.add(Type.GROUND);
			weak.add(Type.WATER);
			weak.add(Type.GRASS);
			res.add(Type.NORMAL);
			res.add(Type.FLYING);
			res.add(Type.POISON);
			res.add(Type.FIRE);
			break;

		case GROUND:
			weak.add(Type.WATER);
			weak.add(Type.GRASS);
			weak.add(Type.ICE);
			res.add(Type.POISON);
			res.add(Type.ROCK);
			not.add(Type.ELECTRIC);
			break;

		case DRAGON:
			weak.add(Type.DRAGON);
			weak.add(Type.FAIRY);
			weak.add(Type.ICE);
			res.add(Type.FIRE);
			res.add(Type.WATER);
			res.add(Type.ELECTRIC);
			res.add(Type.GRASS);
			break;

		case PSYCHIC:
			weak.add(Type.GHOST);
			weak.add(Type.BUG);
			weak.add(Type.DARK);
			res.add(Type.FIGHTING);
			res.add(Type.PSYCHIC);
			break;

		case DARK:
			weak.add(Type.FIGHTING);
			weak.add(Type.BUG);
			weak.add(Type.FAIRY);
			res.add(Type.LIFE);
			res.add(Type.GHOST);
			res.add(Type.DARK);
			not.add(Type.PSYCHIC);
			break;

		case ICE:
			weak.add(Type.FIGHTING);
			weak.add(Type.ROCK);
			weak.add(Type.STEEL);
			weak.add(Type.FIRE);
			res.add(Type.ICE);
			break;

		case FLYING:
			weak.add(Type.ROCK);
			weak.add(Type.ICE);
			weak.add(Type.ELECTRIC);
			res.add(Type.FIGHTING);
			res.add(Type.GRASS);
			res.add(Type.BUG);
			not.add(Type.GROUND);
			break;

		case ELECTRIC:
			weak.add(Type.GROUND);
			res.add(Type.FLYING);
			res.add(Type.STEEL);
			res.add(Type.ELECTRIC);
			break;

		case BUG:
			weak.add(Type.LIFE);
			weak.add(Type.FIRE);
			weak.add(Type.FLYING);
			weak.add(Type.ROCK);
			res.add(Type.FIGHTING);
			res.add(Type.GROUND);
			res.add(Type.GRASS);
			break;

		case TYPE_20:
			weak.add(Type.TYPE_20);
			break;
		case NONE:
			break;
		}
	}

	/**
	 * A quick check for Type immunity
	 *
	 * @param p
	 *            The defending Pokemon
	 * @param t
	 *            The offending Type
	 * @return True if the given Pokemon is immune to the given type, false
	 *         otherwise
	 */
	public static boolean isImmune(Pokemon p, Type t) {
		switch (t) {
		case DRAGON:
			return p.typeMatch(Type.FAIRY);
		case ELECTRIC:
			return p.typeMatch(Type.GROUND);
		case FIGHTING:
		case NORMAL:
			return p.typeMatch(Type.GHOST);
		case GHOST:
			return p.typeMatch(Type.NORMAL);
		case GROUND:
			return p.typeMatch(Type.FLYING);
		case POISON:
			return p.typeMatch(Type.STEEL);
		case PSYCHIC:
			return p.typeMatch(Type.DARK);
		default:
			return false;
		}
	}

	public final static String ZWORD = "Guy D4ng3r0us";

	/**
	 * Returns a multiplier based on type relations
	 *
	 * @param t
	 *            the type of the Pokemon
	 * @param a
	 *            the type of the Attack
	 * @return the multiplier
	 */
	public float typeMultiplier(Type t, Type a) {

		float mult = 1;

		findWeakness(t);

		if (weak.contains(a))
			mult = mult * 2;

		if (res.contains(a))
			mult = mult * 0.5f;

		if (not.contains(a))
			mult = mult * 0;

		return mult;
	}

	public float typeCompare(Pokemon p, Type t) {
		float mult = 1;

		mult *= typeMultiplier(p.getType1(), t);
		mult *= typeMultiplier(p.getType2(), t);
		mult *= typeMultiplier(p.getType3(), t);

		return mult;
	}

	/**
	 * Returns a string of the effects different types have on a given Pokemon.
	 * Calculates for each type
	 *
	 * @param p
	 *            the Pokemon
	 * @param e
	 *            either weakness, resistance or no effect
	 * @return the String describing the effect e
	 */
	public String typeComparison(Pokemon p, String e) {

		String weakness = "";
		String resistance = "";
		String nowt = "";
		String effect = null;

		ArrayList<Type> compareW = new ArrayList<>();
		ArrayList<Type> compareR = new ArrayList<>();
		ArrayList<Type> compareN = new ArrayList<>();

		// Store the weaknesses as arrayLists
		findWeakness(p.getType1());
		compareW.addAll(weak);
		compareR.addAll(res);
		compareN.addAll(not);

		findWeakness(p.getType2());
		compareW.addAll(weak);
		compareR.addAll(res);
		compareN.addAll(not);

		findWeakness(p.getType3());
		compareW.addAll(weak);
		compareR.addAll(res);
		compareN.addAll(not);

		// cancel weakness and resistance e.g a Water and Flying type isn't weak
		// to grass types
		ListIterator<Type> iterate = compareR.listIterator();
		while (iterate.hasNext()) {
			Type t = iterate.next();
			;
			if (compareW.contains(t)) {
				compareW.remove(t);
				iterate.remove();
			}
		}

		// no effect is constant, but cancels weakness and resistance
		for (Type t : compareN) {
			if (compareW.contains(t))
				compareW.remove(t);
		}
		for (Type t : compareN) {
			if (compareR.contains(t))
				compareR.remove(t);
		}
		for (Type t : compareN) {
			if (compareW.contains(t))
				compareW.remove(t);
		}
		for (Type t : compareN) {
			if (compareR.contains(t))
				compareR.remove(t);
		}

		Collections.sort(compareW);
		// for (int i = 0; i < (compareW.size()-1); i++) {
		// if (compareW.get(i).equals(compareW.get(i+1))) {
		// compareW.remove(i+1);
		// compareW.set(i, compareW.get(i) + ".2");
		// }
		// }
		Collections.sort(compareR);
		// for (int i = 0; i < (compareR.size()-1); i++) {
		// if (compareR.get(i).equals(compareR.get(i+1))) {
		// compareR.remove(i+1);
		// compareR.set(i, compareR.get(i) + ".2");
		// }
		// }
		Collections.sort(compareN);

		// the strings
		if (compareW.size() > 0) {
			weakness += compareW.get(0);
			for (int i = 1; i < compareW.size(); i++) {
				weakness += ", " + compareW.get(i);
			}
		} else
			weakness = "No weakness";

		if (compareR.size() > 0) {
			resistance += compareR.get(0);
			for (int i = 1; i < compareR.size(); i++) {
				resistance += ", " + compareR.get(i);
			}
		} else
			resistance = "No resistance";

		if (compareN.size() > 0) {
			nowt += compareN.get(0);
			for (int i = 1; i < compareN.size(); i++) {
				nowt += ", " + compareN.get(i);
			}
		} else
			nowt = "Everything has an effect";

		switch (e) {
		case "weak":
		case "weakness":
			effect = weakness;
			break;

		case "res":
		case "resistance":
		case "strong":
		case "resistant":
			effect = resistance;
			break;

		case "no effect":
		case "nothing":
			effect = nowt;
			break;
		}

		return effect;
	}

	public static void main(String[] args) {

		Scanner typer = new Scanner(System.in);
		char goAgain = 'y';

		while (goAgain == 'y') {

			Type t1 = Type.NORMAL;
			Type t2 = Type.NONE;

			System.out.print("Do you want to select types? ");
			char question = typer.nextLine().charAt(0);
			if (question == 'y') {
				System.out.print("2 types? ");
				char enquire = typer.nextLine().charAt(0);

				do {
					System.out.print("Enter First Type: ");
					t1 = Type.fromString(typer.nextLine());
				} while (t1 == Type.NONE);

				if (enquire == 'y') {
					do {
						System.out.print("Enter Second Type: ");
						t2 = Type.fromString(typer.nextLine());
					} while (t2 == Type.NONE);
				} else
					t2 = Type.NONE;
			}

			Pokemon really = new Pokemon("test", 0, 0, 0, 0, t1, t2);

			System.out.print("\n" + t1);
			if (t2 != Type.NONE && t2 != t1)
				System.out.println(" / " + t2);
			else
				System.out.print("\n");
			System.out.println(really.getTypeEffects());

			System.out.print("Try Again? ");
			goAgain = typer.nextLine().charAt(0);
		}
		typer.close();
	}
}
