package BCT;

public interface BCT_Visitor {
	void visit(BCT_Application node);
	void visit(BCT_BuiltIn node);
	void visit(BCT_ConstantNum node);
	void visit(BCT_ConstantString node);
	void visit(BCT_ConstantBool node);
	void visit(BCT_Variable node);
	void visit(BCT_Pair node);
}
