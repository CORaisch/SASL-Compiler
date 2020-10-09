package Lexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import Tokens.T_Bool;
import Tokens.T_ID;
import Tokens.T_Num;
import Tokens.T_String;
import Tokens.TokenType;

public class LexerTest {

	public static File createTestFile(String paramString) {
		File file = new File("test.sasl");
		
		try {
			file.deleteOnExit();
			BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(file, false));
			localBufferedWriter.write(paramString);
			localBufferedWriter.close();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		
		return file;
	}
	
	@Test
	public void comment() throws Exception {
		File file = createTestFile("42 ||This is a test! 123");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 42);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 123);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void justComment() throws Exception {
		File file = createTestFile("||This is \n ||a test");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void string() throws Exception {
		File file = createTestFile("def\"Test string\"");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.nextToken().getType() == TokenType.DEF);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.STRING);
		Assert.assertEquals(((T_String)lex.nextToken()).getValue(), "Test string");
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void num() throws Exception {
		File file = createTestFile("def 789 if 234");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.nextToken().getType() == TokenType.DEF);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 789);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.IF);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 234);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void bool() throws Exception {
		File file = createTestFile("if true then false");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.nextToken().getType() == TokenType.IF);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.BOOL);
		Assert.assertEquals(((T_Bool)lex.nextToken()).getValue(), true);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.THEN);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.BOOL);
		Assert.assertEquals(((T_Bool)lex.nextToken()).getValue(), false);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void symbol() throws Exception {
		File file = createTestFile("<= ; ~= : > -");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.nextToken().getType() == TokenType.LEQ);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.SEMICOLON);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.NEQ);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.COLON);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.GT);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.MINUS);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
	
	@Test
	public void myplus() throws Exception {
		File file = createTestFile("def myplus x = x+1 \n . \n myplus 1");
		Lexer lex = new Lexer(file);
		
		Assert.assertTrue(lex.nextToken().getType() == TokenType.DEF);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.ID);
		Assert.assertEquals(((T_ID)lex.nextToken()).getValue(), "myplus");
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.ID);
		Assert.assertEquals(((T_ID)lex.nextToken()).getValue(), "x");
		Assert.assertTrue(lex.nextToken().getType() == TokenType.EQ);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.ID);
		Assert.assertEquals(((T_ID)lex.nextToken()).getValue(), "x");
		Assert.assertTrue(lex.nextToken().getType() == TokenType.PLUS);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 1);
		Assert.assertTrue(lex.nextToken().getType() == TokenType.DOT);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.ID);
		Assert.assertEquals(((T_ID)lex.nextToken()).getValue(), "myplus");
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.NUM);
		Assert.assertEquals(((T_Num)lex.nextToken()).getValue(), 1);
		Assert.assertTrue(lex.lookAhead().getType() == TokenType.EOF);
	}
}
