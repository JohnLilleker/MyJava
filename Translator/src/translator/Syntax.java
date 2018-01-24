package translator;

public abstract class Syntax {
	
	protected String file;
	protected int line;
	
	public Syntax(String file, int line) {
		this.file = file;
		this.line = line;
	}
	
	public String getFile() {return file; }
	public int getLine() { return line; }
	
	public abstract boolean isRule();
	public abstract boolean isCode();
	
	public String toString() {
		return "Origin: " + file + " Line " + line;
	}
}
