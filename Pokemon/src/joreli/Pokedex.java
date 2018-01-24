package joreli;

/* Created: 16/3/14
 * Last Modified: 3/12/17 (pokemonRelatedTo)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.Random;

import joreli.Pokemon.EggGroup;
import joreli.Pokemon.Evolution;
import joreli.Pokemon.GrowthRate;
import joreli.attack_effects.AttackEffectsCreator;
import joreli.items.ItemParser;

/**
 * This will read from a trio of files, "Attacks List.txt", "Items.txt" and
 * "Pokemon List.txt", and store them in a trio of ArrayLists. Methods will then
 * be used to access the lists, both by search and randomised. It is also used
 * for housekeeping and preparation of the Pokemon.
 *
 * @author JB227
 */
public class Pokedex {

	private ArrayList<Pokemon> pokelist = new ArrayList<Pokemon>();
	private ArrayList<Attack> attacklist = new ArrayList<Attack>();
	// stores TMs in another list. Allows them to be differentiated from normal
	// attacks
	private ArrayList<Attack> TMlist = new ArrayList<Attack>();
	// old favourites, from OldFavourites.txt
	private ArrayList<Pokemon> olddex = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private Random rand = new Random();

	/**
	 * Initialises lists, including older regions
	 */
	public Pokedex() {
		this(true);
	}

	/**
	 *
	 * @param setup
	 *            initialise all lists?
	 */
	public Pokedex(boolean setup) {
		if (setup) {
			this.setUp();
		}
	}

	/**
	 * Has the pokedex been initialised?
	 */
	public boolean hasPokemon() {
		return (pokelist.size() + olddex.size()) > 0;
	}

	/**
	 * Has the attackdex been initialised?
	 */
	public boolean hasAttacks() {
		return attacklist.size() > 0;
	}

	public boolean hasItems() {
		return items.size() > 0;
	}

	/**
	 * Reads from the list(s) of Pokemon
	 */
	public void readPok() {
		readOlder();
	}

	private void readOlder() {

		if (olddex.size() > 0)
			return;

		Pattern evs_reg = Pattern.compile("(\\w+):(\\w+)\\{(.*?)\\}");
		int line = 0;
		int index = 1;

		try (BufferedReader reader = new BufferedReader(new FileReader("src/joreli/Information/OldFavourites.txt"))) {

			String haunt;

			while ((haunt = reader.readLine()) != null) {

				line++;

				if (haunt.startsWith("!"))
					break;
				if (haunt.equals(""))
					continue;
				if (!haunt.startsWith("/")) {
					int comp = 0;
					String[] num = haunt.split("\\s+");

					String n = num[comp++];
					int h = Integer.valueOf(num[comp++]);
					int a = Integer.valueOf(num[comp++]);
					int d = Integer.valueOf(num[comp++]);
					int sa = Integer.valueOf(num[comp++]);
					int sd = Integer.valueOf(num[comp++]);
					int s = Integer.valueOf(num[comp++]);
					Type t1 = Type.valueOf(num[comp++]);
					Type t2 = Type.valueOf(num[comp++]);

					String b = num[comp++];

					Evolution[] evs = null;
					if (!b.equals("none")) {
						String[] list = b.split(",");

						evs = new Evolution[list.length];

						for (int i = 0; i < list.length; i++) {

							Matcher match = evs_reg.matcher(list[i]);
							if (!match.find()) {
								throw new Exception("Evolve descriptor in wrong format");
							}
							String evo = match.group(1);
							String name = match.group(2);
							String precond = match.group(3);
							evs[i] = new Evolution(evo, name, precond.split("&"));
						}
					}

					String g = num[comp++];
					GrowthRate grow = GrowthRate.valueOf(num[comp++]);

					int capture = Integer.valueOf(num[comp++]);
					int base = Integer.valueOf(num[comp++]);

					String home = num[comp++];

					int stage = Integer.valueOf(num[comp++]);

					String[] feet_and_inches = num[comp++].split("'");

					int height = (Integer.valueOf(feet_and_inches[0]) * 12) + Integer.valueOf(feet_and_inches[1]);
					float weight = Float.valueOf(num[comp++]);

					String[] eggs = num[comp++].split(",");
					EggGroup egg1 = EggGroup.valueOf(eggs[0]);
					EggGroup egg2 = EggGroup.NONE;
					if (eggs.length == 2) {
						egg1 = EggGroup.valueOf(eggs[1]);
					}

					String[] atks = new String[num.length - comp];
					for (int i = 0; i < atks.length; i++) {
						atks[i] = num[i + comp];
					}

					Pokemon p = new Pokemon(index++, n, h, a, d, sa, sd, s, t1, t2, evs, g, grow, capture, base, stage,
							height, weight, egg1, egg2);
					p.setCreator("Nintendo");
					p.location(home);
					p.normalise();
					p.setLearnedAttacks(atks);
					olddex.add(p);
				}
			}

			reader.close();

		} catch (Exception e) {
			System.err.println("Pokemon read Exception line " + line);
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Reads a list of Attacks from the "Attacks List.txt" file
	 */
	public void readAtk() {
		if (attacklist.size() > 0)
			return;

		AttackEffectsCreator aec = new AttackEffectsCreator();
		int line = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("src/joreli/Information/Attacks List.txt"))) {

			String bulk;

			while ((bulk = reader.readLine()) != null) {

				line++;

				if (bulk.startsWith("!"))
					break;
				if (bulk.equals(""))
					continue;

				if (!bulk.startsWith("/")) {

					String[] ele = bulk.split("\\s+");

					String how = ele[0];
					String n = ele[1];
					n = n.replace('_', ' ');

					int p = Integer.valueOf(ele[2]);
					int acc = Integer.valueOf(ele[3]);
					int u = Integer.valueOf(ele[4]);

					Type t = Type.valueOf(ele[5]);
					Attack.Category cat = Attack.Category.valueOf(ele[6]);
					Attack.Target who = Attack.Target.valueOf(ele[7]);

					int spd = Integer.valueOf(ele[8]);

					// get the extra stuff
					String[] rest = new String[ele.length - 9];
					for (int i = 0; i < rest.length; i++) {
						rest[i] = ele[9 + i];
					}
					aec.decode(rest, line);

					Attack a = new Attack(n, p, acc, u, t, cat, who, spd, aec.getT(), aec.getD(), aec.getEs(),
							aec.getA());
					if (how.startsWith("TM"))
						TMlist.add(a);
					attacklist.add(a);
				}
			}

			reader.close();
		} catch (Exception e) {
			System.err.println("Attack read Error on line " + line);
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Reads a list of Items from the items info file
	 */
	public void readItems() {
		if (items.size() > 0)
			return;

		int line = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("src/joreli/Information/Items.txt"))) {

			String input;

			while ((input = reader.readLine()) != null) {

				line++;

				if (input.startsWith("!"))
					break;

				if (input.startsWith("/") || input.equals(""))
					continue;

				Item item = ItemParser.parse(input);

				items.add(item);

			}
			reader.close();
		} catch (Exception e) {
			System.err.println("Error on line " + line);
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Prints the contents of pokelist
	 */
	public void printPokemon() {
		int pok = 0;
		for (Pokemon p : pokelist) {
			p.printBase();
			pok++;
		}
		for (Pokemon p : olddex) {
			p.printBase();
			pok++;
		}
		System.out.println("\n" + pok + " Pokemon registered\n");
	}

	public void printOlder() {
		olddex.sort((p1, p2) -> Integer.compare(p1.getNumber(), p2.getNumber()));
		olddex.forEach(p -> p.printBase());
	}

	/**
	 * a print loop using a predicate
	 * 
	 * @param pred
	 *            a predicate
	 */
	public void printPokemonWhere(Predicate<Pokemon> pred) {
		Stream<Pokemon> stream = olddex.stream().filter(pred);
		stream.sorted((p1, p2) -> Integer.compare(p1.getNumber(), p2.getNumber())).forEach(p -> p.printBase());
		System.out.printf("%d pokemon registered%n", stream.count());
	}

	/**
	 * Prints Pokemon by type
	 *
	 * @param t
	 *            the type
	 */
	public void pokePrintOut(Type t) {
		int pok = 0;
		for (Pokemon p : pokelist) {
			if (p.typeMatch(t)) {
				p.printBase();
				pok++;
			}
		}

		for (Pokemon o : olddex) {
			if (o.typeMatch(t)) {
				o.printBase();
				pok++;
			}
		}
		System.out.println("\n" + pok + " " + t + " Pokemon registered\n");
	}

	/**
	 * Prints out all the Pokemon from a region, baring in mind this is the joreli
	 * region
	 *
	 * @param r
	 *            the region
	 */
	public void regionPrint(String r) {
		if (r.equalsIgnoreCase("joreli"))
			this.printPokemon();

		else {
			int pok = 0;
			for (Pokemon p : olddex) {
				if (p.home().equalsIgnoreCase(r)) {
					p.printBase();
					pok++;
				}
			}
			System.out.println("\n" + pok + " Pokemon from " + r + "\n");
		}
	}

	/**
	 * Total number of Pokemon
	 */
	public int pokeCount() {
		int tot = pokelist.size() + olddex.size();
		return tot;
	}

	/**
	 * Number of Pokemon of a certain type
	 *
	 * @param t
	 *            the type
	 */
	public int typeCount(Type t) {
		int type = 0;
		for (Pokemon p : pokelist) {
			if (p.typeMatch(t))
				type++;
		}
		for (Pokemon o : olddex) {
			if (o.typeMatch(t))
				type++;
		}
		return type;
	}

	/**
	 * The number of Pokemon from a region
	 *
	 * @param r
	 *            the region
	 */
	public int regionCount(String r) {
		int reg = 0;
		if (r.equalsIgnoreCase("joreli")) {
			reg = pokelist.size();
		} else {
			for (Pokemon p : olddex) {
				if (p.home().equalsIgnoreCase(r))
					reg++;
			}
		}
		return reg;
	}

	/**
	 * Prints the Pokemon made by a certain creator
	 *
	 * @param n
	 *            the possible creator
	 */
	public int printMadeBy(String n) {
		int pok = 0;
		for (Pokemon p : pokelist) {
			if (p.whoMade().equalsIgnoreCase(n)) {
				p.printBase();
				pok++;
			}
		}
		return pok;
	}

	/**
	 * Sorts all stored pokemon
	 * 
	 * @param comp
	 *            how to sort them
	 */
	public void pokesort(Comparator<Pokemon> comp) {
		ArrayList<Pokemon> ord = pokelist;

		ord.addAll(olddex);
		Collections.sort(ord, comp);
		ord.forEach(p -> p.printBase());
	}

	/**
	 * Only sorts the the older gen Pokemon
	 * 
	 * @param comp
	 *            A comparator
	 */
	public void olderPokeSort(Comparator<Pokemon> comp) {
		Collections.sort(olddex, comp);
		olddex.forEach(p -> p.printBase());
	}

	public void forEachPokemon(Consumer<Pokemon> p) {
		olddex.forEach(p);
	}

	public void forEachAttack(Consumer<Attack> a) {
		attacklist.forEach(a);
	}

	/**
	 * Prints out the contents of attacklist and statlist
	 */
	public void printAttacks() {
		printAttacksWhere(a -> true);
	}

	public void attackSort(Comparator<Attack> comp) {
		Collections.sort(attacklist, comp);
		attacklist.forEach(a -> System.out.println(a + "\n"));
	}

	public void printAttacksWhere(Predicate<Attack> pred) {
		attacklist.stream().filter(pred).forEach(a -> System.out.println(a + "\n"));
		System.out.printf("%d attacks registered%n", attacklist.stream().filter(pred).count());
	}

	public void printItemsWhere(Predicate<Item> pred) {
		items.stream().filter(pred).forEach(i -> System.out.println(i + "\n"));
	}

	public void printItems() {
		printItemsWhere(i -> true);
	}

	/**
	 * An amalgamation of readPok() and readAtk()
	 */
	public void setUp() {
		readOlder();
		readAtk();
		readItems();
	}

	/**
	 * Search for an attack by name
	 * 
	 * @param n
	 * @return
	 */
	public Attack attackSearch(String n) {
		return attackWhere(a -> {
			return a.getName().equalsIgnoreCase(n);
		});
	}

	/**
	 * Finds an attack based on a predicate
	 * 
	 * @param pred
	 * @return an attack if found
	 */
	public Attack attackWhere(Predicate<Attack> pred) {
		Attack at = null;
		Collections.shuffle(attacklist);
		for (Attack a : attacklist) {
			if (pred.test(a)) {
				at = a.copy();
				break;
			}
		}
		return at;
	}

	/**
	 * Finds a pokemon based on a given predicate
	 * 
	 * @param pred
	 *            the test
	 * @return a pokemon if it found one, else null
	 */
	public Pokemon pokemonWhere(Predicate<Pokemon> pred) {
		Pokemon poke = null;
		Collections.shuffle(olddex);
		for (Pokemon p : olddex) {
			if (pred.test(p)) {
				poke = p.copy();
				break;
			}
		}
		if (poke != null)
			return formChange(poke);
		return poke;
	}

	/**
	 * Finds all Pokemon related to the given Pokemon, including a copy of the
	 * passed Pokemon.<br>
	 * A Pokemon is defined as related iff they can be traced to the same basic
	 * form.<br>
	 * If a Pokemon p1 evolves into Pokemon p2, they are related,<br>
	 * but if p2 also evolves into another Pokemon p3, p3 is related to both p2 and
	 * p1.<br>
	 * If Pokemon q1 evolves into q2 or q3, then q2 and q3 are related.
	 * 
	 * @param p
	 *            A Pokemon to query about
	 * @return An ArrayList of Pokemon related to the parameter, as explained above
	 */
	public ArrayList<Pokemon> pokemonRelatedTo(Pokemon p) {
		ArrayList<Pokemon> relatives = new ArrayList<>();
		/*
		 * loop through pokemon if (same) or (p.evolve(q)) or (q.evolve(p)) check chain?
		 * fail on relatedTo(charizard) { charmander, gastly, charmeleon }
		 * 
		 * no chain is more than 3 length {basic -> stage 1 -> stage 2} so compare
		 * relative in evolves?
		 * 
		 * this is easier for pokemon with only one stage difference i.e. riolu,
		 * haunter{middle, so easy}, vulpix
		 */
		for (Pokemon r : olddex) { // works for 1 stage evolutions
			if (r.getSpecies().equalsIgnoreCase(p.getSpecies()))
				relatives.add(r.copy());
			if (r.canEvolve(p))
				relatives.add(r.copy());
			if (p.canEvolve(r))
				relatives.add(r.copy());
		}
		return relatives;
	}

	/**
	 * Calculates the minimum logical level a pokemon can be, meaning the level any
	 * prevolve must have been to become this pokemon
	 * 
	 * @param p
	 *            the pokemon in question
	 * @return the minimum level the prevolve must have been to evolve, or 1 if it
	 *         has no prevolves
	 */
	public int minimumLevel(Pokemon p) {
		int level = 1;
		// TODO
		for (Pokemon pre : olddex) {
			if (pre.canEvolve(p)) {
				Evolution[] evs = pre.getEvolves();
				for (Evolution e : evs) {
					if (e.getName().equalsIgnoreCase(p.getSpecies())) {
						if (e.getMethod() == Evolution.Method.LEVEL) {
							String lvl = e.getMethodDescription().split(" ")[1];
							int i;
							if (lvl.equals("up"))
								i = 1;
							else
								i = Integer.valueOf(lvl);
							if (i > level)
								level = i;
						}
					}
				}
			}
		}
		return level;
	}

	/**
	 * Finds an item based on a given predicate
	 * 
	 * @param pred
	 *            the test
	 * @return an item if it found one, else null
	 */
	public Item itemWhere(Predicate<Item> pred) {
		Item item = null;
		Collections.shuffle(items);
		for (Item i : items) {
			if (pred.test(i)) {
				item = i.copy();
				break;
			}
		}
		return item;
	}

	/**
	 * Searches for an item by name
	 * 
	 * @param name
	 *            the name of the item
	 * @return the item if found, else null
	 */
	public Item itemSearch(String name) {
		return itemWhere(i -> i.getName().equals(name));
	}

	/**
	 * Generates a random attack from attacklist
	 *
	 * @return the attack
	 */
	public Attack randomAttack() {
		Attack learn = null;

		int index = rand.nextInt(attacklist.size());
		Attack a = attacklist.get(index);

		learn = a.copy();
		return learn;
	}

	/**
	 * Picks a random attack common to all pokemon
	 *
	 * @return the attack
	 */
	public Attack randomTM() {
		Attack tm = null;
		int index = rand.nextInt(TMlist.size());
		Attack a = TMlist.get(index);

		tm = a.copy();
		return tm;
	}

	/**
	 * Picks a random Item
	 * 
	 * @return
	 */
	public Item randomItem() {
		Item item = null;
		int index = rand.nextInt(items.size());
		Item i = items.get(index);

		item = i.copy();
		return item;
	}

	/**
	 * Selects a random attack from attacklist of a given type
	 *
	 * @param t
	 *            the desired type
	 * @return the attack
	 */
	public Attack randomAttack(Type t) {
		return attackWhere(a -> {
			return a.getType() == t;
		});
	}

	public static final int TYPE20 = 27;

	/**
	 * Selects a random Pokemon
	 *
	 * @return the Pokemon
	 */
	public Pokemon randomPokemon() {
		return randomLegacy();
	}

	/**
	 * If possible, returns a random Pokemon from before joreli
	 *
	 * @return a pokemon from regions Kanto - Kalos
	 */
	public Pokemon randomLegacy() {
		return pokemonWhere(p -> {
			return true;
		});
	}
	/*
	 * private static final Pokemon sun = new Pokemon("Solarball", 175, 130, 134,
	 * 136, 130, 150, Type.TYPE_20, Type.NONE, null, "0", Pokemon.GrowthRate.SLOW,
	 * 3, 200, 1); private static final Pokemon moon = new Pokemon("Lunarclaw", 175,
	 * 136, 130, 130, 134, 150, Type.TYPE_20, Type.NONE, null, "0",
	 * Pokemon.GrowthRate.SLOW, 3, 200, 1); private static final Attack spark = new
	 * Attack("Spark's desire", 275, 100, 1, Type.TYPE_20, Attack.Category.SPECIAL,
	 * 1); private static final Attack shadow = new Attack("Shadow's purge", 275,
	 * 100, 1, Type.TYPE_20, Attack.Category.PHYSICAL, 1);
	 */

	/**
	 * Generates a random Pokemon based on type
	 *
	 * @param t
	 *            the type
	 * @return the Pokemon
	 */
	public Pokemon randomPokemon(Type t) {
		return pokemonWhere(p -> p.typeMatch(t));
	}

	/**
	 * If possible, returns a random Pokemon from before joreli with a certain type
	 *
	 * @param t
	 *            the type
	 * @return the Pokemon
	 */
	public Pokemon randomLegacy(Type t) {
		return pokemonWhere(p -> {
			return p.typeMatch(t);
		});
	}

	/**
	 * Searches by region, if possible else you will just get a joreli
	 *
	 * @param r
	 *            the region
	 * @return the Pokemon
	 */
	public Pokemon randomByRegion(String r) {
		Pokemon here = null;

		if (r.equalsIgnoreCase("joreli")) {

			Collections.shuffle(pokelist);

			int index = rand.nextInt(pokelist.size());
			Pokemon p = pokelist.get(index);

			here = p.copy();
		}

		else {
			return pokemonWhere(p -> {
				return p.home().equalsIgnoreCase(r);
			});
		}
		return formChange(here);
	}

	protected final String TWORD = "R3l4699";

	/**
	 * Searches for a given Pokemon by name, giving error message if not found
	 *
	 * @param n
	 *            the target
	 * @return the Pokemon if found, null if not.
	 */
	public Pokemon pokeSearch(String n) {
		return pokemonWhere(p -> p.getName().equalsIgnoreCase(n));
	}

	/**
	 * No chance of a form-change here, used by the secret code method in
	 * HumanTrainer
	 *
	 * @param n
	 *            the name
	 * @return the pokemon as it was
	 */
	public Pokemon pokeSearchL(String n) {
		Pokemon target = null;
		for (Pokemon p : pokelist) {
			if (p.getName().equalsIgnoreCase(n)) {
				target = p.copy();
				break;
			}
		}
		return target;
	}

	/**
	 * Assigns a Pokemon attacks based on it's level, with a 5% probability of the
	 * pokemon having a TM/ alias for TM,HM,egg, tutor move
	 *
	 * @param p
	 *            The pokemon to set up. It should have it's level and learned
	 *            attacks set up first
	 */
	public void setAttacks(Pokemon p) {

		String[] atks = p.getLearnedAttacks();

		if (atks[0] == null) {
			int N = Pokemon.NO_OF_ATTACKS;
			int attacks_learnt = (p.getLevel() > N * (N - 1)) ? N : (p.getLevel() / N) + 1;

			for (int i = 0; i < attacks_learnt; i++) {
				Attack attack;
				do {
					if (Math.random() > 0.15) {
						Type pre = (Math.random() < 0.3) ? Type.NORMAL : p.preference();
						attack = randomAttack(pre);
					} else {
						attack = randomTM();
					}
				} while (p.learnAttack(attack));
			}
			return;
		}

		for (int i = 0; i < atks.length; i++) {
			String a = atks[i];
			// no more attacks
			if (a == null)
				break;

			// find Attack a
			Attack atk = attackSearch(a);
			if (atk == null) {
				System.out.println("No such attack " + a);
				continue;
			}
			// Math.random > 0.05, learn Attack a
			if (Math.random() > 0.05 && !p.knowsAttack(a)) {
				p.learnAttack(atk);
			}
			// else choose random TM and learn that
			else {
				Attack tm = null;
				do {
					tm = randomTM();
				} while (p.knowsAttack(tm.getName()));
				p.learnAttack(tm);
			}
		}
	}

	/*
	 * I'm keeping this for future reference
	 *
	 * Triplet Attack a1 = new Attack( "Guardian Call", 100, 5, Type.BUG, 100,
	 * special, 1); // normal, or can't escape?
	 *
	 * spark Attack a2 = new Attack("Rebirth", 0, 5, Type.FIRE, 100, status, 1); //
	 * heal completely, burn opponent
	 *
	 * shadow Attack a3 = new Attack( "Shadow Splice", 90, 5, Type.DARK, 100,
	 * physical, 1); // high critical, recover 50%
	 *
	 * day Attack a4 = new Attack("Light Ray", 120, 5, Type.FIRE, 95, special, 1);
	 * // reduce accuracy of opponent
	 *
	 * night Attack a5 = new Attack( "Darkness Beam", 120, 5, Type.DARK, 95,
	 * special, 1); // reduce accuracy of oppponent
	 *
	 * bubble Attack a6 = new Attack("Psy Flare", 120, 5, Type.PSYCHIC, 95, special,
	 * 1); // reduce accuracy, 10% burn
	 *
	 * ele bubble Attack a7 = new Attack("Draco Eruption", 175, 5, Type.DRAGON, 90,
	 * special, 1); // reduce atk + spatk of user
	 *
	 * ele spark Attack a8 = new Attack("Day Burst", 160, 5, Type.ELECTRIC, 90,
	 * special, 1); // reduce user accuracy + spd
	 *
	 * ele shadow Attack a9 = new Attack("Night Terrors", 160, 5, Type.GHOST, 90,
	 * physical, 1); // reduce user def + spdef
	 */

	/**
	 * Certain Pokemon have different forms and that may have different stats, types
	 * or attacks. This changes the form
	 *
	 * @param p
	 *            the Pokemon to consider
	 * @return either the pokemon originally entered or if it has another form or 6,
	 *         a random choice of them.
	 */
	public Pokemon formChange(Pokemon p) {

		/*
		 * Pokemon that have various forms are Aform Drakendrion Renix, Shadamancer and
		 * Sawasion (their elemental forms have seed 6) old favourites Aegislash and
		 * Giratina
		 */

		Pokemon form = p;

		if (form != null) {

			switch (p.getName()) {

			case "Aform":
				// 50% chance of having a folded form with altered stats (+5 or
				// -5) and types
				double choiceA = Math.random();
				String fold = "";
				if (choiceA > 0.91666666667) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk(), p.getBaseDef() - 5,
							p.getBaseSpAtk(), p.getBaseSpDef(), p.getBaseSpd() + 5, p.getType1(), Type.FLYING,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Plane";
				} else if (choiceA > 0.83333333333) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk(), p.getBaseSpAtk(),
							p.getBaseDef(), p.getBaseSpDef() + 5, p.getBaseSpd() - 5, p.getType1(), Type.GRASS,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Lotus";
				} else if (choiceA > 0.75) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk(), p.getBaseSpAtk() - 5,
							p.getBaseDef(), p.getBaseSpDef(), p.getBaseSpd() + 5, p.getType1(), Type.WATER,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Boat";
				} else if (choiceA > 0.66666666667) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk() - 5, p.getBaseSpAtk(),
							p.getBaseDef() + 5, p.getBaseSpDef(), p.getBaseSpd(), p.getType1(), Type.ROCK,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Ball";
				} else if (choiceA > 0.58333333333) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk(), p.getBaseSpAtk() + 5,
							p.getBaseDef(), p.getBaseSpDef(), p.getBaseSpd() - 5, p.getType1(), Type.PSYCHIC,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Fortune";
				} else if (choiceA > 0.5) {
					form = new Pokemon(p.getNumber(), "Aform", p.getBaseHealth(), p.getBaseAtk() + 5, p.getBaseSpAtk(),
							p.getBaseDef(), p.getBaseSpDef() - 5, p.getBaseSpd(), p.getType1(), Type.FIRE,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					fold = "Chomper";
				}
				form.setFormName(fold + " Aform");
				// else the original will be returned.
				break;

			case "Drakendrion":
				// three distinct forms, each with different stats and different
				// types
				int choiceD = rand.nextInt(3);
				String env = "";
				if (choiceD == 0) {
					form = new Pokemon(p.getNumber(), "Drakendrion", p.getBaseHealth(), p.getBaseAtk() + 10,
							p.getBaseDef(), p.getBaseSpAtk() + 10, p.getBaseSpDef(), p.getBaseSpd(), p.getType1(),
							Type.FIRE, p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(),
							p.getBaseExp(), p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					env = "Volcano";
				} else if (choiceD == 1) {
					form = new Pokemon(p.getNumber(), "Drakendrion", p.getBaseHealth(), p.getBaseAtk(),
							p.getBaseDef() + 10, p.getBaseSpAtk(), p.getBaseSpDef() + 10, p.getBaseSpd(), p.getType1(),
							Type.GRASS, p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(),
							p.getBaseExp(), p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					env = "Jungle";
				} else if (choiceD == 2) {
					form = new Pokemon(p.getNumber(), "Drakendrion", p.getBaseHealth(), p.getBaseAtk(), p.getBaseDef(),
							p.getBaseSpAtk(), p.getBaseSpDef(), p.getBaseSpd() + 20, p.getType1(), Type.WATER,
							p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(),
							p.getStage(), p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(),
							p.getEggGroup2());
					env = "Ocean";
				}
				form.setFormName(env + " Drakendrion");
				break;

			case "Renix":
			case "Shadamancer":
			case "Sawasion":
				// about 2.25% chance of having Elemental form
				// powerful signature move, heightened stats, a third Type, what
				// more could you want?
				double choiceE = Math.random();

				if (choiceE > 0.975) {

					if (p.getSpecies() == "Renix") {
						form = new Pokemon(p.getNumber(), "Renix", p.getBaseHealth() + 20, p.getBaseAtk() + 25,
								p.getBaseDef() + 25, p.getBaseSpAtk() + 3 - 0, p.getBaseSpDef() + 15,
								p.getBaseSpd() + 30, p.getType1(), p.getType2(), p.getEvolves(), p.getGenderBalance(),
								p.getGrowthRate(), p.getCaptureRate(), p.getBaseExp(), p.getStage(),
								p.getHeightInInches(), p.getWeightInKilos(), p.getEggGroup1(), p.getEggGroup2());
						form.setType3(Type.ELECTRIC);
					} else if (p.getSpecies() == "Shadamancer") {
						form = new Pokemon(p.getNumber(), "Shadamancer", p.getBaseHealth() + 20, p.getBaseAtk() + 25,
								p.getBaseDef() + 15, p.getBaseSpAtk() + 20, p.getBaseSpDef() + 25, p.getBaseSpd() + 35,
								p.getType1(), p.getType2(), p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(),
								p.getCaptureRate(), p.getBaseExp(), p.getStage(), p.getHeightInInches(),
								p.getWeightInKilos(), p.getEggGroup1(), p.getEggGroup2());
						form.setType3(Type.GHOST);
					} else if (p.getSpecies() == "Sawasion") {
						form = new Pokemon(p.getNumber(), "Sawasion", p.getBaseHealth() + 30, p.getBaseAtk() + 40,
								p.getBaseDef() + 20, p.getBaseSpAtk() + 50, p.getBaseSpDef() + 25, p.getBaseSpd() + 45,
								p.getType1(), p.getType2(), p.getEvolves(), p.getGenderBalance(), p.getGrowthRate(),
								p.getCaptureRate(), p.getBaseExp(), p.getStage(), p.getHeightInInches(),
								p.getWeightInKilos(), p.getEggGroup1(), p.getEggGroup2());
						form.setType3(Type.DRAGON);
					}
					form.setFormName("Elemental " + form.getSpecies());
				}
				break;

			case "Aegislash":
				if (p.getBaseAtk() == 150) {
					form.setFormName("Aegislash (Blade)");
				} else {
					form.setFormName("Aegislash (Shield)");
				}
				break;
			case "Giratina":
				if (p.getBaseAtk() == 120) {
					form.setFormName("Giratina (Origin)");
				} else {
					form.setFormName("Giratina (Altered)");
				}
				break;
			}
		}
		form.setCreator(p.whoMade());
		return form;
	}

	/**
	 * Gets a number of pokemon, returning them as an array
	 * 
	 * @param num
	 *            how many pokemon to be returned
	 * @param level
	 *            what level to set them at
	 * @return an array of pokemon
	 */
	public Pokemon[] getList(int num, int level) {
		return getList(num, level, p -> {
			return true;
		});
	}

	/**
	 * Gets a number of pokemon, returning them as an array
	 * 
	 * @param num
	 *            how many pokemon to be returned
	 * @param level
	 *            what level to set them at
	 * @param pred
	 *            a predicate to filter by
	 * @return an array of pokemon
	 */
	public Pokemon[] getList(int num, int level, Predicate<Pokemon> pred) {
		Pokemon[] res = new Pokemon[num];

		int i = 0;
		while (i < num) {
			Collections.shuffle(olddex);
			int j = 0;
			for (j = 0; j < olddex.size() && i < num; j++) {
				Pokemon p = olddex.get(j);
				if (pred.test(p)) {
					res[i] = formChange(p.copy());
					res[i].setLevel(level);
					i++;
				}
			}
		}

		return res;
	}

	public ArrayList<Pokemon> getAll(int level, Predicate<Pokemon> pred) {
		ArrayList<Pokemon> res = new ArrayList<>();
		for (Pokemon p : olddex) {
			if (pred.test(p)) {
				Pokemon r = formChange(p.copy());
				r.setLevel(level);
				res.add(r);
			}
		}
		return res;
	}

	public static void main(String args[]) {
		Pokedex dex = new Pokedex();

		for (int i = 0; i < 10; i++) {
			Pokemon ee = dex.randomPokemon();
			System.out.println(ee.getName() + " -> " + dex.minimumLevel(ee));
		}

		// verify every pokemon's evolve, vaporeon was spelt wrong

		// Pokemon p = dex.randomPokemon();
		// p.setLevel(50);
		// Pokemon t = dex.randomLegacy();
		// t.setLevel(50);
		//
		// Attack x = dex.randomAttack();
		// Attack y = dex.randomAttack();
		//
		// Battle b = new Battle();
		// b.setLogMethod(Battle.LOG);
		//
		// p.attack(x, t, b);
		//
		// ArrayList<String> log1 = b.getLatestLogs();
		//
		// t.attack(y, p, b);
		//
		// System.out.println("Initial logs");
		// System.out.println(log1);
		//
		// System.out.println("Secondary logs");
		// System.out.println(b.getLatestLogs());
		//
		// System.out.println("All logs");
		// System.out.println(b.getLogs());

		// dex.getAll(50, p -> p.getHeightInInches() >= 67).stream().sorted((Pokemon p1,
		// Pokemon p2) -> {
		// return
		// Float.valueOf(p1.getWeightInPounds()).compareTo(Float.valueOf(p2.getWeightInPounds()));
		// }).forEach(p -> {
		// System.out.printf("%s : %d'%d''%n", p.getName(), p.getHeightInInches()/12,
		// p.getHeightInInches()%12);
		// });

		// dex.printAttacksWhere(a -> a.getType() == Type.ICE && a.status());

		// dex.forEachPokemon((Pokemon p) -> {
		// String[] ats = p.getLearnedAttacks(100);
		// boolean canLearn = false;
		// for (String a : ats) {
		// if (a != null) canLearn = true;
		// }
		// if (canLearn) {
		// System.out.printf("%s ->\n\t%s\n\n", p.getName(), dex.attackSearch(ats[0]));
		// }
		// });

		// dex.forEachPokemon(p -> System.out.printf("%s - %.2fm %.2fkg%n", p.getName(),
		// p.getHeightInMetres(), p.getWeightInKilos()));
		// dex.pokesort((p1,p2) ->
		// Integer.valueOf(p1.getHeightInInches()).compareTo(Integer.valueOf(p2.getHeightInInches())));
		// dex.printPokemonWhere(p -> p.getHeightInInches() >= 67);
		// dex.printPokemonWhere(p -> p.isEggGroup(Pokemon.EggGroup.DRAGON));
		// dex.printAttacksWhere(a -> a.getMaxPP() < 15);

		// dex.printPokemonWhere(p -> p.getTotal() >= 600);

		// dex.olderPokeSort((p1, p2) -> {
		// return
		// Integer.valueOf(p2.getTotal()).compareTo(Integer.valueOf(p1.getTotal()));
		// });

		/*
		 * Pokemon[] list = dex.getList(20);
		 * 
		 * FileWriter f = null; BufferedWriter write = null; try { f = new
		 * FileWriter("list1.txt"); write = new BufferedWriter(f); for (Pokemon p :
		 * list) { write.write(p.getName() + "\n"); } write.close(); f.close(); } catch
		 * (IOException e) { e.printStackTrace(); } finally {
		 * 
		 * try { if (write != null) write.close(); if (f != null) f.close(); } catch
		 * (IOException e) { e.printStackTrace(); } } System.out.println("Done");
		 */
	}
}