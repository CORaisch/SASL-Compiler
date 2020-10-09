package BCT;

public class BCT_TreePrinter implements BCT_Visitor{
	private String stringRepresentation;
	
	public BCT_TreePrinter(){
		stringRepresentation = "";
	}
	
	public String print(BCT tree){
		stringRepresentation = "";
		
		BCT_Node root = tree.getRoot();
		root.accept(this);
		
		return stringRepresentation;
	}

	@Override
	public void visit(BCT_Application node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
		
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	@Override
	public void visit(BCT_BuiltIn node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(BCT_ConstantNum node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(BCT_ConstantString node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(BCT_ConstantBool node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(BCT_Variable node) {
		stringRepresentation += "VAR";
		stringRepresentation += " ";
	}

	@Override
	public void visit(BCT_Pair node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
		
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}
}
