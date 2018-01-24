package translator;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CodeInterpreter {
	
	private ArrayList<String> prints;
	private TreeMap<String, String> store;
	
	public CodeInterpreter() {
		prints = new ArrayList<>();
		store = new TreeMap<>();
	}
	
	/**
	 * Checks code syntax and creates the object
	 * @param code the String line
	 * @param file the file it came from 
	 * @param line the line
	 * @return the Code object
	 * @throws CodeException
	 */
	public Code checkCode(String code, String file, int line) throws CodeException {
		
		code = code.trim();
		
		/*
		 * TODO
		 * Check syntax, set up any looping / if branching logic
		 * if syntax is dodgey, throw exception
		 */
		
		/*
		 * Syntax
		 * 
		 * @variable = @variable [+ addition][* multiplication][- subtraction][/ division][% modulus][++ append] @variable
		 * @variables on rhs may be literals, \d+ or ".*"
		 * also may have methods find(...) etc
		 * 
		 * print(...)
		 * die()
		 * 
		 * if (statement)
		 * elif (statement)
		 * else
		 * endif
		 * 
		 * while (statement)
		 * endwhile
		 * 
		 * statements
		 *   Method  i.e. isset()
		 *   @variable [<][>][<=][>=][==][!=] @variable 
		 *   statement & statement
		 *   statement | statement
		 *   !statement
		 */
		
		return new Code(code, file, line);
	}

	/**
	 * Queries the number of prints
	 * @return how many prints have been stored
	 */
	public int getPrintNumber() {
		return prints.size();
	}
	
	/**
	 * Gets a print at the parameter index
	 * @param index the index of the print you want
	 * @return the string print
	 */
	public String getPrint(int index) {
		return prints.get(index);
	}
	
	/**
	 * Clears the print list and store
	 */
	public void clear() {
		prints.clear();
		store.clear();
	}

	public void clearVars() {
		store.clear();
	}
	
	/**
	 * Replaces each occurrence of the variable with the associated value
	 * @param str the input string thing
	 * @return
	 */
	public String replaceVars(String str) {
		
		// pre-defined constants
		str = str.replaceAll("@EMPTY", "");
		str = str.replaceAll("@SPACE", " ");
		str = str.replaceAll("@NULL", null);
		
		for (Entry<String, String> var : store.entrySet()) {
			str = str.replaceAll(var.getKey(), var.getValue());
		}
		
		return str;
	}
	
	/**
	 * use the given code to update the state of the store, prints and control flow
	 * @param code the Code object
	 * @param line the current line being acted upon
	 * @return the next line
	 */
	public int run(Code code, String input, int line) throws CodeException {
		
		//die() returns -1, so stop
		if (code.getCode().equalsIgnoreCase("die()"))
			return -1;
		/*
		 * TODO
		 * do it
		 * check if / else
		 * store/update @variables 
		 * check @variables exist, if in stements or on rhs
		 */
		return line+1;
	}
}
