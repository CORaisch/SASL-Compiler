package Tokens;

/**
 * Token is an abstract class used to give all Token-Classes a similar structure.
 * A Token has a specific {@link TokenType} which defines its purpose.
 * 
 * @author David, Claudio
 */
public abstract class Token {
	private TokenType type;
	
	/**
	 * getType() is used to get the TokenType of the Token it is used on
	 * 
	 * @return type the Token currently has
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * setType() is used to set the TokenType of the Token it is used on
	 * 
	 * @param type type the Token will have from now on
	 */
	public void setType(TokenType type) {
		this.type = type;
	}
	
	/**
	 * Each Token has to have a toString()-Method which will print all vital
	 * information about said Token
	 */
	public abstract String toString();	
}
