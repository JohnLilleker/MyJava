package joreli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/* Created: 23/3/14
 * Last Modified: 13/7/17 (Responses)
 */
/**
 * A Human controlled player
 * 
 * @author JB227
 */
public class HumanTrainer extends Trainer {

	private BufferedReader typer;

	public HumanTrainer(String n) {
		super(n);
		typer = new BufferedReader(new InputStreamReader(System.in));
	}

	public HumanTrainer(String n, Pokemon[] t) {
		super(n, t);
		typer = new BufferedReader(new InputStreamReader(System.in));
	}

	/** {@inheritDoc} */
	public boolean isHuman() {
		return true;
	}

	/*
	 	Elementals
		else if (p.equals(RWORD)) {
			if (codeR)
				System.out.println(repete);
			else {
				Pokemon legend = all.pokeSearchL("Renix");
				mega = new Pokemon("Elemental Renix", legend.getBaseHealth() + 20, legend.getBaseAtk() + 25,
						legend.getBaseDef() + 25, legend.getBaseSpAtk() + 30, legend.getBaseSpDef() + 15,
						legend.getBaseSpd() + 30, Type.FIRE, Type.FLYING, null, "0", Pokemon.GrowthRate.SLOW, 3, 100, 1);
				mega.setType3(Type.ELECTRIC);
				mega.setCreator(legend.whoMade());
				System.out.println(mega.getName() + " unlocked!");
				codeR = true;
			}
		}

		else if (p.equals(all.randomAttack().SHWORD)) {
			if (codeSh)
				System.out.println(repete);
			else {
				Pokemon legend = all.pokeSearchL("Shadamancer");
				mega = new Pokemon("Elemental Shadamancer", legend.getBaseHealth() + 20, legend.getBaseAtk() + 25,
						legend.getBaseDef() + 15, legend.getBaseSpAtk() + 20, legend.getBaseSpDef() + 25,
						legend.getBaseSpd() + 35, Type.DARK, Type.PSYCHIC, null, "0", Pokemon.GrowthRate.SLOW, 3, 100, 1);
				mega.setType3(Type.GHOST);
				mega.setCreator(legend.whoMade());
				System.out.println(mega.getName() + " unlocked!");
				codeSh = true;
			}
		}

		else if (p.equals(all.randomPokemon().SAWORD)) {
			if (codeSa)
				System.out.println(repete);
			else {
				Pokemon legend = all.pokeSearchL("Sawasion");
				mega = new Pokemon("Elemental Sawasion", legend.getBaseHealth() + 30, legend.getBaseAtk() + 30,
						legend.getBaseDef() + 20, legend.getBaseSpAtk() + 50, legend.getBaseSpDef() + 25,
						legend.getBaseSpd() + 45, Type.FIRE, Type.PSYCHIC, null, "0", Pokemon.GrowthRate.SLOW, 3, 100, 1);
				mega.setType3(Type.DRAGON);
				mega.setCreator(legend.whoMade());
				System.out.println(mega.getName() + " unlocked!");
				codeSa = true;
			}
		}

		else if (p.equals(Trainer.PWORD)) {
			if (codeP)
				System.out.println(repete);
			else {
				mega = this.pokedex.pokeSearch("Pikachu");
				mega.setNickName("Black Ear");
				mega.setCreator("Nintendo/REL");
				System.out.println("A Pikachu with a black ear came up to you...");
				codeP = true;
			}
		}

		else if (p.equals(TypeCompare.ZWORD)) {
			if (codeZ)
				System.out.println(repete);
			else {
				mega = new Pokemon("Elemental Zekiram", 250, 126, 113, 104, 126, 274, Type.DRAGON, Type.ELECTRIC,
						null, "0", Pokemon.GrowthRate.FLUCTUATING, 3, 110, 1);
				mega.setType3(Type.FIRE);
				mega.setCreator("Imangi Studios/JBL");
				mega.learnAttack(new Attack("Idol's Curse", 150, 75, 5, Type.TYPE_20, Attack.Category.PHYSICAL, 2));
				mega.learnAttack(new Attack("Trip", 25, 95, 15, Type.GROUND, Attack.Category.PHYSICAL, 3,
						new StatEffect("spd", -2, Effect.Target.TARGET, 100)));
				mega.learnAttack(all.randomAttack(Type.GRASS));
				mega.learnAttack(all.randomAttack(Type.FIRE));
				mega.learnAttack(all.randomAttack(Type.WATER));
				System.out.println("I think someone stole the Idol again...");
				codeZ = true;
			}
		}

		else if (p.equals(Battle.R_LEDGE)) {
			if (codeTy)
				System.out.println(repete);
			else {
				mega = all.pokeSearch("Typhlosion");
				mega.setNickName("Ty");
				mega.learnAttack(new Attack("Eruption", 150, 100, 3, Type.FIRE, Attack.Category.SPECIAL, 1));
				mega.learnAttack(new Attack("Rock Smash", 40, 100, 25, Type.FIGHTING, Attack.Category.PHYSICAL, 1,
						new StatEffect("def", -1, Effect.Target.TARGET, 50)));
				mega.learnAttack(new Attack("Flamethrower", 90, 100, 10, Type.FIRE, Attack.Category.SPECIAL, 1,
						new ConditionEffect(Condition.BURN, 10, Effect.Target.TARGET)));
				mega.learnAttack(new Attack("Lava Plume", 80, 100, 13, Type.FIRE, Attack.Category.SPECIAL, 1,
						new ConditionEffect(Condition.BURN, 30, Effect.Target.TARGET)));
				mega.setNature("Quirky");
				mega.setGender(Pokemon.Gender.MALE);
				System.out.println("A girl gave you a Pokemon...");
				codeTy = true;
			}
		}
	*/

	/**
	 * Prompts the player for an attack with additional info about the attack
	 * via battleInfo(energy) or otherwise
	 * 
	 * @param p
	 *            the active Pokemon
	 * @return the chosen attack
	 */
	public Attack attack(Pokemon p) {

		if (p.isCharging())
			return p.chargingAttack();

		Attack atset[] = p.getAtkSet();

		Attack attack = null;
		boolean choiceMade = false;
		
		while (!choiceMade) {
			System.out.print("Choose an attack,");

			for (int i = 0; i < atset.length; i++) {
				if (atset[i] != null) {
					System.out.print(" " + (i + 1) + ". " + atset[i].getName());
				} else {
					System.out.print(" " + (i + 1) + ". -");
				}
			}
			System.out.print(": ");

			int choice = -1;
			boolean str = false;
			String init = read();

			if (init.equalsIgnoreCase("back")) {
				break;
			}
			
			for (int i = 0; i < atset.length; i++) {
				if (atset[i] != null) {
					if (init.equalsIgnoreCase(atset[i].getName())) {
						str = true;
						choice = i;
					}
				}
			}

			if (!str) {
				try {
					choice = Integer.parseInt(init) - 1;
				} catch (NumberFormatException e) {
					choice = -1;
				}
			}
			if (choice >= 0 && choice < Pokemon.NO_OF_ATTACKS) {
				Attack temp = p.getAttack(choice);
				if (temp != null) {
					if (!temp.exhausted()) {

						System.out.println(temp);
						System.out.print("Are you sure? (y/n) ");
						char finalAnswer = read().charAt(0);
						
						if (finalAnswer == 'y') {
							attack = temp;
							choiceMade = (attack != null);
						}
					} else
						System.out.println("Cannot use " + temp.getName());
				}
			}
		}
		System.out.println();
		return attack;
	}
	
	public Response action(Pokemon p) {
		
		if (p.isCharging()) return new Response(p.chargingAttack());
		
		
		System.out.println(name + " your turn");
		
		p.printHealth(true);
		
		String help = "Options:\n"
				+ " - Attack : choose an attack\n"
				+ " - Item : use an item from the bag\n"
				+ " - Switch : switch to another pokemon in your team\n"
				+ " - Run : concede the battle\n"
				+ " - Help : display this list";
		
		Response res = null;
		System.out.println(help);
		
		do {
			System.out.print("What do you want to do? ");
			String action = read().toLowerCase();
			
			switch(action) {
			case "attack":
				if (p.cantMove()) {
					res =  new Response(p.getAttack(0));
				}
				else {
					Attack attack = this.attack(p);
					if (attack != null) res = new Response(attack);
					break;
					
				}
			case "item":
				Item item = this.selectItem();
				if (item != null) res = new Response(item);
				break;
			case "switch":
				if (p.cantMove() || !p.canEscape()) {
					System.out.printf("%s can't be switched out!%n", p.getName());
					continue;
				}
				Pokemon pokemon = selectPokemon(p);
				if (pokemon != null && !p.equals(pokemon)) res = new Response(pokemon);
				break;
			case "run":
				System.out.print("Are you sure (y/n)? You will concede the battle and lose ");
				if (read().toLowerCase().charAt(0) == 'y') res = new Response();
				break;
			case "help":
				System.out.println(help);
				break;
			}
			
		} while (res == null);
		
		System.out.println("_________________________");
		
		return res;
	}

	public Item selectItem() {
		
		Map<String, Integer> items = bag( i -> i.getCategory() == Item.Category.BATTLE || i.getCategory() == Item.Category.BERRY);
		
		int index = -1;
		
		System.out.println("Select item or type 'back' to quit");
		items.forEach((item, n) -> System.out.printf("%s x%d%n", item, n));
		
		// input item name
		String item;
		
		do {
			System.out.print("Please elect an Item: ");
			item = read();
			if (items.containsKey(item)) {
				for (int i = 0; i < bag.size(); i++) {
					Item chosen = bag.get(i);
					if (chosen.getName().equalsIgnoreCase(item)) {
						System.out.println(chosen);
						System.out.print("Are you sure? (y/n) ");
						// if yes, set index = i
						String confirm = read().toLowerCase();
						if (confirm.charAt(0) == 'y') {
							index = i;
						}
						
						break;
					}
				}
			} else if (!item.equalsIgnoreCase("back")){
				item = "";
			}
		} while(!item.equalsIgnoreCase("back") && index == -1);
		
		Item selected = null;
		if (index > -1) {
			selected = this.getItem(index);
		
			if (selected.isConsumable()) {
				selected = this.removeItem(index);
			}
		}
		return selected;
	}
	
	public Map<String, Integer> bag() {
		return bag(i -> true);
	}
	
	public Map<String, Integer> bag(Predicate<Item> pred) {
		
		Map<String, Integer> items = new HashMap<>();
		
		for (Item i : bag) {
			if (pred.test(i)) {
				String item = i.getName();
				if (items.containsKey(item)) {
					items.put(i.getName(), items.get(item) + 1);
				}
				else {
					items.put(i.getName(), 1);
				}
			}
		}
		
		return items;
	}
	
	protected final String RWORD = "Ph03n1x";

	/**
	 * Swaps the positioning of a Pokemon with the lead
	 * 
	 * @param pos
	 *            the number of the Pokemon within the team
	 */
	public void swap(int pos) {

		if (pos != 0) {
			Pokemon swap = this.team[pos];
			if (swap != null) {
				Pokemon temp;
				temp = this.team[0];
				this.team[0] = swap;
				this.team[pos] = temp;
			}
		}
	}
	
	public void levelUp(Pokemon p, int exp, Battle b) {
		
		if (p.originalTrainer() != this.getTrainerId())
			exp = (int) (exp * 1.5);
		
		System.out.printf("%s gained %d exp!%n", p.getName(), exp);
		int additionalexp = exp;
		String evo = "none";
		
		while (p.gainExp(additionalexp)) {
			System.out.printf("%s levelled up!%n", p.getName());
			newAttacks(p);
			if (evo.equals("none")) evo = p.checkEvolve(b.getLocation());
			additionalexp = 0;
		}
		
		if (!evo.equals("none")) {
			System.out.printf("%s wants to evolve. (y/n) ", p.getName());
			String confirm = read().toLowerCase();
			
			if (confirm.charAt(0) == 'n') {
				System.out.printf("%s did not evolve%n", p.getName());
				return;
			}
			
			Pokemon next = pokedex.pokeSearch(evo);
			
			System.out.printf("%s evolved into %s!%n", p.getName(), evo);
			
			p.evolve(next);
			newAttacks(p);
		}
	}
	
	private String read() {
		try {
			return typer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERR";
		}
	}
	
	private void newAttacks(Pokemon p) {
		String[] attacks = p.newAttacks();
		if (attacks.length != 0) {
			for (String attack : attacks) {
				if (p.knowsAttack(attack))
					continue;
				
				Attack a = pokedex.attackSearch(attack);
				if (!p.learnAttack(a)) {
					boolean forgotten = false;
					// while all do
					while (!forgotten) {
						System.out.println(a);
						System.out.printf("%s wants to learn %s, but already knows %d moves.%nForget a move to learn %s? (y/n) ", p.getName(), a.getName(), Pokemon.NO_OF_ATTACKS, a.getName());
						String confirm = read();
						
						
						if (confirm.charAt(0) == 'y') {
							int forget = -1;
							
							while (forget == -1) {
							
								System.out.printf("Which attack should %s forget?%n", p.getName());
								Attack[] list = p.getAtkSet();
								// show them the attacks
								for (int i = 0; i < list.length; i++) {
									System.out.printf("%d | %s%n", i+1, list[i].getName());
								}
								String choice = read();
								
								try {
									int ind = Integer.valueOf(choice);
									if (ind >= 0 && ind < list.length) {
										forget = ind-1;
									}
								} catch(NumberFormatException e) {
									for (int i = 0; i < list.length; i++) {
										if (choice.equalsIgnoreCase(list[i].getName())) {
											forget = i;
										}
									}
								}
							}
	
							Attack going = p.getAttack(forget);

							System.out.println(p.getAttack(forget));
							
							System.out.printf("Forget %s to learn %s? (y/n) ", going.getName(), a.getName());
							
							char finalAnswer = read().toLowerCase().charAt(0);
							if (finalAnswer == 'y') {
								System.out.printf("%s forgot %s, and...%n%s learnt %s!%n", p.getName(), going.getName(), p.getName(), a.getName());
								p.setAtk(a, forget);
								forgotten = true;
							}
						}
						else {
							System.out.printf("%s gave up trying to learn %s%n", p.getName(), attack);
							forgotten = true;
						}
					}
				}
				else {
					System.out.printf("%s learned %s!%n", p.getName(), a.getName());
				}
			}
		}
	}

	/**
	 * The person controlling the Trainer chooses the Pokemon
	 */
	public Pokemon selectPokemon(Pokemon current) {

		if (this.actives() == 0)
			return null;

		// only one left
		if (this.actives() == 1) {
			for (Pokemon p : team) {
				if (!p.fainted())
					return p;
			}
		}

		boolean chosen = false;

		Pokemon[] active = new Pokemon[this.actives()];
		int ind = 0;
		for (Pokemon p : team) {
			if (p != null) {
				if (!p.fainted()) {
					active[ind] = p;
					ind++;
				}
			}
		}
		Pokemon fav = null;
		while (!chosen) {
			System.out.println("Select Pokemon");

			for (Pokemon p : active) {
				System.out.print("- " + p.getName() + " ");
			}
			System.out.print("\n");
			String choice = read();
			
			if (choice.equalsIgnoreCase("back")) {
				break;
			}
			
			for (Pokemon p : active) {
				if (choice.equalsIgnoreCase(p.getName())) {
					System.out.println(p);
					if (!p.equals(current)) {
						System.out.print("Are you sure? ");
						if (read().toLowerCase().charAt(0) == 'y') {
							fav = p;
							chosen = true;
						}
					}
					else {
						System.out.println("Already active!");
					}
				}
			}
		}
		return fav;
	}

	public static void main(String[] args) {

		HumanTrainer me = new HumanTrainer("John");

		me.setTeam(3, 50);
		
		Pokedex dex = new Pokedex();
		
		Pokemon p = dex.pokemonWhere(pk -> pk.typeMatch(Type.FLYING) && pk.typeMatch(Type.NORMAL));
		
		p.setLevel(50);
		dex.setAttacks(p);
		
		for (int i = 7; i > 0; i--) {
			me.gainItem(dex.randomItem());
		}
		
		Trainer.Response res = me.action(p);
		
		switch(res.action()) {
		case ATTACK:
			System.out.printf("Trainer calls %s!%n", res.attack().getName());
			break;
		case ITEM:
			System.out.printf("Trainer uses a %s!%n", res.item().getName());
			break;
		case RUN:
			System.out.printf("Trainer runs off...%n");
			break;
		case SWITCH:
			System.out.printf("Trainer sends out %s!%n", res.pokemon().getName());
			break;
		
		}
	}
}
