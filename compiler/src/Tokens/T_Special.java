package Tokens;

/**
 * A Special is a specific type of {@link Token}. It is used to represent DOT,
 * NIL and EndOfFile. value. RegEx: (\.|nil|EOF)
 * 
 * @author David, Claudio
 */
public class T_Special extends Token {

	/**
	 * constructs a new {@link Token} of type {@code 'Special'} and initializes
	 * its type to {@code 'type'}
	 * 
	 * @param type initial type of the new Special-Token
	 */
	public T_Special(TokenType type) {
		this.setType(type);
	}

	/**
	 * toString() produces a string representation of a Special
	 * 
	 * @return string representation of the Special
	 */
	public String toString() {
		String str = "";
		switch (this.getType()) {

		case DOT:
			str += ".";
			break;
		case NIL:
			str += "nil";
			break;
		case EOF:
			str += "EOF";
			break;
		default:
			System.out.println("FALSCHER AUFRUF IN SPECIAL.WRITEOUT().");
		}
		return str;
	}

}
