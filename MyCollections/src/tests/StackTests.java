package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import stack.Stack;
import stack.BlockingStack;
// implementations
import stack.DequeStack;
import stack.LinkedStack;

// A collection of tests any stack implementation should be able to pass
class StackTests {

	// Integers for simplicity
	Stack<Integer> subject;

	@BeforeEach
	void setUp() {
		// subject = new DequeStack<>();
		// subject = new LinkedStack<>();
		subject = new BlockingStack<>(15);
	}

	@Test
	void testConstructor() {
		// tests the empty stack behaves as expected
		assertEquals(0, subject.size());
		assertNull(subject.peek());
		assertTrue(subject.isEmpty());
		assertThrows(EmptyStackException.class, () -> subject.pop());
	}

	@Test
	void testPop() {
		for (int i = 0; i < 5; i++) {
			subject.push(i);
		}

		// reverse the list
		for (int i = 4; i >= 0; i--) {
			assertEquals(Integer.valueOf(i), subject.pop());
		}
		assertTrue(subject.isEmpty());
	}

	@Test
	void testPush() {
		assertTrue(subject.push(3));
		assertTrue(subject.push(5));
		assertTrue(subject.push(7));
		assertEquals(3, subject.size());
	}

	@Test
	void testPeek() {

		assertNull(subject.peek());

		subject.push(3);
		subject.push(4);

		assertEquals(2, subject.size());

		// testing the state doesn't change
		assertEquals(Integer.valueOf(4), subject.peek());
		assertEquals(Integer.valueOf(4), subject.peek());
		assertEquals(2, subject.size());

	}

	@Test
	void testGetSize() {
		assertEquals(0, subject.size());
		for (int i = 0; i < 10; i++) {
			subject.push(i);
		}
		assertEquals(10, subject.size());
		for (int i = 0; i < 5; i++) {
			subject.pop();
		}
		assertEquals(5, subject.size());
	}

	@Test
	void testIsEmpty() {
		subject.push(1);
		assertFalse(subject.isEmpty());
		subject.pop();
		assertTrue(subject.isEmpty());
	}

	@Test
	void testFind() {

		// check empty
		assertEquals(-1, subject.find(3));

		// 4, 3, 2, 1, 0
		for (int i = 0; i < 5; i++) {
			subject.push(i);
		}

		// if it is the head, it would return 1
		assertEquals(1, subject.find(4));
		assertEquals(2, subject.find(3));
		assertEquals(3, subject.find(2));
		assertEquals(4, subject.find(1));
		assertEquals(5, subject.find(0));

		assertEquals(-1, subject.find(5));
		assertEquals(-1, subject.find(-1));

	}

}
