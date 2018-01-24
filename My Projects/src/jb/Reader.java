package jb;

import java.io.*;

/*
 * Created: 2/5/2014
 * Last Modified: 18/9/2014 (line number in error)
 */
/**
 * My own reader, screw you EasyReader... Can read from both files and keyboard,
 * with format options for "proper nouns". For use with other projects, check
 * toInt().
 * 
 * @author JB227
 */
public class Reader {

	private BufferedReader typer;
	private boolean fileEnd = false;

	private int line = 0;

	private void error(String w) {
		System.err.println("Reader says- " + w);
	}

	/**
	 * Reads from a file, quits if file not found
	 * 
	 * @param f
	 *            the file
	 */
	public Reader(String f) {
		try {
			typer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			error("Cannot find " + f);
			System.exit(0);
		}
	}

	/**
	 * Simple keystroke input constructor
	 */
	public Reader() {
		typer = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Reads a String
	 * 
	 * @return the String
	 */
	public String read() {
		String input = "";
		if (!endOfFile()) {
			try {
				line++;
				input = typer.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (input == null) {
				fileEnd = true;
			}
		}
		return input;
	}

	/**
	 * Allows a prompt, better than EasyReader as the prompt actually works in
	 * eclipse
	 * 
	 * @param p
	 *            the prompt
	 * @return the String
	 */
	public String read(String p) {
		System.out.print(p);
		return read();
	}

	/**
	 * Reads a number
	 * 
	 * @return the Integer
	 */
	public int readInt() {
		return toInt(read());
	}

	public int readInt(String s) {
		return toInt(read(s));
	}

	/**
	 * Reads a String, then takes the first letter
	 * 
	 * @return the char
	 */
	public char readChar() {
		char c = '0';
		try {
			c = read().charAt(0);
		} catch (StringIndexOutOfBoundsException e) {
			error("Invalid input, no char detected");
		}
		return c;
	}

	public char readChar(String p) {
		char c = '0';
		try {
			c = read(p).charAt(0);
		} catch (StringIndexOutOfBoundsException e) {
			error("Invalid input, no char detected");
		}
		return c;
	}

	/**
	 * Reads a string, then formats it. Useful with names and other proper
	 * nouns.
	 * 
	 * @return the properly set out String
	 */
	public String readName() {
		String name = read();
		String format = name(name);
		return format;
	}

	public String readName(String p) {
		System.out.print(p);
		return readName();
	}

	/**
	 * End of file? Find out here
	 * 
	 * @return true if end of file
	 */
	public boolean endOfFile() {
		if (fileEnd)
			System.out.println("End of File!!!!!");
		return fileEnd;
	}

	/**
	 * Converts a string to an integer, error message if incorrect then exit,
	 * bit harsh I know but still...
	 * 
	 * @param s
	 *            the String
	 * @return the Integer, if no exit 0
	 */
	public int toInt(String s) {
		int number = 0;
		try {
			number = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			error("Invalid Integer- " + s + " at line " + line);
			System.exit(0);
		}
		return number;
	}

	/**
	 * Formats a String properly to a name, even with multiple words.
	 * 
	 * @param s
	 *            the String
	 * @return formatted name
	 */
	public String name(String s) {
		String[] words = s.trim().split("\\s+");
		String format = "";

		try {
			for (int i = 0; i < words.length; i++) {

				String first = words[i].substring(0, 1);
				first = first.toUpperCase();

				first = first + words[i].substring(1, words[i].length()).toLowerCase();
				if ((words.length > 1) && (i < words.length - 1)) {
					format += first + " ";
				} else
					format += first;
			}
		} catch (StringIndexOutOfBoundsException e) {
			error("No input detected");
		}
		return format;
	}

}
