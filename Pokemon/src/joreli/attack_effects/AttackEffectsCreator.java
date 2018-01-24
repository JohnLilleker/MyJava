package joreli.attack_effects;

import java.util.ArrayList;

import joreli.Attack;
import joreli.Battle;
import joreli.Condition;
import joreli.Pokemon;
import joreli.Type;
import joreli.Weather;
import joreli.attack_effects.damage.*;
import joreli.attack_effects.effect.*;
import joreli.attack_effects.timed.*;
import joreli.attack_effects.accuracy.*;
import joreli.items.TypeChange;

public class AttackEffectsCreator {
	private Timed t;
	private Damage d;
	private Effect[] es;
	private Accuracy a;

	public Timed getT() {
		return t;
	}

	public Damage getD() {
		return d;
	}

	public Effect[] getEs() {
		return es;
	}

	public Accuracy getA() {
		return a;
	}

	/**
	 * Given data, it converts them into usable class for attacks
	 * 
	 * @param nums
	 *            the data
	 * @param line
	 *            line number in the file, used in error reporting
	 */
	public void decode(String[] nums, int line) {
		t = null;
		d = null;
		a = null;
		ArrayList<Effect> temp_es = new ArrayList<>();

		// catch all for any type of exception, thrown manually or otherwise
		try {
			for (int i = 0; i < nums.length; i++) {
				switch (nums[i]) {
				case "":
					continue;

				/*
				 * Timed 'multihit MIN_HIT MAX_HIT' -> MultiHit(...) 'charged' -> Charged()
				 * 'hide WHERE' -> HideCharged(...) 'gender WHO{SAME,DIFFERENT}' ->
				 * GenderCanHit(...) 'hasCond CONDS' -> HasCondition(...) 'future TURNS' ->
				 * FutureMove(...)
				 */
				case "charged":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new Charged();
					break;

				case "hide":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new HideCharged(Battle.Place.valueOf(nums[++i]));
					break;

				case "multihit":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					int min = Integer.valueOf(nums[++i]);
					int max = Integer.valueOf(nums[++i]);
					t = new MultiHit(min, max);
					break;

				case "gender":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new GenderCanHit(GenderCanHit.Who.valueOf(nums[++i]));
					break;

				case "hasCond":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new HasCondition(nums[++i]);
					break;

				case "future":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					int turns = Integer.valueOf(nums[++i]);
					t = new FutureMove(turns);
					break;

				case "repeat":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new LockIn(Integer.valueOf(nums[++i]));
					break;

				case "useWith":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new UseWithCondition(Condition.valueOf(nums[++i]));
					break;

				case "weatherOneTurn":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new WeatherOneTurn(Weather.valueOf(nums[++i]));
					break;

				case "weatherNoMiss":
					if (t != null) {
						throw new IllegalArgumentException("Multiple timer effects!");
					}
					t = new WeatherNoMiss(Weather.valueOf(nums[++i]));
					break;

				/*
				 * Damage 'OHKO' -> OHKODamage() 'hit_protect' -> HitProtectedDamage() 'fix
				 * DAMAGE' -> FixDamage(...) 'no_hide WHERE MULT' -> HideDamage(...) 'critical'
				 * -> CriticalDamage() 'counter' -> CounterDamage() 'condDamage CONDS MULT' ->
				 * ConditionDamage(...) 'typeless' -> PlainDamage() 'healthReduce %DAMAGE' ->
				 * PartDamage(...) 'nokill' -> NoKillDamage()
				 */
				case "OHKO":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new OHKODamage();
					a = new LevelDiffAccuracy();
					break;

				case "hit_protect":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new HitProtectedDamage();
					break;

				case "fix":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new FixDamage(Integer.valueOf(nums[++i]));
					break;

				case "no_hide":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					Battle.Place wh = Battle.Place.valueOf(nums[++i]);
					float mul = Float.valueOf(nums[++i]);
					d = new HideDamage(wh, mul);
					a = new NoHideAccuracy(wh);
					break;

				case "critical":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new CriticalDamage();
					break;

				case "counter":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new CounterDamage();
					break;

				case "condDamage":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					String conditions1 = nums[++i];
					float m = Float.valueOf(nums[++i]);
					d = new ConditionDamage(conditions1, m);
					break;

				case "typeless":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new PlainDamage();
					break;

				case "healthReduce":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new PartDamage(Float.valueOf(nums[++i]));
					break;

				case "level":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new LevelDamage();
					break;

				case "nokill":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new NoKillDamage();
					break;

				case "ivs":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new IVDamage();
					break;

				case "statDamage":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					Pokemon.Stats atk = Pokemon.Stats.valueOf(nums[++i]);
					Pokemon.Stats def = Pokemon.Stats.valueOf(nums[++i]);
					d = new StatDamage(atk, def);
					break;

				case "revenge":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new RevengeDamage();
					break;

				case "noItem":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new NoItemDamage();
					break;

				case "build":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new BuildingDamage();
					break;

				case "delay":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new DelayedEffectDamage(Integer.valueOf(nums[++i]));
					break;

				case "weatherDamage":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new WeatherDamage(nums[++i], Float.valueOf(nums[++i]));
					break;

				case "ignoreMod":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new IgnoreStatModDamage();
					a = new IgnoreEvasionAccuracy();
					break;

				case "fullHP":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new HighHPDamage();
					break;

				case "itemType":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new ItemTypeDamage(TypeChange.Device.valueOf(nums[++i]));
					break;

				case "weatherType":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new WeatherTypeDamage();
					break;

				case "lowHP":
					if (d != null) {
						throw new IllegalArgumentException("Multiple damage effects!");
					}
					d = new LowHPDamage();
					break;

				/*
				 * Effect 'stat STAT INC TARGET %happen' -> StatEffect(...) 'condition CONDITION
				 * TARGET %HAPPEN' -> ConditionEffect(...) 'destruct' -> DestructEffect()
				 * 'recoil %DAMAGE' -> RecoilEffect(...) 'absorb %HEAL' -> AbsorbEffect(...)
				 * 'protect' -> ProtectEffect() 'rest' -> RestEffect() 'weather WEATHER' ->
				 * WeatherEffect(...) 'aim' -> LockEffect() 'heal %DAMAGE FIX' ->
				 * HealEffect(...) 'steal' -> ThiefEffect()
				 */
				case "stat":
					String st = nums[++i];
					int ch = Integer.valueOf(nums[++i]);
					Effect.Target tar = Effect.Target.valueOf(nums[++i]);
					int l = Integer.valueOf(nums[++i]);
					temp_es.add(new StatEffect(st, ch, tar, l));
					break;

				case "condition":
					String cs = nums[++i];
					Effect.Target tar1 = Effect.Target.valueOf(nums[++i]);
					int when = Integer.valueOf(nums[++i]);
					temp_es.add(new ConditionEffect(cs, when, tar1));
					break;

				case "destruct":
					temp_es.add(new DestructEffect());
					break;

				case "recoil":
					temp_es.add(new RecoilEffect(Float.valueOf(nums[++i])));
					break;

				case "absorb":
					temp_es.add(new AbsorbEffect(Float.valueOf(nums[++i])));
					break;

				case "protect":
					temp_es.add(new ProtectEffect());
					break;

				case "endure":
					temp_es.add(new EndureEffect());
					break;

				case "rest":
					temp_es.add(new RestEffect());
					break;

				case "aim":
					temp_es.add(new LockEffect());
					break;

				case "heal":
					float health = Float.valueOf(nums[++i]);
					Condition.Fix fix = Condition.Fix.valueOf(nums[++i]);
					Effect.Target pat = Effect.Target.valueOf(nums[++i]);
					temp_es.add(new HealEffect(health, fix, pat));
					break;

				case "weather":
					temp_es.add(new WeatherEffect(Weather.valueOf(nums[++i])));
					break;

				case "steal":
					temp_es.add(new ThiefEffect());
					break;

				case "trap":
					temp_es.add(new TrapEffect(Battle.Trap.valueOf(nums[++i])));
					break;

				case "removeTrap":
					temp_es.add(new RemoveTrapEffect(Effect.Target.valueOf(nums[++i])));
					break;

				case "switch":
					temp_es.add(new SwitchEffect(Effect.Target.valueOf(nums[++i])));
					break;

				case "defence":
					temp_es.add(new TeamDefenceEffect(Battle.Defence.valueOf(nums[++i])));
					break;

				case "remDef":
					temp_es.add(new RemoveDefenceEffect(nums[++i]));
					break;

				/*
				 * Accuracy
				 */
				case "nvmissWeather":
					a = new WeatherMaxAccuracy(Weather.valueOf(nums[++i]));
					break;

				default:
					throw new Exception("Unkown effect type " + nums[i] + "!");
				}
			}
		} catch (Exception e) {
			System.err.println("ERROR IN ATTACK PARSER!!!");
			System.err.print("Options: ");
			for (String s : nums) {
				System.err.print(s + " ");
			}
			System.err.println("\nLine: " + line);
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}

		if (t == null)
			t = new OneTurn();

		if (d == null)
			d = new RegularDamage();

		es = new Effect[temp_es.size()];
		for (int i = 0; i < es.length; i++) {
			es[i] = temp_es.get(i);
		}

		if (a == null)
			a = new StandardAccuracy();
	}

	public static void main(String args[]) {
		AttackEffectsCreator aec = new AttackEffectsCreator();

		String practice = "charged critical condition BURN ENEMY 10 heal 0 THAW ENEMY";
		String[] ready = practice.split("\\s");

		aec.decode(ready, 0);

		Attack a = new Attack("Attack", 0, 100, 0, Type.NORMAL, Attack.Category.STATUS, Attack.Target.ENEMY, 0,
				aec.getT(), aec.getD(), aec.getEs(), aec.getA());

		System.out.println(a);
	}
}
