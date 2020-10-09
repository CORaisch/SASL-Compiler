package Tokens;

/**
 * An IdentifierToken is a special type of {@link Token}. It is used to
 * represent an identifier. RegEx: [a-zA-Z_][a-zA-Z0-9_]*
 * 
 * @author David, Claudio
 */
public class T_ID extends Token {

	private String value;

	/**
	 * constructs a new Token of type 'Id' and initializes its value to 'val'
	 * 
	 * @param val initial value of the new IdentifierToken
	 */
	public T_ID(String val) {
		this.setType(TokenType.ID);
		this.value = val;
	}

	/**
	 * getValue() is used to get the value of an IdentifierToken
	 * 
	 * @return value the Id-Token currently has
	 */
	public String getValue() {
		return value;
	}

	/**
	 * toString() produces a string representation of the current value the
	 * IdentifierToken has
	 * 
	 * @return string representation of current value of IdentifierToken
	 */
	public String toString() {
		return this.value;
	}
}
