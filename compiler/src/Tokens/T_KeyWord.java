package Tokens;

/**
 * A KeyWord is a specific type of {@link Token}. It is used to represent a
 * keyword of the SASL-language. RegEx: (def|if|then|else|where)
 * 
 * @author David, Claudio
 */
public class T_KeyWord extends Token {

	/**
	 * constructs a new Token of type 'KeyWord' with a certain type
	 * 
	 * @param val initial value of the new KeyWord-Token
	 */
	public T_KeyWord(String keyString) {
		this.setType(selectType(keyString));
	}

	/**
	 * selectType() determines the right {@link TokenType} for the new KeyWord
	 * from a given string
	 * 
	 * @param keyString the string representing the type
	 * @return correct {@link TokenType}
	 */
	private TokenType selectType(String keyString) {
		switch (keyString) {
		case "def":
			return TokenType.DEF;
		case "if":
			return TokenType.IF;
		case "then":
			return TokenType.THEN;
		case "else":
			return TokenType.ELSE;
		case "where":
			return TokenType.WHERE;
		default:
			return null;
		}
	}

	/**
	 * toString() produces a string representation of a KeyWord
	 * 
	 * @returns string representation of the KeyWord
	 */
	public String toString() {
		String str = "";
		if (this.getType() == null)
			;
		else {
			switch (this.getType()) {

			case DEF:
				str += "DEF";
				break;
			case IF:
				str += "IF";
				break;
			case THEN:
				str += "THEN";
				break;
			case ELSE:
				str += "ELSE";
				;
				break;
			case WHERE:
				str += "WHERE";
				break;
			default:
				System.out.println("FALSCHER AUFRUF IN KEYWORD.WRITEOUT().");
			}
		}
		return str;
	}

}
