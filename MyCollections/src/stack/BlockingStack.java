package stack;

import java.util.EmptyStackException;

/**
 * An implementation of a stack that can be filled and will stop accepting new
 * items after a certain number of items have been added
 * 
 * @author JB227
 *
 * @param <K>
 */
public class BlockingStack<K> implements Stack<K> {

	private int capacity;
	// points to the next empty space in the items array
	private int top;
	private Object[] items;

	public BlockingStack(int capacity) {
		this.capacity = capacity;
		this.top = 0;
		this.items = new Object[capacity];
	}

	@Override
	public K pop() {
		if (top == 0)
			throw new EmptyStackException();

		@SuppressWarnings("unchecked")
		K item = (K) items[--top];
		items[top] = null;
		return item;
	}

	@Override
	public boolean push(K obj) {
		if (top == capacity)
			return false;
		items[top++] = obj;
		return true;
	}

	@Override
	public K peek() {
		if (top == 0)
			return null;
		@SuppressWarnings("unchecked")
		K item = (K) items[top - 1];
		return item;
	}

	@Override
	public int size() {
		return top;
	}

	@Override
	public boolean isEmpty() {
		return top == 0;
	}

	@Override
	public int find(K obj) {

		boolean found = false;
		int i;

		for (i = 0; i < top; i++) {
			if (items[i].equals(obj)) {
				found = true;
				break;
			}
		}

		// TODO Auto-generated method stub
		return found ? top - i : -1;
	}

	public int getCapacity() {
		return capacity;
	}

}
