package BCT;

public class BCT_ConstantString extends BCT_Node {
	
	private String value;
	
	public BCT_ConstantString(String str) {
		super(null, null);
		this.value = str;
		this.type = BCT_NodeType.CONSTANT_STRING;
	}
	
	public String toString() {
		return this.value;
	}
	
	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}
}