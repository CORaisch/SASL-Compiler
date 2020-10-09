package Tokens;

/**
 * A NumToken is a specific type of {@link Token}. It is used to represent an
 * integer value. RegEx: [0-9]+
 * 
 * @author David, Claudio
 */
public class T_Num extends Token {

	private int value;

	/**
	 * constructs a new {@link Token} of type {@code 'NumToken'} and initializes
	 * its value to {@code 'val'}
	 * 
	 * @param val initial value of the new NumToken
	 */
	public T_Num(int val) {
		this.setType(TokenType.NUM);
		this.value = val;
	}

	/**
	 * getValue() is used to get the value of a NumToken
	 * 
	 * @return value the NumToken currently has
	 */
	public int getValue() {
		return value;
	}

	/**
	 * toString() produces a string representation of the current value a NumToken
	 * has
	 * 
	 * @return string representation of current value of NumToken
	 */
	public String toString() {
		return String.valueOf(this.value);
	}
}
