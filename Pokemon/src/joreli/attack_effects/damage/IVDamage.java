package joreli.attack_effects.damage;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;
import joreli.Type;

public class IVDamage extends Damage {

	// IVs don't change, so store this to prevent unnecessary calculation
	private int power = 0;
	private Type type = Type.NORMAL;
	
	// lookup table for types
	final Type[] TABLE = {Type.FIGHTING, Type.FLYING, Type.POISON, Type.GROUND, Type.ROCK, Type.BUG, Type.GHOST, Type.STEEL, Type.FIRE, Type.WATER, Type.GRASS, Type.ELECTRIC, Type.PSYCHIC, Type.ICE, Type.DRAGON, Type.DARK};

	
	public int harm(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		return defaultBehaviour(attacker, attack, defender, arena);
	}

	/**
	 * Uses bit 1 of IVs to get a damage
	 */
	protected int getAttackPower(Attack attack, Pokemon attacker, Pokemon defender, Battle arena) {
		if (power != 0) return power;
		int p = ((bits(attacker, 1) * 40) / 63) + 30;
		power = p;
		return p;
	}
	
	/**
	 * Uses bit 0 of IVs
	 */
	protected Type getAttackType(Attack attack, Pokemon attacker, Battle arena) {
		if (type != Type.NORMAL) return type;
		Type t = TABLE[((bits(attacker, 0) * 15) / 63)];
		type = t;
		return t;
	}
	
	private int bits(Pokemon attacker, int bit_index) {
		// spDef, spAtk, spd, def, atk, hp
		int[] ivs = attacker.getIvs();
		
		// the order used
		int[] indices = {4, 3, 5, 2, 1, 0};
		
		String bits = "";
		
		for (int i : indices) {
			int iv = ivs[i];
			// convert to bits
			String binary = Integer.toBinaryString(iv);
			
			// pad it if too short
			while (binary.length() < (bit_index + 1)) {
				binary = "0" + binary;
			}
			char bit = binary.charAt((binary.length() - 1) - bit_index);
			
			bits += bit;
		}

		int v = Integer.valueOf(bits, 2);
		return v;
	}
	
	public Damage copy() {
		return new IVDamage();
	}

	public String description() {
		return "Power and Type are based on the individual Pokemon's strengths. ";
	}

	public String code() {
		return "ivs";
	}

}
