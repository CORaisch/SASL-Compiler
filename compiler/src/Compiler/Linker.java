/**LINKER (VISITOR)
 * 
 * Visitor used during Compilation Phase.
 * Its task is to replace left function calls
 * with their compiled representation.
 * 
 */
package Compiler;


import java.util.HashMap;
import BCT.*;

public class Linker implements BCT_Visitor {

	/**VISITOR HELPER
	 * 
	 * globalNode is used to rebuild the Tree during visiting.
	 */
	private BCT_Node globalNode; 
	
	/**GLOBAL DATA
	 * 
	 * compList contains compiled Data. It's used to link function calls to their compiled Representation stored in 'compList'.
	 */
	private HashMap<String, BCT> compList;
	
	/**INIT
	 * 
	 * Sets up 'compList', so that linker is ready to work.
	 * 
	 * @param compList contains Compiled Data
	 */
	public void init(HashMap<String, BCT> compList) {		
		this.compList = compList;		
	}
	
	/**GETBCT
	 * 
	 * @return root node of linked Tree
	 */
	public BCT_Node getBCT() {
		return globalNode;		
	}
	
	//---------------------------------------------------------------------------------------------------
	//----------------------------------------------Visits-----------------------------------------------
	//---------------------------------------------------------------------------------------------------
	
	/**BCT_APPLICATION (VISIT)
	 * 
	 * Applications all the time own two children. Furthermore Applications
	 * never store individual data, so that we just have to accept its children. 
	 */	
	@Override
	public void visit(BCT_Application node) {
		node.getLeftChild().accept(this);
		node.setLeftChild(globalNode);
		node.getRightChild().accept(this);
		node.setRightChild(globalNode);
		globalNode = node;
	}
	
	/**BCT_BUILTIN (VISIT)
	 * 
	 * Linker is just replacing function calls. So we just have to copy BuiltIns.
	 */
	@Override
	public void visit(BCT_BuiltIn node) {
		globalNode = node;
	}

	/**BCT_CONSTANTNUM (VISIT)
	 * 
	 * Linker is just replacing function calls. So we just have to copy Numerics.
	 */
	@Override
	public void visit(BCT_ConstantNum node) {
		globalNode = node;		
	}

	/**BCT_CONSTANTSTRING (VISIT)
	 * 
	 * Linker is just replacing function calls. So we just have to copy Strings.
	 */
	@Override
	public void visit(BCT_ConstantString node) {
		globalNode = node;		
	}

	/**BCT_CONSTANTBOOL (VISIT)
	 * 
	 * Linker is just replacing function calls. So we just have to copy Bools.
	 */
	@Override
	public void visit(BCT_ConstantBool node) {
		globalNode = node;		
	}

	/**BCT_VARIABLE (VISIT)
	 * 
	 * All Variables left in Tree have to be function calls, so we have to check for
	 * its definition in 'compList' and set Link to compiled representation of 
	 * called function. 
	 */
	@Override
	public void visit(BCT_Variable node) {
		if(this.compList.containsKey(node.toString()))					
			globalNode = compList.get(node.toString()).getRoot();
		else {
			System.err.println("Linker: Undefined Variable "+node.toString()+".");
			System.exit(1);
		}
	}
	
	/**BCT_PAIR (VISIT)
	 * 
	 * Its impossible to catch Pair Node during Compilation Phase. 
	 * BCT_Pair nodes are exclusively build during reduction Phase. 
	 */
	@Override
	public void visit(BCT_Pair node) {
		System.err.println("Found Pair during Compilation Phase Ouch!");
		System.exit(1);
	}
}
