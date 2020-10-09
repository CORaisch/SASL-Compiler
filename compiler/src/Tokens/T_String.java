package Tokens;

/**
 * A StringToken is a specific type of {@link Token}. It is used to represent a string
 * value. RegEx: "[^"]*"
 * 
 * @author David, Claudio
 */
public class T_String extends Token {

	private String value;

	/**
	 * constructs a new StringToken and initializes its value to
	 * 'val'
	 * 
	 * @param val initial value of the new SASL_String-Token
	 */
	public T_String(String val) {
		this.setType(TokenType.STRING);
		this.value = val;
	}

	/**
	 * getValue() is used to get the value of a StringToken
	 * 
	 * @return value the StringToken currently has
	 */
	public String getValue() {
		return value;
	}

	/**
	 * toString() produces a string representation of the current value the
	 * StringToken has
	 * 
	 * @return string representation of current value of StringToken
	 */
	public String toString() {
		return "\"" + this.value + "\"";
	}
}
