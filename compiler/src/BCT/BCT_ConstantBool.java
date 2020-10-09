package BCT;

public class BCT_ConstantBool extends BCT_Node {
	
	private boolean value;
	
	public BCT_ConstantBool(boolean val) {
		super(null, null);
		this.value = val;
		this.type = BCT_NodeType.CONSTANT_BOOL;
	}
	
	public boolean getValue(){
		return this.value;
	}
	
	public String toString() {
		return String.valueOf(this.value);
	}

	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}
}