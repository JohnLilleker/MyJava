package joreli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/* Created: 10/4/2014
 * Last Modified: 20/7/2017 (New updated better team file reading)
 */
/**
 * An abstract class for Pokemon trainers both Biological and Computerised
 * 
 * @author JB227
 */
public abstract class Trainer {

	protected String name;
	protected Pokemon[] team = new Pokemon[TEAM_SIZE];
	/**
	 * Maximum number of pokemon carriable
	 */
	public static final int TEAM_SIZE = 6;
	
	protected List<Item> bag = new ArrayList<>();
	
	// used to indicate what the player will do
	public enum Action {ATTACK, ITEM, SWITCH, RUN} 
	/**
	 * Returned by a Trainer to show what they want to do next.<br>
	 * Either do an Attack, use an Item or switch Pokemon 
	 * @author JB227
	 *
	 */
	public static class Response {
		private Action action;
		private Pokemon next = null;
		private Item item = null;
		private Attack attack = null;
		
		public Response(Pokemon p) {
			action = Action.SWITCH;
			next = p;
		}
		public Response(Item i) {
			action = Action.ITEM;
			item = i;
		}
		public Response(Attack a) {
			action = Action.ATTACK;
			attack = a;
		}
		public Response() {
			action = Action.RUN;
		}
		
		public Action action() { return action; }
		public Pokemon pokemon() { return next; }
		public Attack attack() { return attack; }
		public Item item() { return item; }
	}
	
	/**
	 * Used to set teams and can be used by smart AIs to size up their opponents
	 */
	protected Pokedex pokedex;

	public Trainer(String name) {
		this.name = name;
		pokedex = new Pokedex();
	}
	
	public Trainer(String n, Pokemon[] t) {
		this.name = n;
		int count = 0;
		for (Pokemon p : t) {
			team[count] = p;
			count++;
			if (count == team.length)
				break;
		}
		pokedex = new Pokedex();
	}

	public String getName() {
		return name;
	}
	
	public int getTrainerId() {
		return hashCode();
	}

	public void printBattleInfo() {
		System.out.print(name + ": ");
		for (Pokemon p : team) {
			if (p != null) {
				if (p.fainted()) {
					System.out.print("*");
				} else {
					System.out.print("o");
				}
			} else {
				System.out.print("-");
			}
		}
		System.out.print("\n");
	}

	/**
	 * Builds a team from a given file
	 * 
	 * @param file The file name to read from
	 * 
	 * @return false if the file doesn't work or exist.
	 */
	public boolean setTeamFromFile(String file) {		
		if (!new File(file).exists()) {
			System.err.printf("Unable to read from file %s%n", file);
			return false;
		}
		
		int line = 0;
		int team_member = -1;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){

			String input;

			while((input = reader.readLine()) != null) {
				
				if (input.equals("") || input.startsWith("/")) continue;
				
				String[] parts = input.split("\\s");
				if (parts.length % 2 != 0) {
					throw new Exception("Invalid input length");
				}
				switch(parts[0]) {
				
				case "-p" :
					team_member++;
					if (team_member == TEAM_SIZE) {
						throw new Exception("Too many pokemon");
					}
					Pokemon p = pokedex.pokeSearch(parts[1]);
					if (p == null) {
						throw new Exception("No such pokemon " + parts[1]);
					}
					team[team_member] = p;
					
					for (int i = 2; i < parts.length; i++) {
						switch(parts[i]) {
						
						case "-l":
							p.setLevel(Integer.valueOf(parts[++i]));
							break;
						case "-g":
							if (!p.setGender(Pokemon.Gender.valueOf(parts[++i]))) {
								throw new Exception(p.getSpecies() + " cannot be " + parts[i]);
							}
							break;
						case "-N":
							p.setNickName(parts[++i]);
							break;
						case "-n":
							p.setNature(parts[++i]);
							break;
						case "-I":
							if (p.isHoldingItem()) {
								throw new Exception(p.getName() + " is already holding an item");
							}
							String in = parts[++i].replace("_", " ");
							Item item = pokedex.itemSearch(in);
							if (item == null) {
								throw new Exception("No such item " + in);
							}
							p.giveItem(item);
							break;
						}
					}
					break;
					
				case "-a" :
					if (team_member == -1) {
						throw new Exception("No pokemon to assign attack " + parts[1]);
					}
					String an = parts[1].replace("_", " ");
					Attack a = pokedex.attackSearch(an);
					if (a == null) {
						throw new Exception("No such attack " + an);
					}
					team[team_member].learnAttack(a);
					break;
					
				case "-i" :
					String in = parts[1].replace("_", " ");
					Item i = pokedex.itemSearch(in);
					if (i == null) {
						throw new Exception("No such item " + in);
					}
					this.gainItem(i);
					break;
				
				default :
					throw new Exception("Unknown marker " + parts[0]);
				}
				line++;
			}
			
			reader.close();
		}
		catch(Exception e) {
			System.err.printf("(%s) Error on line %d%n", file, line);
			System.err.println(e.getMessage());
			e.printStackTrace();
			this.bag.clear();
			for (int i = 0; i < team.length; i++) {
				team[i] = null;
			}
			return false;
		}
		return true;
	}
	

	/**
	 * Builds a team from a file, name.txt
	 * 
	 * @return false if the file doesn't work or exist
	 */
	public boolean setTeamFromFile() {
		return setTeamFromFile(this.name + ".txt");
	}
	
	public Pokemon getTeamMember(int m) {
		return team[m];
	}

	public Pokemon getLead() {
		Pokemon lead = team[0];
		if (lead.fainted()) {
			for (Pokemon p : team) {
				if (!p.fainted())
					return p;
			}
		}
		return lead;
	}

	/**
	 * The trainer obtains a Pokemon
	 * 
	 * @param p the Pokemon
	 * @return if the Trainer can keep it, they have less than {@value #TEAM_SIZE} pokemon
	 */
	public boolean catchPokemon(Pokemon p) {
		p.setTrainer(this);
		for (int i = 0; i < TEAM_SIZE; i++) {
			if (team[i] == null) {
				team[i] = p;
				return true;
			}
		}
		return false;
	}

	/**
	 * Select a pokemon to replace the last
	 * @param active
	 * @return the next pokemon
	 */
	public abstract Pokemon selectPokemon(Pokemon active);
	public abstract void levelUp(Pokemon p, int exp, Battle b);

	/**
	 * A random pokemon is released
	 * @param p the current active pokemon
	 * @return
	 */
	public Pokemon randomTeamMember(Pokemon p) {
		
		if (this.actives() <= 1) return null;
		
		int ind = (int) Math.round(Math.random() * (TEAM_SIZE-1));
		
		Pokemon next = null;
		
		do {
			if (team[ind] != null && !team[ind].fainted()) {
				if (!team[ind].equals(p)) {
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
	
	/**
	 * Gives the size of the team
	 * 
	 * @return the number of Pokemon
	 */
	public int teamSize() {
		int members = 0;
		for (Pokemon t : team) {
			if (t != null)
				members++;
		}
		return members;
	}

	/**
	 * The number of awake Pokemon
	 * 
	 * @return
	 */
	public int actives() {
		int members = 0;
		for (Pokemon t : team) {
			if (t != null) {
				if (!t.fainted())
					members++;
			}
		}
		return members;
	}

	public boolean canBattle() {
		for (Pokemon t : team) {
			if (t != null) {
				if (!t.fainted())
					return true;
			}
		}
		return false;
	}

	/**
	 * Makes a team of random pokemon with levels in range of given parameters
	 * @param size number of pokemon in the team
	 * @param minLevel
	 * @param maxLevel
	 * @param pred a Predicate relating to the desired pokemon, i.e. only of a certain type, stage etc
	 */
	public void setTeam(int size, int minLevel, int maxLevel, Predicate<Pokemon> pred) {

		for (int i = 0; i < size; i++) {
			
			int diff = maxLevel - minLevel;
			long rnd = Math.round(Math.random() * diff);
			int level = minLevel + (int) rnd;
			
			Pokemon pokemon = pokedex.pokemonWhere(pred);
			pokemon.setLevel(level);
			pokedex.setAttacks(pokemon);
			
			pokemon.setTrainer(this);
			
			this.team[i] = pokemon;
			
		}
	}
	
	/**
	 * Makes a team of random pokemon with levels in range of given parameters
	 * @param size number of pokemon in the team
	 * @param minLevel
	 * @param maxLevel
	 */
	public void setTeam(int size, int minLevel, int maxLevel) {
		this.setTeam(size, minLevel, maxLevel, p -> true);
	}
	
	/**
	 * Makes a team of random pokemon with the level given
	 * @param size number of pokemon in the team
	 * @param level
	 */
	public void setTeam(int size, int level) {
		this.setTeam(size, level, level);
	}
	
	public abstract Attack attack(Pokemon p);

	/**
	 * The Trainer chooses an action during battle
	 * @param p the active pokemon
	 * @return a response object containing either a Pokemon (switch), Attack (attack), Item (use item) or nothing (concede)
	 */
	public abstract Response action(Pokemon p);
	
	/**
	 * A handy method for establishing if a Trainer is human or not
	 * 
	 * @return true if human false otherwise.
	 */
	public abstract boolean isHuman();

	/**
	 * puts an item in the bag
	 * @param item
	 */
	public boolean gainItem(Item item) {
		return bag.add(item);
	}
	
	/**
	 * Gets an item from the bag without removing it
	 * @param index
	 * @return the item
	 */
	public Item getItem(int index) {
		return bag.get(index);
	}
	
	/**
	 * Removes an Item from the bag
	 * @param index
	 * @return
	 */
	public Item removeItem(int index) {
		return bag.remove(index);
	}
	
	public void viewBag() {
		int i = 0;
		for (Item item : bag) {
			System.out.println(i+1 + ": " + item.getName());
		}
	}

	protected final static String PWORD = "j8l22795";

	/**
	 * Restores the entire team's stats and attacks
	 */
	public void restore() {
		for (Pokemon t : team) {
			if (t != null)
				t.resetStats();
		}
	}

	/**
	 * Heals the entire squad
	 */
	public void revive(Battle arena) {
		for (Pokemon t : team) {
			if (t != null) {
				t.revive(arena);
				t.restore();
			}
		}
	}

	public String toString() {
		String t = name + ": Team";
		for (int i = 0; i < TEAM_SIZE; i++) {
			if (team[i] != null) {
				t += " -" + team[i].getName();
				if (team[i].fainted())
					t += "(f)";
				t += " Level " + team[i].getLevel();
			}
		}
		String bagDesc = "\nBag: ";
		if (this.bag.isEmpty()) {
			bagDesc += "Empty";
		}
		else {
			for (Item i : bag) {
				bagDesc += "\n" + i.getName();
			}
		}
		
		return t + bagDesc;
	}
	public void printTeam() {
		System.out.println("Trainer " + name + ":");
		for (Pokemon p : team) {
			if (p != null)
				System.out.println(p + "\n");
		}
	}
	public void printBag() {
		System.out.println("Trainer " + name + ":");
		for (Item item : bag) {
			System.out.println(item + "\n");
		}
	}
}
