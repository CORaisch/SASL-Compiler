package BCT;

public class BCT_Variable extends BCT_Node{
	private String name;
	
	public BCT_Variable(String varName){
		super(null, null);
		this.type = BCT_NodeType.VAR;
		name = varName;
	}

	@Override
	public void accept(BCT_Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return name;
	}
}
