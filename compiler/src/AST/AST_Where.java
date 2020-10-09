package AST;

import java.util.HashMap;

public class AST_Where extends AST_Node {
	
	private HashMap<String, AST> localDefs;
	private AST_NodeType type;
	
	public AST_Where(AST_Node left, AST_Node right) {
		super(left, right);
		this.type = AST_NodeType.WHERE;
		this.localDefs = new HashMap<String, AST>();
	}
	
	public AST_Where(AST_Node left, AST_Node right, HashMap<String, AST> localDefs) {
		super(left, right);
		this.type = AST_NodeType.WHERE;
		this.localDefs = localDefs;
	}
	
	@Override
	public AST_NodeType getNodeType() {	
		return this.type;
	}
	
	public HashMap<String, AST> getLocalDefs() {
		return this.localDefs;
	}
	
	public boolean setLocalDef(String key, AST value) {
		if(!this.localDefs.containsKey(key)) {
			this.localDefs.put(key, value);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "Where";
	}
	
	@Override
	public void accept(AST_Visitor v) {
		v.visit(this);
	}
}
