package AST;

public class AST_BuiltIn extends AST_Leaf {
	
	public AST_BuiltIn(AST_NodeType type) {
		super(type);
	}
	
	@Override
	public String toString() {
		String string = "";
		
		switch (this.getNodeType()){
		case MINUS: 
			string += "MINUS";
			break;
		case PLUS:
			string += "PLUS";
			break;
		case MUL:
			string += "MUL";
			break;
		case DIV:
			string += "DIV";
			break;
		case HD:
			string += "HD";
			break;
		case TL:
			string += "TL";
			break;
		case COND:
			string += "COND";
			break;
		case AND:
			string += "AND";
			break;
		case OR:
			string += "OR";
			break;
		case NOT:
			string += "NOT";
			break;
		case EQ:
			string += "EQUALS";
			break;
		case COLON:
			string += ":";
			break;
		case NIL:
			string += "NIL";
			break;
		case NEQ:
			string += "NEQ";
			break;
		case LT:
			string += "LT";
			break;
		case GT:
			string += "GT";
			break;
		case LEQ:
			string += "LEQ";
			break;
		case GEQ:
			string += "GEQ";
			break;
		case S:
			string += "S";
			break;
		case K:
			string += "K";
			break;
		case I:
			string += "I";
			break;
		case Y:
			string += "Y";
			break;
		case U:
			string += "U";
			break;
		default:
			System.out.println("That should not happen!");
			string += "Unknown BuiltIn!";
		}
		return string;
	}
	
	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
