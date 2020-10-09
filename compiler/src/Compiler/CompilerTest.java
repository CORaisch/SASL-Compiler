package Compiler;

import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import AST.AST;
import BCT.BCT;
import BCT.BCT_TreePrinter;
import Lexer.Lexer;
import Lexer.LexerTest;
import Parser.Parser;

public class CompilerTest {
	
	private final BCT_TreePrinter treePrinter = new BCT_TreePrinter();
	
	@Test
	public void testMultibleVariables() {
		File file = LexerTest.createTestFile("def test a b = a + b . test 1 1");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);

		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> result = comp.compile();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		BCT func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ @ @ @ S @ @ S @ K S @ @ S @ @ S @ K S @ @ S @ K K @ K PLUS @ @ S @ K K I @ K I 1 1 ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		BCT test = result.get("test");
		actual = treePrinter.print(test);
		expected = "@ @ S @ @ S @ K S @ @ S @ @ S @ K S @ @ S @ K K @ K PLUS @ @ S @ K K I @ K I ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testList() {
		File file = LexerTest.createTestFile("def test = [1,2,3] . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);

		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> result = comp.compile();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		BCT func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ @ : 1 @ @ : 2 @ @ : 3 nil ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		BCT test = result.get("test");
		actual = treePrinter.print(test);
		expected = "@ @ : 1 @ @ : 2 @ @ : 3 nil ";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testWhere() {
		File file = LexerTest.createTestFile("def test = a where a = b where b = 2 . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> result = comp.compile();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		BCT func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ I @ I 2 ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		BCT test = result.get("test");
		actual = treePrinter.print(test);
		expected = "@ I @ I 2 ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testMultibleWhere() {
		File file = LexerTest.createTestFile("def test = a + b where a = 1 ; b = 2 . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> result = comp.compile();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		BCT func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ @ U @ @ S @ K U @ @ S @ @ S @ K S @ @ S @ K K @ K K @ @ S @ @ S @ K S @ @ " + 
				"S @ @ S @ K S @ @ S @ K K @ K PLUS @ @ S @ K K I @ K I @ @ : 1 @ @ : 2 nil ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		BCT test = result.get("test");
		actual = treePrinter.print(test);
		expected = "@ @ U @ @ S @ K U @ @ S @ @ S @ K S @ @ S @ K K @ K K @ @ S @ @ S @ K S @ @ " +
				"S @ @ S @ K S @ @ S @ K K @ K PLUS @ @ S @ K K I @ K I @ @ : 1 @ @ : 2 nil ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void myplus() {
		File file = LexerTest.createTestFile("def myplus x = x+1 . myplus 1");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> result = comp.compile();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("myplus"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		BCT func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ @ @ S @ @ S @ K @ PLUS I @ K 1 1 ";
		//Assert.assertEquals(expected, actual);
		
		/*Testing myplus-AST*/
		BCT myplus = result.get("myplus");
		actual = treePrinter.print(myplus);
		expected = "@ @ S @ @ S @ K PLUS I @ K 1 ";
		Assert.assertEquals(expected, actual);
	}
}
