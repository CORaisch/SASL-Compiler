package BCT;

public class BCT {
	private BCT_Node root;
	
	public BCT(BCT_Node node) {
		root = node;
	}
	
	public void setRoot(BCT_Node root) {
		this.root = root;
	}
	
	public BCT_Node getRoot() {
		return root;
	}
}
