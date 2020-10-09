package Tokens;

/**
 * A Symbol is a specific type of {@link Token}. It is used to represent a
 * symbol. RegEx: (+|-|*|/|=|~=|<=|>=|<|>|\(|\)|\[|\]|:|,|;|AND|OR|NOT)
 * 
 * @author David, Claudio
 */
public class T_Symbol extends Token {

	/**
	 * constructs a new {@link Token} of type 'Symbol' with given type
	 * {@code 'type'}
	 * 
	 * @param type type of the Symbol-Token
	 */
	public T_Symbol(TokenType type) {
		this.setType(type);
	}

	/**
	 * getSymbol() is used to get a Symbol-Token representing a given
	 * symbolString
	 * 
	 * @param symbolString the new Symbol should represent
	 * @return correct Symbol-Token
	 */
	public static T_Symbol getSymbol(String symbolString) {
		switch (symbolString) {
		case "+":
			return new T_Symbol(TokenType.PLUS);
		case "-":
			return new T_Symbol(TokenType.MINUS);
		case "*":
			return new T_Symbol(TokenType.MUL);
		case "/":
			return new T_Symbol(TokenType.DIV);
		case "=":
			return new T_Symbol(TokenType.EQ);
		case "~=":
			return new T_Symbol(TokenType.NEQ);
		case "<=":
			return new T_Symbol(TokenType.LEQ);
		case ">=":
			return new T_Symbol(TokenType.GEQ);
		case "<":
			return new T_Symbol(TokenType.LT);
		case ">":
			return new T_Symbol(TokenType.GT);
		case "(":
			return new T_Symbol(TokenType.LBRACKETR);
		case ")":
			return new T_Symbol(TokenType.RBRACKETR);
		case "[":
			return new T_Symbol(TokenType.LBRACKETS);
		case "]":
			return new T_Symbol(TokenType.RBRACKETS);
		case ":":
			return new T_Symbol(TokenType.COLON);
		case ",":
			return new T_Symbol(TokenType.COMMA);
		case ";":
			return new T_Symbol(TokenType.SEMICOLON);
		default:
			// Throw exception?
			System.out.println("No valid symbol");
			return null;
		}
	}

	/**
	 * toString() produces a string representation of a Symbol
	 * 
	 * @return string representation of the Symbol
	 */
	public String toString() {
		String str = "";
		switch (this.getType()) {

		case PLUS:
			str += "+";
			break;
		case MINUS:
			str += "-";
			break;
		case MUL:
			str += "*";
			break;
		case DIV:
			str += "/";
			break;
		case EQ:
			str += "=";
			break;
		case NEQ:
			str += "~=";
			break;
		case LEQ:
			str += "<=";
			break;
		case GEQ:
			str += ">=";
			break;
		case LT:
			str += "<";
			break;
		case GT:
			str += ">";
			break;
		case LBRACKETR:
			str += "(";
			break;
		case RBRACKETR:
			str += ")";
			break;
		case LBRACKETS:
			str += "[";
			break;
		case RBRACKETS:
			str += "]";
			break;
		case COLON:
			str += ":";
			break;
		case COMMA:
			str += ",";
			break;
		case SEMICOLON:
			str += ";";
			break;
		case AND:
			str += "AND";
			break;
		case OR:
			str += "OR";
			break;
		case NOT:
			str += "NOT";
			break;
		default:
			System.out.println("FALSCHER AUFRUF IN SYMBOL.TOSTRING().");
		}
		return str;
	}
}
