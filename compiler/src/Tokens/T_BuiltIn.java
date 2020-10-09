package Tokens;

/**
 * A BuiltInToken is a special {@link Token}. It is used to represent the
 * functions HD and TL. RegEx: (hd|tl)
 * 
 * @author David, Claudio
 */
public class T_BuiltIn extends Token {

	/**
	 * constructs a new BuiltInToken with a certain type
	 * 
	 * @param type type of the BuiltInToken {HD, TL}
	 */
	public T_BuiltIn(TokenType type) {
		this.setType(type);
	}

	/**
	 * toString() produces a string representation of a BuiltInToken
	 * 
	 * @return string representation of the BuiltInToken
	 */
	public String toString() {
		return TokenType.toString(this.getType());
	}
}
