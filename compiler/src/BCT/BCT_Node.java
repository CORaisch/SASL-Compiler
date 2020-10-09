package BCT;

public abstract class BCT_Node implements BCT_Acceptor{
	protected BCT_Node leftChild;
	protected BCT_Node rightChild;
	protected BCT_NodeType type;
	
	public BCT_Node(BCT_Node left, BCT_Node right){
		leftChild = left;
		rightChild = right;
	}
	
	public abstract String toString();
	
	public BCT_NodeType getNodeType() {
		return type;
	}
	
	public BCT_Node getLeftChild() {
		return leftChild;
	}
	
	public BCT_Node getRightChild() {
		return rightChild;
	}
	
	public void setLeftChild(BCT_Node leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setRightChild(BCT_Node rightChild) {
		this.rightChild = rightChild;
	}
}
