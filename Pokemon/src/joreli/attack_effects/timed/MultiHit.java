package joreli.attack_effects.timed;

import joreli.Attack;
import joreli.Battle;
import joreli.Pokemon;

public class MultiHit extends Timed {

	private int minHit;
	private int maxHit;

	/**
	 * 
	 * @param min
	 *            minimum number of hits
	 * @param max
	 *            maximum number of hits
	 */
	public MultiHit(int min, int max) {
		minHit = min;
		maxHit = max;
	}

	/**
	 * Hits multiple times
	 */
	public void use(Pokemon attacker, Attack attack, Pokemon defender, Battle arena) {
		arena.log(attacker.getName() + " used " + attack.getName());

		int hits = hitCount();
		int i;

		for (i = 0; i < hits && !defender.fainted(); i++) {
			int sucess = attack.getDamageEffect().harm(attacker, attack, defender, arena);
			if (sucess < 1 && !attack.status())
				break;
		}
		if (i > 0) {
			String plural = (i > 1) ? " times" : " time";
			arena.log("Hit " + i + plural);
		}

		attack.use();
	}

	public Timed copy() {
		return new MultiHit(minHit, maxHit);
	}

	/**
	 * Generate number of hits this turn
	 * 
	 * @return a number hit such that min <= hit <= max
	 */
	private int hitCount() {
		if (maxHit == minHit)
			return minHit;

		// 3/8 chance of hitting 2-3 times, 1/8 chance of hitting 4-5 times
		if (minHit == 2 && maxHit == 5) {
			double r = Math.random();
			if (r < 0.375)
				return 2;
			if (r < 0.75)
				return 3;
			if (r < 0.875)
				return 4;
			return 5;
		}

		int diff = maxHit - minHit;
		long rnd = Math.round(Math.random() * diff);

		return minHit + (int) rnd;
	}

	public String description() {
		String times = (minHit == maxHit) ? String.valueOf(maxHit) : (minHit + "-" + maxHit);
		return "Hits " + times + " times. ";
	}

	public String code() {
		return "multihit " + minHit + " " + maxHit;
	}
}
