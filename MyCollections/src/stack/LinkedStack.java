package stack;

import java.util.EmptyStackException;

/**
 * An implementation of a stack where each item is stored in a wrapper class
 * with a pointer to the element underneath, not unlike a singly-linked list
 * 
 * @author JB227
 *
 * @param <K>
 */
public class LinkedStack<K> implements Stack<K> {

	// the wrapper class for storing items
	private class StackItem {
		private K item;
		private StackItem next;

		public StackItem(K item, StackItem next) {
			super();
			this.item = item;
			this.next = next;
		}

		public K getItem() {
			return item;
		}

		public StackItem getNext() {
			return next;
		}

	}

	private int size;
	private StackItem top;

	public LinkedStack() {
		size = 0;
		top = null;
	}

	@Override
	public K pop() {
		if (top == null) {
			throw new EmptyStackException();
		}
		K item = top.getItem();
		top = top.getNext();
		size--;
		return item;
	}

	@Override
	public boolean push(K obj) {
		StackItem next = new StackItem(obj, top);
		top = next;
		size++;
		return true;
	}

	@Override
	public K peek() {
		if (top == null)
			return null;
		return top.getItem();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return top == null;
	}

	@Override
	public int find(K obj) {
		int index = 0;
		boolean found = false;

		StackItem query = top;

		while (query != null && !found) {
			found = query.getItem().equals(obj);
			index++;
			query = query.getNext();
		}

		return found ? index : -1;
	}

	public String toString() {
		StringBuilder stackString = new StringBuilder("[");

		if (top != null) {
			stackString.append('{').append(top.getItem()).append('}');

			StackItem next = top.getNext();

			while (next != null) {
				stackString.append(", ").append(next.getItem());
				next = next.getNext();
			}

		}

		return stackString.append(']').toString();
	}
}
