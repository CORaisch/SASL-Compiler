package BCT;

public class BCT_ConstantNum extends BCT_Node {

	private int value;
	
	public BCT_ConstantNum(int val) {
		super(null, null);
		this.value = val;
		this.type = BCT_NodeType.CONSTANT_NUM;
	}
	
	public String toString() {
		return String.valueOf(this.value);
	}

	public int getValue() {
		return this.value;
	}
	
	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);		
	}
}