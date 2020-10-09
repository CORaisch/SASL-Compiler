package SKIReductionMachine;

import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import AST.AST;
import BCT.BCT;
import Compiler.Compiler;
import Lexer.Lexer;
import Lexer.LexerTest;
import Parser.Parser;

public class ReductionMachineTest {
	
	@Test
	public void myPlus() {
		File file = LexerTest.createTestFile("def myPlus x = x + 1 . myPlus 1");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);

		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> compiledList = comp.compile();
		BCT result = compiledList.get("00FuncCall00");
		
		ReductionMachine rm = new ReductionMachine(result);
		rm.reduce();
		
		String actual = rm.getResult().toString();
		String expected = "2";
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void constant() {
		File file = LexerTest.createTestFile("42");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);

		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> compiledList = comp.compile();
		BCT result = compiledList.get("00FuncCall00");
		
		ReductionMachine rm = new ReductionMachine(result);
		rm.reduce();
		
		String actual = rm.getResult().toString();
		String expected = "42";
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void noFunction() {
		File file = LexerTest.createTestFile("1 + 1");
		Lexer lex = new Lexer(file);
		Parser pars = new Parser(lex);

		HashMap<String, AST> parsedList = pars.createDefList();
		Compiler comp = new Compiler(parsedList);
		
		HashMap<String, BCT> compiledList = comp.compile();
		BCT result = compiledList.get("00FuncCall00");
		
		ReductionMachine rm = new ReductionMachine(result);
		rm.reduce();
		
		String actual = rm.getResult().toString();
		String expected = "2";
		
		Assert.assertEquals(expected, actual);
	}
}
