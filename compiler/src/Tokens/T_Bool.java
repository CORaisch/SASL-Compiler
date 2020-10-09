package Tokens;

/**
 * A BoolToken is a specific type of {@link Token}. It is used to represent a
 * boolean value. RegEx: (true|false)
 * 
 * @author David, Claudio
 */
public class T_Bool extends Token {

	private boolean value;

	/**
	 * constructs a new Token of type 'Bool' and initializes its value to 'val'
	 * 
	 * @param val initial value of the new BoolToken
	 */
	public T_Bool(boolean val) {
		this.setType(TokenType.BOOL);
		this.value = val;
	}

	/**
	 * isValue() is used to get the value of a BoolToken
	 * 
	 * @return value the BoolToken currently has
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * toString() produces a string representation of the current value of a
	 * BoolToken
	 * 
	 * @return string representation of current value of BoolToken
	 */
	public String toString() {
		return String.valueOf(this.value);
	}
}
