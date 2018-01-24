package joreli;

public enum Type {
	NONE, NORMAL, FIGHTING, GHOST, FIRE, WATER, GRASS, LIFE, FAIRY, STEEL, POISON, ROCK, GROUND, DRAGON, PSYCHIC, DARK, ICE, FLYING, ELECTRIC, BUG, TYPE_20;

	public static Type fromString(String t) {
		t = t.toUpperCase();
		Type type;
		try {
			type = valueOf(t);
		} catch (IllegalArgumentException e) {
			type = Type.NONE;
		}

		return type;
	}
}
