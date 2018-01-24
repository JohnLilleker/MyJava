package translator;

/**
 * A rule, basically a tuple of pre and post<br>
 * On translation, all cases of pre is replaced with post
 * @author JB227
 *
 */

public class Rule extends Syntax {
	private String pre,  post, count = "*", start_index = "0", step = "1";
	public Rule(String pr, String arg1, String f, int l) {
		super(f, l);
		pre = pr; post = arg1;
	}
	
	public String getPre() { return pre; }
	public String getPost() { return post; }
	public String getSwapCount() { return count; };
	public String getStartIndex() { return start_index; };
	public String getStep() { return step; };

	public void setSwapCount(String c) { count = c; }
	public void setStartIndex(String s) { start_index = s; }
	public void setStep(String s) { step = s; }
	
	public String toString() { return "[" + pre + " =(" + count + " " + start_index + " " + step + ") " + post + "] " + super.toString(); }

	@Override
	public boolean isRule() {
		return true;
	}

	@Override
	public boolean isCode() {
		return false;
	}
}
