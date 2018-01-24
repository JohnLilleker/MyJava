package stack;

/**
 * A Stack, first-in last-out data structure<br>
 * Overall more rigid and better than the java.util implementation
 * 
 * @author JB227
 *
 * @param <K>
 */
public interface Stack<K> {

	/**
	 * Should remove the last item added and return it<br>
	 * Popping an empty stack should throw a java.util.EmptyStackException
	 * 
	 * @return
	 */
	K pop();

	/**
	 * Should add a new item to the stack
	 * 
	 * @param obj
	 * @return
	 */
	boolean push(K obj);

	/**
	 * Returns the last item added without removing it<br>
	 * Also should return null if empty<br>
	 * 
	 * 
	 * @return
	 */
	K peek();

	/**
	 * Number of elements
	 * 
	 * @return
	 */
	int size();

	/**
	 * Check if the stack contains any elements
	 * 
	 * @return
	 */
	boolean isEmpty();

	/**
	 * Searches for an item in the stack, 1-indexed<br>
	 * return -1 if not found (idea came from java.util.Stack)
	 * 
	 * @param obj
	 * @return
	 */
	int find(K obj);
}
