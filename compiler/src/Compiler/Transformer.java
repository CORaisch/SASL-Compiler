/**TRANSFORMER (VISITOR)
 * 
 * Visitor used during compilation Phase.
 * Its Task is to easily convert ASTs to BCTs.
 * 
 */
package Compiler;

import AST.*;
import BCT.*;


public class Transformer implements AST_Visitor{

	/**VISITOR HELPER
	 * 
	 * globalNode is used to rebuild the Tree during visiting.
	 */
	private BCT_Node globalNode;		
	
	/**GETBCT
	 * 
	 * @return root node of converted Tree
	 */
	public BCT_Node getBCT() {
		return globalNode;
	}
	
	//---------------------------------------------------------------------------------------------------
	//----------------------------------------------Visits-----------------------------------------------
	//---------------------------------------------------------------------------------------------------
	
	/**AST_APPLICATION (VISIT)
	 * 
	 * Applications own two children all the time. Furthermore Applications
	 * never store individual data, so that we just have to accept its children.
	 */
	@Override
	public void visit(AST_Application app) {		
		app.getLeftChild().accept(this);
		BCT_Node left = globalNode;
		app.getRightChild().accept(this);
		BCT_Node right = globalNode;
		globalNode = new BCT_Application(left, right);
	}

	/**AST_BUILTIN (VISIT)
	 * 
	 * Visiting AST_BuiltIns, we have to copy it 
	 * into BCT_BuiltIns.
	 */
	@Override
	public void visit(AST_BuiltIn node) {
		globalNode = new BCT_BuiltIn(node.toString());
	}

	/**AST_IDENTIFIER (VISIT)
	 * 
	 * Visiting AST_Identifier, we have to copy it 
	 * into BCT_Variable.
	 */
	@Override
	public void visit(AST_Identifier node) {
		globalNode = new BCT_Variable(node.getValue());
	}

	/**AST_BOOL (VISIT)
	 * 
	 * Visiting AST_Bools, we have to copy it 
	 * into BCT_ConstantBools.
	 */
	@Override
	public void visit(AST_Bool node) {
		globalNode = new BCT_ConstantBool(node.getValue());
	}

	/**AST_NUM (VISIT)
	 * 
	 * Visiting AST_Nums, we have to copy it 
	 * into BCT_ConstantNums.
	 */
	@Override
	public void visit(AST_Num node) {
		globalNode = new BCT_ConstantNum(node.getValue());
	}

	/**AST_STRING (VISIT)
	 * 
	 * Visiting AST_Strings, we have to copy it 
	 * into BCT_ConstantStrings.
	 */
	@Override
	public void visit(AST_String node) {
		globalNode = new BCT_ConstantString(node.getValue());
	}

	/**AST_LEAF (VISIT)
	 * 
	 * We do not accept Leafs, because all subclasses of AST_Leaf are
	 * accepted individually.
	 */
	@Override
	public void visit(AST_Leaf leaf) {}	//Not in use here

	/**AST_ABSTRACTION (VISIT)
	 * 
	 * Its not possible to catch AST_Abstractions here, because they are already
	 * substituted away.
	 */
	@Override
	public void visit(AST_Abstraction abstr) {}	//There is no Abstraction to Convert anymore

	/**AST_ARGUMENT (VISIT)
	 * 
	 * Its not possible to catch AST_Arguments here, because they are already
	 * substituted away.
	 */
	@Override
	public void visit(AST_Argument args) {} //There is no ArgumentNode to Convert anymore

	/**AST_SEMICOLON (VISI)
	 * 
	 * Its not possible to catch AST_Semicolons here, because they are already
	 * substituted away.
	 */
	@Override
	public void visit(AST_Semicolon semi) {}	//There is no Semicolon to Convert anymore

	/**AST_WHERE (VISIT)
	 * 
	 * Its not possible to catch AST_Wheres here, because they are already
	 * substituted away.
	 */
	@Override
	public void visit(AST_Where where) {}		//There is no Where to Convert anymore
	
}
