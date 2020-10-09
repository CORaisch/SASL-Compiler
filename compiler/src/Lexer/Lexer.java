package Lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.regex.Pattern;

import Tokens.T_Bool;
import Tokens.T_BuiltIn;
import Tokens.T_ID;
import Tokens.T_KeyWord;
import Tokens.T_Num;
import Tokens.T_String;
import Tokens.T_Special;
import Tokens.T_Symbol;
import Tokens.Token;
import Tokens.TokenType;

/**
 * The Lexer generates {@link Tokens.Token Tokens} representing a
 * SASL-InputStream.
 * 
 * @author David, Claudio
 */
public class Lexer {
	/* MEMBER DECLARATION */
	private FileInputStream input;
	private PushbackInputStream source;
	private Token nextToken;

	/**
	 * 
	 * @param path
	 */
	public Lexer(File path) {
		try {
			input = new FileInputStream(path);
			source = new PushbackInputStream(input, 2);
			this.genNextToken();
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		} catch (LexerException ex) {
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Lexer(FileInputStream input) {
		source = new PushbackInputStream(input, 2);
	}

	/**
	 * lookAhead() returns the current nextToken
	 * 
	 * @return nextToken
	 */
	public Token lookAhead() {
		return nextToken;
	}

	/**
	 * nextToken() generates the next Token from source and returns the last token generated
	 * 
	 * @return prevToken - the last token generated
	 */
	public Token nextToken() {
		Token prevToken = this.lookAhead();
		try {
			genNextToken();
		} catch (LexerException exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		return prevToken;
	}
	
	/**
	 * 
	 * 
	 * @throws LexerException
	 */
	private void genNextToken() throws LexerException {
		if ((this.nextToken != null) && (this.nextToken.getType() == TokenType.EOF)) {
			throw new LexerException("End of File reached.");
		} else {
			try {
				int curSymbol = source.read();
				curSymbol = ignoreWhitespace(curSymbol);

				if (isCommentary(curSymbol)) {
					curSymbol = source.read();
					if (!isCommentary(curSymbol))
						System.out.println("Unkown Symbol. Maybe check your Commentary!");
					ignoreCommentary(curSymbol);
				} else if (curSymbol == -1 || curSymbol == 255) {
					this.nextToken = new T_Special(TokenType.EOF);
				} else if (curSymbol == '.') {
					this.nextToken = new T_Special(TokenType.DOT);
				} else if (isNumber(curSymbol)) {
					handleNumber(curSymbol);
				} else if (isString(curSymbol)) {
					handleString(curSymbol);
				} else if (isSymbol(curSymbol)) {
					handleSymbol(curSymbol);
				} else if ((curSymbol >= 65 && curSymbol <= 90)
						|| (curSymbol >= 97 && curSymbol <= 122)) {
					handleRest(curSymbol);
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	/*
	 * HELPERS
	 */

	private void handleRest(int curSymbol) throws IOException {
		String totalRest = getRest(curSymbol);

		if (this.isKeyWord(totalRest)) {
			this.nextToken = new T_KeyWord(totalRest);
		} else if (totalRest.equals("true") || totalRest.equals("false")) {
			this.nextToken = new T_Bool(Boolean.parseBoolean(totalRest));
		} else if (totalRest.equals("nil")) {
			this.nextToken = new T_Special(TokenType.NIL);
		} else if (totalRest.equals("and")) {
			this.nextToken = new T_Symbol(TokenType.AND);
		} else if (totalRest.equals("or")) {
			this.nextToken = new T_Symbol(TokenType.OR);
		} else if (totalRest.equals("not")) {
			this.nextToken = new T_Symbol(TokenType.NOT);
		} else if (totalRest.equals("hd")) {
			this.nextToken = new T_BuiltIn(TokenType.HD);
		} else if (totalRest.equals("tl")) {
			this.nextToken = new T_BuiltIn(TokenType.TL);
		} else {
			this.nextToken = new T_ID(totalRest);
		}
	}

	private String getRest(int curSymbol) throws IOException {
		String totalRest = "";

		while (isRest(curSymbol) || curSymbol == 95) {
			totalRest += (char) curSymbol;
			curSymbol = source.read();
		}

		source.unread((char) curSymbol);

		return totalRest;
	}

	private void handleNumber(int curSymbol) throws IOException {
		String stringValue = "";
		int symbol = curSymbol;
		stringValue += (char) symbol;

		while (isNumber(symbol = source.read())) {
			stringValue += (char) symbol;
		}

		source.unread((char) symbol);

		this.nextToken = new T_Num(Integer.parseInt(stringValue));
	}

	private void handleString(int curSymbol) throws IOException {
		String stringValue = "";
		int symbol;
		while ((symbol = source.read()) != '"') {
			stringValue += (char) symbol;
		}

		this.nextToken = new T_String(stringValue);
	}

	private void handleSymbol(int curSymbol) throws IOException {
		String symbolString = "";
		int symbol = curSymbol;
		symbolString += (char) symbol;

		if (Pattern.matches("[<>~]", String.valueOf((char) symbol))) {
			if ((symbol = source.read()) == '=')
				symbolString += (char) symbol;
			else
				source.unread((char) symbol);
		}

		this.nextToken = T_Symbol.getSymbol(symbolString);
	}

	private boolean isRest(int i) {
		if ((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z')
				|| (i >= '0' && i <= '9') || (i == '_')) {
			return true;
		}
		return false;
	}

	private boolean isKeyWord(String str) {
		if (str.equals("def") || str.equals("where") || str.equals("if")
				|| str.equals("then") || str.equals("else")) {
			return true;
		}
		return false;
	}

	private boolean isCommentary(int i) {
		if (i == '|') {
			return true;
		}
		return false;
	}

	private boolean isSymbol(int i) {
		if (Pattern.matches("[:;,+-/*=<>~\\[\\]()]", String.valueOf((char) i))) {
			return true;
		}
		return false;
	}

	private boolean isString(int i) {
		if (i == '"') {
			return true;
		}
		return false;
	}

	private boolean isNumber(int i) {
		if (i >= '0' && i <= '9') {
			return true;
		}
		return false;
	}

	/**
	 * ignoreWhitespace() keeps reading characters from source until it get's a character
	 * that is not space, tab (== 13) or a new line (== 10)
	 * 
	 * @param curSymbol symbol last read from source
	 * @return first symbol that is no whitespace
	 * @throws IOException
	 */
	private int ignoreWhitespace(int curSymbol) throws IOException {

		int symbol = curSymbol;
		while ((Character.isWhitespace(symbol)) || (symbol == 10)
				|| (symbol == 13)) {
			symbol = source.read();
		}

		return symbol;
	}

	/**
	 * ignoreCommentary() keeps reading characters from source until there is a new line
	 * 
	 * @param symbol that starts the comment
	 * @throws IOException
	 */
	private void ignoreCommentary(int symbol) throws IOException {
		while ((symbol != 10) && (symbol != -1))
			symbol = source.read();

		if (symbol == -1){
			this.nextToken = new T_Special(TokenType.EOF);
		} else {
			nextToken();
		}
	}
}
