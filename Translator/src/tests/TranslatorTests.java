package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import translator.CodeException;
import translator.Translator;
import translator.TranslatorException;

public class TranslatorTests {
	
	@Test
	public void testGoodFile() {
		try {
			new Translator("src/tests/ruleFiles/OKDictionary.txt");
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testEmptyConfig() {
		try {
			new Translator("src/tests/ruleFiles/BadDict1.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid translation rule in file src/tests/ruleFiles/BadDict1.txt line 1.\nMissing assignment symbol ( = )", e.getMessage());
		}
	}
	@Test
	public void testUnknownConfig() {
		try {
			new Translator("src/tests/ruleFiles/BadDict2.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid configuration rule in file src/tests/ruleFiles/BadDict2.txt line 1.\nUnknown flag: file", e.getMessage());
		}
	}
	@Test
	public void testNoConfigValue() {
		try {
			new Translator("src/tests/ruleFiles/BadDict3.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid configuration rule in file src/tests/ruleFiles/BadDict3.txt line 1.\nNo value supplied", e.getMessage());
		}
	}
	@Test
	public void testNoConfigFlag() {
		try {
			new Translator("src/tests/ruleFiles/BadDict4.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid configuration rule in file src/tests/ruleFiles/BadDict4.txt line 1.\nNo flag supplied", e.getMessage());
		}
	}
	@Test
	public void testConfigSameAsComment() {
		try {
			new Translator("src/tests/ruleFiles/BadDict5.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid configuration rule in file src/tests/ruleFiles/BadDict5.txt line 1.\nSpecial character $ reserved and cannot be used for comments", e.getMessage());
		}
	}
	@Test
	public void testBadEscapeTranslation() {
		try {
			new Translator("src/tests/ruleFiles/BadDict6.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid translation rule in file src/tests/ruleFiles/BadDict6.txt line 1.\nEscape character used illegally", e.getMessage());
		}
	}
	@Test
	public void testMultipleAssignmentSymbols() {
		try {
			new Translator("src/tests/ruleFiles/BadDict7.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid translation rule in file src/tests/ruleFiles/BadDict7.txt line 5.\nMultiple unescaped assignment symbols ( = )", e.getMessage());
		}
	}
	@Test
	public void testCyclicImport() {
		try {
			new Translator("src/tests/ruleFiles/BadDict8.txt");
			fail();
		} catch (TranslatorException | CodeException e) {
			assertEquals("Invalid configuration rule in file src/tests/ruleFiles/BadDict8.txt line 4.\nIllegal cyclic import", e.getMessage());
		}
	}
	@Test
	public void testTranslateZC() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/Translations1.txt");
			assertEquals("cip, cap, coom", t.translate("zip, zap, zoom"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testTranslateCatDog() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/Translations1.txt");
			assertEquals("Feed the dog", t.translate("Feed the cat"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testTranslateCatchDrop() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/Translations1.txt");
			assertEquals("drop the dog", t.translate("catch the cat"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testTranslateNumbers() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/Translations1.txt");
			assertEquals("17 83/100", t.translate("17.83"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testImportRules() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/Translations1.txt");
			assertEquals("Dumbo isn't scared of mice, mice are scared of Dumbo", t.translate("The elephant isn't scared of mice, mice are scared of the elephant"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testChangeParam1() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/TranslationCat1.txt");
			assertEquals("a cat in a hat sat on the mat with the rat", t.translate("The cat in the hat sat on the mat with the rat"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testChangeParam2() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/TranslationCat2.txt");
			assertEquals("The cat in a hat sat on a mat with the rat", t.translate("The cat in the hat sat on the mat with the rat"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testChangeParam3() {
		try {
			Translator t = new Translator("src/tests/ruleFiles/TranslationCat3.txt");
			assertEquals("The cat in a hat sat on the mat with a rat", t.translate("The cat in the hat sat on the mat with the rat"));
		} catch (TranslatorException | CodeException e) {
			fail(e.getMessage());
		}
	}
}
