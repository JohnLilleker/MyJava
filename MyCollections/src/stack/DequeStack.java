package stack;

import java.util.ArrayDeque;
import java.util.EmptyStackException;

/**
 * A wrapper for an ArrayDeque. It accesses the elements using the Stack methods
 * and is neater than simply treating a Deque as a stack
 * 
 * @author JB227
 *
 * @param <K>
 */
public class DequeStack<K> implements Stack<K> {

	private ArrayDeque<K> stack;

	public DequeStack() {
		super();
		stack = new ArrayDeque<>();
	}

	@Override
	public K pop() {
		if (stack.isEmpty())
			throw new EmptyStackException();
		return stack.removeLast();
	}

	@Override
	public boolean push(K obj) {
		stack.addLast(obj);
		return true;
	}

	@Override
	public K peek() {
		if (stack.isEmpty())
			return null;
		return stack.getLast();
	}

	@Override
	public int size() {
		return stack.size();
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public int find(K obj) {
		int index = stack.size();
		boolean found = false;

		for (K item : stack) {
			if (item.equals(obj)) {
				found = true;
				break;
			}
			index--;
		}
		return found ? index : -1;
	}

	public String toString() {
		return stack.toString();
	}

}
