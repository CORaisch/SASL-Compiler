package BCT;

public class BCT_Pair extends BCT_Node {

	public BCT_Pair(BCT_Node left, BCT_Node right) {
		super(left, right);
		this.type = BCT_NodeType.PAIR;
	}

	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return "PAIR";
	}
}
