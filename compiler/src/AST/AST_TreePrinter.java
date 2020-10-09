package AST;

public class AST_TreePrinter implements AST_Visitor{
	private String stringRepresentation;
	
	public AST_TreePrinter(){
		stringRepresentation = "";
	}
	
	public String print(AST tree){
		stringRepresentation = "";
		
		AST_Node root = tree.getRoot();
		root.accept(this);
		
		return stringRepresentation;
	}
	
	@Override
	public void visit(AST_Application abst) {
		stringRepresentation += abst.toString();
		stringRepresentation += " ";
		
		abst.getLeftChild().accept(this);
		abst.getRightChild().accept(this);
	}

	@Override
	public void visit(AST_Leaf leaf) {}

	@Override
	public void visit(AST_Abstraction abstr) {
		stringRepresentation += abstr.toString();
		stringRepresentation += " ";
		
		abstr.getLeftChild().accept(this);
		abstr.getRightChild().accept(this);
	}

	@Override
	public void visit(AST_Argument args) {
		stringRepresentation += args.toString();
		stringRepresentation += " ";
		
		args.getLeftChild().accept(this);
		args.getRightChild().accept(this);
	}

	@Override
	public void visit(AST_Semicolon semi) {
		stringRepresentation += semi.toString();
		stringRepresentation += " ";
		
		semi.getLeftChild().accept(this);
		semi.getRightChild().accept(this);
	}

	@Override
	public void visit(AST_Where where) {
		stringRepresentation += where.toString();
		stringRepresentation += " ";
		
		where.getLeftChild().accept(this);
		where.getRightChild().accept(this);
	}

	@Override
	public void visit(AST_BuiltIn node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(AST_Identifier node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(AST_Bool node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(AST_Num node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

	@Override
	public void visit(AST_String node) {
		stringRepresentation += node.toString();
		stringRepresentation += " ";
	}

}
