package BCT;

public class BCT_BuiltIn extends BCT_Node {

	public BCT_BuiltIn(BCT_NodeType type) {
		super(null, null);
		this.type = type;
	}

	public BCT_BuiltIn(String stringType) {
		super(null, null);
		this.type = getTypeFromString(stringType);
	}

	private BCT_NodeType getTypeFromString(String stringType) {
		switch (stringType) {
		case "MINUS":
			return BCT_NodeType.MINUS;
		case "PLUS":
			return BCT_NodeType.PLUS;
		case "MUL":
			return BCT_NodeType.MUL;
		case "DIV":
			return BCT_NodeType.DIV;
		case "HD":
			return BCT_NodeType.HD;
		case "TL":
			return BCT_NodeType.TL;
		case "COND":
			return BCT_NodeType.COND;
		case "AND":
			return BCT_NodeType.AND;
		case "OR":
			return BCT_NodeType.OR;
		case "NOT":
			return BCT_NodeType.NOT;
		case "EQUALS":
			return BCT_NodeType.EQ;
		case ":":
			return BCT_NodeType.COLON;
		case "NIL":
			return BCT_NodeType.NIL;
		case "NEQ":
			return BCT_NodeType.NEQ;
		case "LT":
			return BCT_NodeType.LT;
		case "GT":
			return BCT_NodeType.GT;
		case "LEQ":
			return BCT_NodeType.LEQ;
		case "GEQ":
			return BCT_NodeType.GEQ;
		case "S":
			return BCT_NodeType.S;
		case "K":
			return BCT_NodeType.K;
		case "I":
			return BCT_NodeType.I;
		case "Y":
			return BCT_NodeType.Y;
		case "U":
			return BCT_NodeType.U;
		case "B":
			return BCT_NodeType.B;
		case "C":
			return BCT_NodeType.C;
		case "S'":
			return BCT_NodeType.S_;
		case "B*":
			return BCT_NodeType.B_;
		case "C'":
			return BCT_NodeType.C_;
		default:
			System.err.println("Should not happen (BCT_BuiltIn.getType)");
			return null;
		}
	}

	@Override
	public String toString() {
		String string = "";

		switch (this.getNodeType()) {
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
		case B:
			string += "B";
			break;
		case C:
			string += "C";
			break;
		case S_:
			string += "S'";
			break;
		case B_:
			string += "B*";
			break;
		case C_:
			string += "C'";
			break;
		case COLON:
			string += ":";
			break;
		case NIL:
			string += "nil";
			break;
		default:
			System.err.println("That should not happen! (BCT_BuiltIn)");
			string += "Unknown BuiltIn in Compiler!";
		}
		return string;
	}

	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}
}
