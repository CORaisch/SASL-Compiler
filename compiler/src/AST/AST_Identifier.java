package AST;

public class AST_Identifier extends AST_Leaf {
	
	private String value;
	
	public AST_Identifier(String val) {
		super(AST_NodeType.IDENTIFIER);
		this.value = val;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return this.getValue();
	}

	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
