package joreli;

/**
 * A class representing a particular area in Pokemon <br>
 * It has many features, such as terrain and weather.<br>
 * future updates would include pokemon that live here and connected routes, then this API becomes a Game!
 * @author JB227
 *
 */
public class Location {

	public enum Terrain {GRASS, CAVE, MAGNETIC, FOREST, ICY, UNDERWATER, WATER_SURFACE, URBAN, BUILDING}
	
	private String name;
	private Time time;
	private Terrain terrain;
	private Weather weather = Weather.CLEAR;
	
	public Location(String n) {
		this(n, Terrain.GRASS);
	}
	public Location(String n, Terrain t) {
		this.name = n;
		this.terrain = t;
		this.time = Time.DAY;
	}
	
	public String getName() { return name; }
	public Terrain getTerrain() { return terrain; }
	public Time getTime() { return time; }
	public Weather getWeather() { return weather; }
	
	public void setTime(Time t) { this.time = t; }
	public void setWeather(Weather w) { weather = w; }
}
