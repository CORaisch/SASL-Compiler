package Tokens;

/**
 * TokenType is used to define the purpose of a given Token.
 * 
 * @author David, Claudio
 */
public enum TokenType {
	DEF, WHERE, IF, THEN, ELSE, BOOL, STRING, NUM, ID, EOF, PLUS, MINUS, MUL, DIV, EQ, NEQ,
	SEMICOLON, LT, GT, LEQ, GEQ, NOT, AND, OR, DOT, NIL, COLON, LBRACKETR, RBRACKETR,
	LBRACKETS, RBRACKETS, COMMA, TL, HD;
	
	/**
	 * toString() is used to get a string representation of a given TokenType
	 * 
	 * @param type
	 * @return string representation of the type
	 */
	public static String toString(TokenType type) {
		String str = "";
		switch (type) {

		case DEF:
			str += "DEF";
			break;
		case WHERE:
			str += "WHERE";
			break;
		case IF:
			str += "IF";
			break;
		case THEN:
			str += "THEN";
			break;
		case ELSE:
			str += "ELSE";
			break;
		case BOOL:
			str += "BOOL";
			break;
		case STRING:
			str += "String";
			break;
		case NUM:
			str += "Numeric";
			break;
		case ID:
			str += "ID";
			break;
		case EOF:
			str += "EOF";
			break;
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
		case SEMICOLON:
			str += ";";
			break;
		case LT:
			str += "<";
			break;
		case GT:
			str += ">";
			break;
		case LEQ:
			str += "<=";
			break;
		case GEQ:
			str += ">=";
			break;
		case NOT:
			str += "not";
			break;
		case AND:
			str += "and";
			break;
		case OR:
			str += "or";
			break;
		case DOT:
			str += ".";
			break;
		case NIL:
			str += "nil";
			break;
		case COLON:
			str += ":";
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
		case COMMA:
			str += ",";
		case TL:
			str += "TL";
			break;
		case HD:
			str += "HD";
			break;
		default:
			str += "<No Valid Token>";
		}
		return str;
	}
}