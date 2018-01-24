package joreli;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Creates a file of Pokemon data, useful for Python experiments
 * 
 * @author JB227
 */
public class DataMaker {

	private PrintWriter maker;
	private Pokemon[] data;

	/**
	 * 
	 * @param f
	 *            the name of the file you wish to create
	 * @param n
	 *            the number of subjects
	 * @param t
	 *            a type preference
	 */
	public DataMaker(String f, int n, Type t) {

		try {
			maker = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		Pokedex lis = new Pokedex(false);
		lis.readPok();
		data = lis.getList(n, 50, p -> {return p.typeMatch(t);} );
	}

	/**
	 * Creates the file, format (health,attack stat,sp.attack stat,defence
	 * stat,sp.defence stat,speed)
	 * 
	 */
	public void create() {
		for (Pokemon p : data) {

			String d = "";

			d += p.getBaseHealth() + ",";
			d += p.getBaseAtk() + ",";
			d += p.getBaseDef() + ",";
			d += p.getBaseSpAtk() + ",";
			d += p.getBaseSpDef() + ",";
			d += p.getBaseSpd() + ",";
			maker.println(d);
		}
		maker.flush();
		maker.close();
	}

	public Pokemon[] getData() {
		return data;
	}

	public static void main(String[] args) {
	
		/*
		DataMaker py = new DataMaker("Poke.txt", 200, null);
		py.create();

		String[] ty = { "Grass", "Fire", "Water" };
		for (String t : ty) {
			DataMaker st = new DataMaker(t + ".txt", 20, Type.fromString(t));
			st.create();
		}
		*/
		
		Pokedex dex = new Pokedex();
		
		Pokemon litwick = dex.pokeSearch("Litwick");
		Pokemon cobalion = dex.pokeSearch("Cobalion");
		
		Attack irn_hd = dex.attackSearch("Iron Head");
		Attack hex = dex.attackSearch("Hex");
		
		//Attack inf = new Attack("Inferno", 100, 100, 5, Type.FIRE, Attack.Category.SPECIAL, 1);
		
		cobalion.setLevel(42);
		litwick.setNature("Quirky");
		litwick.setLevel(38);
		
		Battle b = new Battle();
		
		cobalion.giveCondition(Condition.BURN, "", b);
		cobalion.attack(irn_hd, litwick, b);
		litwick.printHealth(true);
		
		litwick.attack(hex, cobalion, b);
		cobalion.printHealth(true);
		
	}
}
