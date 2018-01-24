package translator;

public class Code extends Syntax {

	private String code;
	
	/*
	 * Have if, else, elif, while, endif, endwhile
	 * continue and break?
	 */
	
	public Code(String c, String file, int line) {
		super(file, line);
		code = c;
	}

	public String getCode() { return code; }
	
	@Override
	public boolean isRule() {
		return false;
	}

	@Override
	public boolean isCode() {
		return true;
	}

	public String toString() { return "[" + code + "] " + super.toString(); }
	
}
