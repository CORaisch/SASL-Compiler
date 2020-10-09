package AST;

public class AST_Bool extends AST_Leaf {
	
	private boolean value;
	
	public AST_Bool(boolean val){
		super(AST_NodeType.BOOL);
		this.value = val;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void setValue(boolean value) {
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
