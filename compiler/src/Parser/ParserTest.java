package Parser;

import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import AST.AST;
import AST.AST_TreePrinter;
import Lexer.Lexer;
import Lexer.LexerTest;

public class ParserTest {
	private final AST_TreePrinter treePrinter = new AST_TreePrinter();
	
	@Test
	public void myplus() {
		File file = LexerTest.createTestFile("def myplus x = x+1 . myplus 1");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> result = pars.createDefList();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("myplus"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		AST func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "@ myplus 1 ";
		Assert.assertEquals(expected, actual);
		
		/*Testing myplus-AST*/
		AST myplus = result.get("myplus");
		actual = treePrinter.print(myplus);
		expected = "= # myplus x @ @ PLUS x 1 ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void where() {
		File file = LexerTest.createTestFile("def test = a where a = b where b = 2 . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> result = pars.createDefList();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		AST func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "test ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		AST test = result.get("test");
		actual = treePrinter.print(test);
		expected = "= test Where a = a Where b = b 2 ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void multipleWhere() {
		File file = LexerTest.createTestFile("def test = a + b where a = 1 ; b = 2 . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> result = pars.createDefList();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		AST func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "test ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		AST test = result.get("test");
		actual = treePrinter.print(test);
		expected = "= test Where @ @ PLUS a b ; = a 1 = b 2 ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void constant() {
		File file = LexerTest.createTestFile("42");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> result = pars.createDefList();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		AST func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "42 ";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void multipleVariable() {
		File file = LexerTest.createTestFile("def test a b c = a + b + c . test");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);
		
		HashMap<String, AST> result = pars.createDefList();
		
		/*Testing HashMap*/
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.containsKey("test"));
		Assert.assertTrue(result.containsKey("00FuncCall00"));
		
		String actual;
		String expected;
		
		/*Testing 00FuncCall00-AST*/
		AST func = result.get("00FuncCall00");
		actual = treePrinter.print(func);
		expected = "test ";
		Assert.assertEquals(expected, actual);
		
		/*Testing test-AST*/
		AST test = result.get("test");
		actual = treePrinter.print(test);
		expected = "= # # # test a b c @ @ PLUS @ @ PLUS a b c ";
		Assert.assertEquals(expected, actual);
	}
}
