package joreli;

import java.util.Random;

/* Created: 10/4/2014
 * Last modified: 30/3/2017 (Burn StatAttack)
 */
/**
 * An AI Trainer, chooses at random.
 * 
 * @author JB227
 */
public class ComputerTrainer extends Trainer {

	private Random rand = new Random();

	/** {@inheritDoc} */
	public boolean isHuman() {
		return false;
	}

	public ComputerTrainer(String name) {
		super(name);
	}

	public ComputerTrainer(String n, Pokemon p[]) {
		super(n, p);
	}

	/**
	 * The Computer randomly chooses an attack
	 * 
	 * @param p
	 *            the Pokemon
	 * @return the chosen attack
	 */
	public Attack attack(Pokemon p) {

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

	public Response action(Pokemon p) {
		// basic ais always attack
		// smarter ones could take advantage of items and, after refactoring, switching
		return new Response(attack(p));
	}

	/**
	 * The Computer returns the next available (conscious) Pokemon in the team
	 */
	public Pokemon selectPokemon(Pokemon active) {
		int ind = (int) Math.round(Math.random() * (TEAM_SIZE - 1));

		Pokemon next = null;

		do {
			if (team[ind] != null && !team[ind].fainted()) {
				if (!team[ind].equals(active)) {
					next = team[ind];
				}
			}
			if (next == null) {
				ind++;
				if (ind == TEAM_SIZE) {
					ind = 0;
				}
			}
		} while (next == null);

		return next;
	}

	public void levelUp(Pokemon p, int exp, Battle b) {

		if (p.originalTrainer() != this.getTrainerId())
			exp = (int) (exp * 1.5);

		int additionalexp = exp;
		String evo = "none";

		while (p.gainExp(additionalexp)) {
			String[] attacks = p.newAttacks();
			if (attacks.length != 0) {
				for (String attack : attacks) {
					Attack a = pokedex.attackSearch(attack);
					if (!p.learnAttack(a)) {
						int ind = (int) Math.round(Math.random() * (Pokemon.NO_OF_ATTACKS - 1));
						p.setAtk(a, ind);
					}
				}
			}
			if (evo.equals("none"))
				evo = p.checkEvolve(b.getLocation());
			additionalexp = 0;
		}

		if (!evo.equals("none")) {
			Pokemon next = pokedex.pokeSearch(evo);
			p.evolve(next);
			p = next;
			String[] attacks = p.newAttacks();
			if (attacks.length != 0) {
				for (String attack : attacks) {
					Attack a = pokedex.attackSearch(attack);
					if (!p.learnAttack(a)) {
						int ind = (int) Math.round(Math.random() * (Pokemon.NO_OF_ATTACKS - 1));
						p.setAtk(a, ind);
					}
				}
			}
		}
	}
}