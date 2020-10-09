package AST;

public class AST_Num extends AST_Leaf {
	private int value;
	
	public AST_Num(int val){
		super(AST_NodeType.NUM);
		this.value = val;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
