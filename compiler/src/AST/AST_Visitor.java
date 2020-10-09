package AST;

public interface AST_Visitor {
	
	public void visit(AST_Application abst);			
	public void visit(AST_Leaf leaf);		
	public void visit(AST_Abstraction abstr);	
	public void visit(AST_Argument args);	
	public void visit(AST_Semicolon semi);	
	public void visit(AST_Where where);		
	public void visit(AST_BuiltIn node);
	public void visit(AST_Identifier node);
	public void visit(AST_Bool node);
	public void visit(AST_Num node);
	public void visit(AST_String node);
}
