package AST;

public class AST_Argument extends AST_Node {
	
	private AST_NodeType type;
	
	public AST_Argument(AST_Node left, AST_Node right) {
		super(left, right);
		this.type = AST_NodeType.ARGUMENT;
	}

	@Override
	public AST_NodeType getNodeType() {
		return this.type;
	}
	
	public String toString() {
		return "#";
	}

	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
