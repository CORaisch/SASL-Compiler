package AST;

public class AST {

	private AST_Node root;
	
	public AST(AST_Node rt) {
		this.root = rt;
	}
	
	public void setRoot(AST_Node rt) {
		this.root = rt;
	}
	
	public AST_Node getRoot() {
		return this.root;
	}
 
}
