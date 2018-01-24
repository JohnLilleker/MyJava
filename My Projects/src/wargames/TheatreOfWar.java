package wargames;

import java.awt.Color;

import jb.*;
import joreli.*;

public class TheatreOfWar {
	/*
	 * private static final int size = 500; private static Graphic grass = new
	 * Graphic(size, size, "chase");
	 * 
	 * private static void drawArea(Unit u, int x, int y) {
	 * 
	 * grass.drawImage(u.getImage()); grass.setColour(0, 0, 0);
	 * grass.drawCentredEllipse(5, 5, x, y, true);
	 * 
	 * }
	 * 
	 * private static int random(int n) { return
	 * (int)Math.round(Math.random()*n); }
	 * 
	 * private static int[] chase(Unit u, int[] foe) {
	 * 
	 * // This works(ish)
	 * 
	 * int[] move = {0, 0};
	 * 
	 * int[] me = u.centre();
	 * 
	 * move[0] = foe[0] - me[0]; move[1] = foe[1] - me[1];
	 * 
	 * move[0] = move[0] / Unit.CHARGE; move[1] = move[1] / Unit.CHARGE; // this
	 * is roughly the number of moves needed
	 * 
	 * int mul = u.getRank(); // not forgetting the differing range of movement
	 * with rank
	 * 
	 * // clever stuff double work[] = {(double) move[0], (double) move[1]};
	 * 
	 * // make work[] positive, then convert the return statement
	 * 
	 * boolean w0n = false, w1n = false;
	 * 
	 * if (work[0] < 0) { work[0] = -work[0]; w0n = true; } if (work[1] < 0) {
	 * work[1] = -work[1]; w1n = true; }
	 * 
	 * double divi = 0; if (work[0] > work[1]) {
	 * 
	 * while((work[0] < mul) && (mul > 0)) mul--;
	 * 
	 * divi = work[0]/(double) mul; divi = work[1]/divi;
	 * 
	 * // this is the double matrix {mul : divi} move[0] = mul; move[1] = (int)
	 * divi; if (w0n) move[0] = -move[0]; if (w1n) move[1] = -move[1]; return
	 * move; } else {
	 * 
	 * while((work[1] < mul) && (mul > 0)) mul--; divi = work[1]/(double) mul;
	 * divi = work[0]/divi;
	 * 
	 * // this is the double matrix {mul : divi} move[1] = mul; move[0] = (int)
	 * divi; if (w0n) move[0] = -move[0]; if (w1n) move[1] = -move[1]; return
	 * move; } }
	 */
	public static void main(String[] args) throws InterruptedException {
		// test the classes

		/*
		 * Pokedex mace = new Pokedex(true);
		 * 
		 * 
		 * int[] home = {0,0,50,185}; int[] away = {150,0,185,185};
		 * 
		 * Army me = new Army("bbgt", 100, Color.blue, home, "Psychic", 1); Army
		 * you = new Army("lfod", 500, Color.red, away, "Dark", 0);
		 * 
		 * Army[] bo = {me, you}; BattleField b = new BattleField(200, 200, bo,
		 * "finv"); b.war(true);
		 * 
		 * int[] area = {0, 0, 100, 100}; Army test1 = new Army("Rouges", 50,
		 * new Color(100, 100, 200), area, 0);
		 * 
		 * System.out.println(test1.rollCall());
		 * System.out.println(test1.intel());
		 * 
		 * for (int d = 0; d < test1.garrison(); d+= 15) {
		 * test1.getUnit(d).die(); }
		 * 
		 * System.out.println(test1.rollCall()); test1.revive(3); test1.bury();
		 * System.out.println(test1.rollCall());
		 * System.out.println(test1.intel());
		 */
		SimpleImage irn = new SimpleImage(5, 5, 3);
		irn.fill(Color.gray);
		irn.colourIn(2, 2, Color.white);

		Pokedex grey = new Pokedex(true);
		Pokemon base = grey.pokeSearch("Aron");
		base.setLevel(25);
		grey.setAttacks(base);

		int[] cave = { 0, 0 };
		Unit irnman = new Unit(irn, base, cave, "The Mountains");

		Unit[] army = new Unit[70];

		army[0] = irnman;

		for (int i = 1; i < army.length; i++) {
			int[] dire = { 1, 0 };
			army[i] = army[i - 1].spawn(dire);
		}
		Army fe = new Army("Greyjoys", army);

		int[] a13 = { 0, 200, 100, 300 };
		Army a1 = new Army("Elves", 50, Type.DRAGON, Color.yellow, a13, 1);

		int[] area3 = { 500, 0, 785, 50 };
		Army help = new Army("Dwarfs", 100, new Color(50, 50, 250), area3, "Johto", 0.25);

		Army other = new Army("Dwarves", help.detach(2 * help.garrison() / 3));
		int[] area4 = { 500, 450, 785, 485 };
		other.redeploy(area4);

		Army rear = new Army("Knurla", other.detach(other.garrison() / 2));
		int[] reaguard = { 700, 230, 785, 380 };
		rear.redeploy(reaguard);

		int[] camp = { 400, 200, 585, 400 };
		Army a2 = new Army("Orcs", 150, Type.FIGHTING, Color.green, camp, 0.1);

		a1.merge(help);

		int[] wargs = { 10, 450, 90, 485 };
		fe.redeploy(wargs);
		Army lowWargs = new Army("IronMen", fe.detach(fe.garrison() / 2));
		int[] pounce = { 10, 15, 90, 50 };
		lowWargs.redeploy(pounce);

		a2.merge(fe);

		Army[] lOfRings = { a1, a2 };

		BattleField seige = new BattleField(800, 500, lOfRings, "Elves, Dwarfs and Orcs", Weather.RAIN);
		seige.war();
		/*
		 * System.out.println(seige.conquerors().rollCall());
		 * 
		 * SimpleImage st = new SimpleImage(5, 5, 3); st.fill(100, 100, 50);
		 * st.colourIn(1, 1, 0, 0, 0); st.colourIn(1, 3, 0, 0, 0);
		 * st.colourIn(2, 2, 0, 0, 0); st.colourIn(3, 1, 0, 0, 0);
		 * st.colourIn(3, 3, 0, 0, 0); Pokemon sell =
		 * mace.pokeSearch("Xerneas"); mace.setAttacks(sell, "Fairy", "Life");
		 * int[] comeOn = {0, 0}; Unit merc = new Unit(st, sell, 7, comeOn,
		 * "Mercenary", true);
		 * 
		 * SimpleImage fl = new SimpleImage(5, 5, 3); fl.fill(Color.YELLOW);
		 * fl.colourIn(1, 1, Color.BLACK); fl.colourIn(3, 1, Color.BLACK);
		 * fl.colourIn(2, 2, Color.BLACK); fl.colourIn(3, 3, Color.BLACK);
		 * fl.colourIn(1, 3, Color.BLACK); fl.colourIn(2, 3, Color.BLACK);
		 * fl.colourIn(1, 4, Color.BLACK); fl.colourIn(3, 4, Color.BLACK);
		 * Pokemon sword = mace.pokeSearch("Yveltal"); mace.setAttacks(sword,
		 * "Dark", "Flying"); int[] hey = {(250),(250)}; Unit help1 = new
		 * Unit(fl, sword, 3, hey, "Mercenary", true);
		 * 
		 * int[] start = {0, 450, 490, 490}; Army o = new Army("Space", 75,
		 * "Flying", new Color(200, 0, 0), start, 0); for (int i = 0; i <
		 * o.garrison()-1; i++) { int[] spread = {random(485), random(485)};
		 * o.getUnit(i).drop(spread); }
		 * 
		 * int[] start1 = {0, 0, 485, 35}; Army r = new Army("Raiders", 75, new
		 * Color(0, 200, 0), start1, "Kalos", 0);
		 * 
		 * int[] start2 = {200, 200, 300, 300}; Army mid = new Army("Bandits",
		 * 100, new Color(100, 100, 100), start2, 1);
		 * 
		 * //r.merge(mid);
		 * 
		 * int[] s3 = {0, 200, 100, 300}; Army edge = new Army("Average", 80,
		 * "Normal", new Color(0, 0, 240), s3, 0); edge.recruit(help1);
		 * 
		 * int[] s4 = {400, 200, 485, 300}; Army flank = new Army("Killers",
		 * 100, new Color(215, 155, 50), s4, "Hoenn", 0); flank.recruit(merc);
		 * 
		 * 
		 * Army[] attack = {o, r, edge, flank, mid};
		 * 
		 * BattleField raid = new BattleField(500, 500, attack,
		 * "The Battle of 5 Armies"); raid.war(true);
		 * 
		 * for (int i = 0; i < attack.length; i++) {
		 * System.out.println(attack[i].rollCall());
		 * System.out.println(attack[i].intel()); System.out.println(); }
		 * 
		 * Unit conqueror = raid.warHero(); System.out.println("\n" +
		 * conqueror);
		 * 
		 * 
		 * int[] point = {410, 300};
		 * 
		 * int chase = 5; while (chase > 0) {
		 * 
		 * merc.orders(chase(merc, point)); merc.move(); Thread.sleep(500);
		 * grass.clear(); drawArea(merc, point[0], point[1]);
		 * 
		 * if (merc.getImage().covers(point[0], point[1])) { chase--; point[0] =
		 * random(500); point[1] = random(500); } }
		 * 
		 * 
		 * 
		 * SimpleImage fl = new SimpleImage(5, 5, 15); fl.fill(Color.cyan);
		 * fl.colourIn(1, 1, Color.BLACK); fl.colourIn(3, 1, Color.BLACK);
		 * fl.colourIn(2, 2, Color.BLACK); fl.colourIn(3, 3, Color.BLACK);
		 * fl.colourIn(1, 3, Color.BLACK); fl.colourIn(2, 3, Color.BLACK);
		 * fl.colourIn(1, 4, Color.BLACK); fl.colourIn(3, 4, Color.BLACK);
		 * Pokemon arrow = mace.randomPokemon(); int[] ggtt =
		 * {random(size),random(size)}; Unit ggttf = new Unit(fl, arrow, 3,
		 * ggtt, "Mercenary", true);
		 * 
		 * int[] poit223 = {random(size), random(size)}; drawArea(ggttf,
		 * poit223[0], poit223[1]);
		 * 
		 * Integer c244 = 20; while (c244 > 0) {
		 * 
		 * ggttf.orders(chase(ggttf, poit223)); ggttf.move(); Thread.sleep(500);
		 * grass.clear(); grass.setColour(Color.red);
		 * grass.drawString(c244.toString(), 10, 10, 20); drawArea(ggttf,
		 * poit223[0], poit223[1]);
		 * 
		 * if (ggttf.getImage().covers(poit223[0], poit223[1])) { c244--;
		 * poit223[0] = random(size); poit223[1] = random(size); } }
		 */
	}
}
