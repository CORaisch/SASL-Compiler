package AST;

public abstract class AST_Node implements AST_Acceptor {

	protected AST_Node leftChild;
	protected AST_Node rightChild;
	protected AST_NodeType type;
	
	/*
	 * 
	 * Constructors
	 * 
	 */
	
	public AST_Node(){}
	
	public AST_Node(AST_Node left, AST_Node right) {
		this.leftChild = left;
		this.rightChild = right;
	}
	
	/*
	 * 	 
	 * Getters
	 * 
	 */
	
	public AST_Node getRightChild() {
		return this.rightChild;
	}
	
	public AST_Node getLeftChild() {
		return this.leftChild;
	}
	
	public abstract AST_NodeType getNodeType();
	
	/*
	 * 
	 * Setters
	 * 
	 */
	
	public void setRightChild(AST_Node right) {
		this.rightChild = right; 
	}
	
	public void setLeftChild(AST_Node left) {
		this.leftChild = left;
	}
	
	public abstract String toString();

}
