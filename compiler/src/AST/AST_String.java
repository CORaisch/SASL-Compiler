package AST;

public class AST_String extends AST_Leaf {
	
	private String value;
	
	public AST_String (String val){
		super(AST_NodeType.SASL_STRING);
		this.value = val;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
