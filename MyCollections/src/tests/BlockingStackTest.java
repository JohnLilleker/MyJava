package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import stack.BlockingStack;

// tests when a blocking stack stops accepting pushes 
class BlockingStackTest {

	@Test
	void testPush() {

		// new stack capacity 10
		BlockingStack<Integer> subject = new BlockingStack<>(10);
		// add ints 0 - 8 (9 numbers)
		for (int i = 0; i < 9; i++) {
			subject.push(i);
		}
		// 9 should be added (10th item)
		assertTrue(subject.push(9));
		assertEquals(10, subject.size());

		// no room for 10 and 11
		assertFalse(subject.push(10));
		assertEquals(10, subject.size());
		assertFalse(subject.push(11));
		assertEquals(10, subject.size());

		// should contain numbers 0-9
		for (int i = 0; i < 10; i++) {
			assertNotEquals(-1, subject.find(i));
		}
		// no 10 or 11
		assertEquals(-1, subject.find(10));
		assertEquals(-1, subject.find(11));
	}

}
