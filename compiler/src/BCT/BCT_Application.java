package BCT;

public class BCT_Application extends BCT_Node {

	
	public BCT_Application(BCT_Node left, BCT_Node right) {
		super(left, right);
		this.type = BCT_NodeType.APPLICATION;
	}

	@Override
	public String toString() {
		return "@";
	}

	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}
}
