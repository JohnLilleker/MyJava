package joreli;

import java.util.Random;

/**
 * Generates natures based on information, either the name or the effect
 * 
 * @author JB227
 *
 */
public class NatureGen {

	public static String[] natures = {
			// Neutral
			"Hardy",
			"Docile",
			"Serious",
			"Bashful",
			"Quirky",

			// Atk
			"Lonely", // def
			"Adamant", // spatk
			"Naughty", // spdef
			"Brave", // spd

			// Def
			"Bold", // atk
			"Impish", // spatk
			"Lax", // etc
			"Relaxed",

			// SpAtk
			"Modest", "Mild", "Rash", "Quiet",

			// SpDef
			"Calm", "Gentle", "Careful", "Sassy",

			// Spd
			"Timid", "Hasty", "Jolly", "Naive" };

	public static boolean isNature(String n) {
		for (String nat : natures) {
			if (nat.equals(n)) {
				return true;
			}
		}
		return false;
	}

	public static String nameNature(float[] n) {
		if (n.length != 5)
			return "UNKNOWN";

		// use the above list as a guide
		for (int i = 0; i < n.length; i++) {
			if (n[i] > 1) {
				int c = 1;
				for (int j = 0; j < n.length; j++) {
					if (i != j) {
						if (n[j] < 1) {
							return natures[((4 * (i + 1)) + c)];
						}
						c++;
					}
				}
			}
		}
		for (int i = 0; i < n.length; i++) {
			if (n[i] != 1)
				return "UNKNOWN";
		}
		Random r = new Random();
		return natures[r.nextInt(4)];
	}

	public static float[] createNature(String n) {
		float[] nat = { 1, 1, 1, 1, 1 };
		for (int i = 0; i < natures.length; i++) {
			if (n.equalsIgnoreCase(natures[i])) {
				// neutral natures
				if (i < 5) {
					return nat;
				}
				int j = i - 5;
				// the stat to be increased
				int upStat = j / 4;
				nat[upStat] = 1.1f;

				// stat to decrease
				int downStat = j - (upStat * 4);
				if (downStat >= j) {
					downStat++;
				}
				nat[downStat] = 0.9f;

				return nat;
			}
		}
		return nat;
	}

	public static void main(String[] args) {
		float[] nature = { 1.1f, 0.9f, 1, 1, 1 };
		System.out.println(nameNature(nature));
	}

}
