package translator;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A tool that performs mass find and replace akin to translation. <br>
 * It uses a file defining the rules with a number of configuration options
 * @author JB227
 * @version 1.0
 */
public class Translator {
	
	private String prePrefix = "";
	private String preSuffix = "";
	private String postPrefix = "";
	private String postSuffix = "";
	
	// used if the prefixes or suffixes are regular expressions that change the others groups 
	// no protection for escaped $\d though... But why would you have it?
	private static final Pattern group = Pattern.compile("\\$(\\d+)");
	
	private List<Syntax> lines;
	private String file;
	private String originFile;
	private String fullFile;

	private String comment = null;
	
	private Deque<String> files = new ArrayDeque<String>();
	
	CodeInterpreter interpreter;
	
	/**
	 * Creates a translator object with a given rule file, parsing it to get the new 'language'
	 * @param ruleFile the file path to the rule file
	 * @throws TranslatorException if rule file not found or rule file in wrong format
	 * @throws CodeException 
	 */
	public Translator(String ruleFile) throws TranslatorException, CodeException {
		
		if (ruleFile == null) {
			throw new NullPointerException("No rule file passed");
		}
		interpreter = new CodeInterpreter();
		lines = new ArrayList<>();
		originFile = ruleFile;
		files.push(originFile);
		readFile(originFile);
	}
	
	/**
	 * Reads the rule file, storing the rules internally
	 * @param ruleFile the file to read
	 * @throws TranslatorException on rule syntax errors
	 * @throws CodeException on code syntax errors
	 */
	private void readFile(String ruleFile) throws TranslatorException, CodeException {
		file = ruleFile;
		List<String> list = new ArrayList<>();
		File f;
		try (Scanner s = new Scanner((f = new File(ruleFile)))) {
			if (ruleFile.equalsIgnoreCase(originFile))
				fullFile = f.getAbsolutePath();
			while (s.hasNextLine()){
			    list.add(s.nextLine());
			}
			s.close();
		} catch (FileNotFoundException e) {
			throw new TranslatorException("IOException: Rule file " + ruleFile + " not found");
		}
		int line = 0;
		for (String rule : list) {
			line++;
			
			rule = rule.trim();
			
			if (rule.equals(""))
				continue;
			
			// comments enabled and used
			if (comment != null && rule.startsWith(comment))
				continue;
			
			if (rule.startsWith("%")) {
				config(rule, line);
				continue;
			}
			
			if (rule.startsWith("$")) {
				lines.add(interpreter.checkCode(rule.substring(1), file, line));
				continue;
			}
			
			lines.add(parse(rule, line));
		}
	}
	
	/**
	 * Gets the full path to the file used to define the rules
	 * @return the string full path to the files defining the rules
	 */
	public String getRuleFilePath() {
		return fullFile;
	}
	
	/**
	 * Gets the file used to define the rules
	 * @return the File object representing the file defining the rules
	 */
	public File getRuleFile() {
		return new File(fullFile);
	}
	
	/**
	 * Re-reads the file, removing all rules and redefining them.<br>helpful if the file has been changed
	 * @throws TranslatorException Any syntax errors raise the exception
	 * @throws CodeException 
	 */
	public void reload() throws TranslatorException, CodeException{
		lines.clear();
		prePrefix = "";
		preSuffix = "";
		postPrefix = "";
		postSuffix = "";
		comment = null;
		files.clear();
		files.push(originFile);
		readFile(originFile);
	}

	/**
	 * The actual translation, also runs any codee found
	 * @param input A string input
	 * @return the input with the rules applied from the rule file
	 * @throws TranslatorException thrown on any "runtime" errors encountered, for example using groups that don't match, (\\w+) = $1 $2 is an example [expected 2 groups got one]
	 * @throws CodeException 
	 */
	public String translate(String input) throws TranslatorException, CodeException {
		interpreter.clear();
		String output = input;
		for (int i = 0; i < lines.size() && i != -1;) {
			Syntax line = lines.get(i);
			if (line.isRule()) {
				output = applyRule((Rule) line, output);
				i++;
			}
			else if (line.isCode()) { // 'else if' allows new Syntax to be created
				i = interpreter.run((Code) line, output, i);
			}
			
		}
		interpreter.clearVars();
		return output;
	}
	
	/**
	 * Applies a rule to input to get the translated output
	 * @param rule the rule object
	 * @param input the String input
	 * @return the translated output
	 * @throws TranslatorException On discrepancies, such as out of range groups
	 * @throws  
	 */
	private String applyRule(Rule rule, String input) throws TranslatorException {
		String ruleErr = "Error in translating rule " + rule.toString() + ".\n";
		
		String count = rule.getSwapCount();
		boolean all = count.equals("*");
		String startInd = rule.getStartIndex();
		String ruleStep = rule.getStep();
		int swaps = 0, index = 0, step = 1;
		try {
			if (!all) {
				swaps = Integer.valueOf(count);
			}
			index = Integer.valueOf(startInd);
			step = Integer.valueOf(ruleStep);
		} catch(NumberFormatException e) {
			throw new TranslatorException(ruleErr + "Change params should be Integers");
		}
		
		if (!all && swaps < 0) {
			throw new TranslatorException(ruleErr + "Number of swaps (change arg 1) should be more than 0");
		}
		if (index < 0) {
			throw new TranslatorException(ruleErr + "Index of changing (change arg 2) should be 0 or more");
		}
		if (step < 1) {
			throw new TranslatorException(ruleErr + "Step change (change arg 3) should be more than 0");
		}
		
		String pre = rule.getPre();
		String post = rule.getPost();
		
		// done in the code Interpreter
		pre = interpreter.replaceVars(pre);
		post = interpreter.replaceVars(post);
		
		int findIndex = 0;
		int changes = 0;
		int last = 0;
		StringBuilder builder = new StringBuilder();
		Matcher m = Pattern.compile(pre).matcher(input);
		
		while (m.find()) {
			if (findIndex >= index) {
				if ((changes * step) + index == findIndex) {
					if (all || changes < swaps) {
						
						int pos = m.start();
						String postRes = post;
												
						if (m.groupCount() > 0) { // manually decode each group
							StringBuilder postMatchString = new StringBuilder();
							
							Matcher groupMatcher = group.matcher(post);
							int lastGroup = 0;
							
							while (groupMatcher.find()) {
								int number = Integer.valueOf(groupMatcher.group(1));
								if (number > m.groupCount()) {
									throw new TranslatorException(ruleErr + "Group " + number + " is out of range on right hand side");
								}
								int loc = groupMatcher.start();
								
								postMatchString.append(post.substring(lastGroup, loc) + m.group(number));
								lastGroup = groupMatcher.end();
							}
							postRes = postMatchString.append(post.substring(lastGroup)).toString();
						}
						
						// append result string to output so far
						builder.append(input.substring(last, pos) + postRes);
						
						last = m.end();
						changes++;
					}
					else {
						break; // all swaps completed
					}
				}
			}
			findIndex++;
		}
		// finish off output string
		return builder.append(input.substring(last)).toString();
	}
	
	/**
	 * Parse a rule in the form of pre = post
	 * @param rule a string describing a rule
	 * @return the rule object
	 */
	private Rule parse(String rule, int line) throws TranslatorException {
		StringBuilder pre = new StringBuilder();
		StringBuilder post = new StringBuilder();
		StringBuilder params = new StringBuilder();
		
		String tranErr = "Invalid translation rule in file " + file + " line " + line + ".\n";
		if (!rule.contains("=")) {
			throw new TranslatorException(tranErr + "Missing assignment symbol ( = )");
		}
		// format taken as (pre) = (post)
		int i = 0;
		boolean chngparams = false;
		int paramNo = 0;
		boolean left = true;
		boolean escape = false;
		boolean quote = false;
		StringBuilder literal = new StringBuilder();
		while (i < rule.length()) {
			
			char c = rule.charAt(i);
			String next = Character.toString(c);
			i++;
			
			if (chngparams) {
				if (params.length() == 0) {
					if (c == '(') {
						params.append(" ");
						continue;
					}
					else {
						chngparams = false;
					}
				}
				
				if (chngparams) {
					if (c == ')') {
						chngparams = false;
						continue;
					}
					
					if (c == ',') {
						paramNo++;
						if (paramNo > 2) {
							throw new TranslatorException(tranErr + "Illegal number of change params, should be 3 or less");
						}
					}
					params.append(c);
					continue;
				}
			}
			
			// escaped characters
			if (escape) {
				escape = false;
				if (c != ' ' && c != '=' && c != '\\' && c != '\"')
					next = '\\' + next;
				
				if (quote) {
					literal.append(next);
					continue;
				}
				if (left)
					pre.append(next);
				else
					post.append(next);
				continue;
			}
			
			// start escape
			if (c == '\\') {
				escape = true;
				continue;
			}
			
			// " char
			if (c == '\"') {
				if (!quote) {
					quote = true;
					continue;
				}
				quote = false;
				if (left)
					pre.append(Pattern.quote(literal.toString()));
				else
					post.append(Matcher.quoteReplacement(literal.toString()));
				literal = new StringBuilder();
				continue;
			}
			
			// in quotes
			if (quote) {
				literal.append(next);
				continue;
			}
			
			// ignore space
			if (c == ' ')
				continue;
			
			// switch sides
			if (c == '=') {
				if (!left) 
					throw new TranslatorException(tranErr + "Multiple unescaped assignment symbols ( = )");
				left = false;
				chngparams = true;
				continue;
			}
			
			// simple rule building
			if (left)
				pre.append(next);
			else
				post.append(next);
		}
		
		if (escape) {
			throw new TranslatorException(tranErr + "Escape character used illegally");
		}
		if (quote) {
			throw new TranslatorException(tranErr + "Unclosed String literal");
		}
		if (pre.length() == 0) {
			throw new TranslatorException(tranErr + "Search expression missing");
		}
		if (post.length() == 0) {
			throw new TranslatorException(tranErr + "Translation expression missing");
		}
		
		return makeRule(pre.toString(), post.toString(), params.toString(), line);
	}
	
	/**
	 * Given the strings describing the rule, adds the suffix and prefix then creates the rule
	 * @param pre String to be translated
	 * @param post String translation
	 * @return the Rule object
	 */
	private Rule makeRule(String pre, String post, String params, int line) throws TranslatorException {
		
		// append prefix and suffix
		String lhs = prePrefix + pre + preSuffix;
		// special cases, $FIRST and $LAST apply at post translation *fix
		String rhs = post;

		if (postPrefix.equals("@FIRST")) {
			// prefix is $1, every other is incremented, unless for some reason they are group 0
			Matcher m = group.matcher(post);
			String prefix = "$1";
			rhs = prefix;
			int last = 0;
			
			while (m.find()) {
				int number = Integer.valueOf(m.group(1));
				if (number != 0) {
					number = number + 1;
				}
				int pos = m.start();
				
				rhs = rhs + post.substring(last, pos) + "$" + number;
				last = m.end();
			}
			rhs = rhs + post.substring(last);
		}
		else {
			rhs = postPrefix + rhs;
		}
		
		if (postSuffix.equals("@LAST")) {
			// one more than the highest group
			// so for each group encountered, increment
			Matcher m = group.matcher(rhs);
			int group = 1;
			while (m.find()) {
				group++;
			}
			rhs = rhs + "$" + group;
		}
		else {
			rhs = rhs + postSuffix;
		}

		try {
			Pattern.compile(lhs);
			Pattern.compile(rhs);
		} catch (PatternSyntaxException e) {
			throw new TranslatorException("Regular Expression Syntax Error in file "  + file + " line " + line + ".\n" + e.getMessage());
		}
		
		Rule r = new Rule(lhs, rhs, file, line);
		
		String[] p = params.replace(" ", "").split(",");
		if (p.length > 0 && p[0].length() > 0) {
			r.setSwapCount(p[0]);
		}
		if (p.length > 1 && p[1].length() > 0) {
			r.setStartIndex(p[1]);
		}
		if (p.length > 2 && p[2].length() > 0) {
			r.setStep(p[2]);
		}
		
//		System.out.println(r);
		
		return r;
	}

	/**
	 * Parse the config rule, changes the way the rule file may be read
	 * @param rule the config rule
	 * @param line the current line in the file
	 * @throws TranslatorException
	 * @throws CodeException 
	 */
	private void config(String rule, int line) throws TranslatorException, CodeException {
		String configErr = "Invalid configuration rule in file " + file + " line " + line + ".\n"; 
		// strip off config flag
		String configRule = rule.substring(1);
		if (!configRule.contains("=")) {
			throw new TranslatorException(configErr + "Missing assignment symbol ( = )");
		}
		// format taken as %(flag) = (value)
		String flag = "";
		String value = "";
		int i = 0;
		boolean left = true;
		while (i < configRule.length()) {
			char c = configRule.charAt(i);
			i++;
			
			if (c == ' ') continue;
			if (c == '=' && left) {
				left = false;
				continue;
			}
			if (left)
				flag += Character.toString(c);
			else
				value += Character.toString(c);
		}
		
		if (flag.length() == 0) {
			throw new TranslatorException(configErr + "No flag supplied");
		}
		if (value.length() == 0) {
			throw new TranslatorException(configErr + "No value supplied");
		}
		
		flag = flag.toLowerCase();
		
		switch(flag) {
		
		case "comment":
			comment = value;
			break;
			
		case "prefix_l":
			prePrefix = value;
			break;
			
		case "suffix_l":
			preSuffix = value;
			break;
			
		case "prefix_r":
			postPrefix = value;
			break;
			
		case "suffix_r":
			postSuffix = value;
			break;
			
		case "import":
			if (files.contains(value)) {
				throw new TranslatorException(configErr + "Illegal cyclic import");
			}
			files.push(value);
			String oldFile = file;
			// comments not shared
			String comm = comment;
			comment = null;
			readFile(value);
			comment = comm;
			file = oldFile;
			files.pop();
			break;
			
		default:
			throw new TranslatorException(configErr + "Unknown flag: " + flag);
		}
		if (comment.startsWith("%") || comment.startsWith("$") || comment.startsWith("@")) 
			throw new TranslatorException(configErr + "Special character " + comment.charAt(0) + " reserved and cannot be used for comments");
	}

	public int getPrintCount() {
		return interpreter.getPrintNumber();
	}
	public String getPrint(int index) {
		return interpreter.getPrint(index);
	}
}
