package AST;

public abstract class AST_Leaf extends AST_Node {
	private AST_NodeType type;

	public AST_Leaf(AST_NodeType type) {
		this.type = type;
		this.rightChild = null;
		this.leftChild = null;
	}		
	
	public abstract String toString();
	
	@Override
	public AST_NodeType getNodeType() {
		return type;
	}	
	
}
