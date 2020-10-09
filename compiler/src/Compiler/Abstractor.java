/**ABSTRACTOR (VISITOR)
 * 
 * Visitor used during compilation Phase.
 * Its task is to transform parsed Representation of source code
 * into reducible SKIYU-Representation.  
 * 
 */
package Compiler;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

import AST.*;

public class Abstractor implements AST_Visitor {
	
	/**VISITOR HELPER
	 * 
	 * globalNode is used to rebuild the Tree during visiting.
	 */
	private AST_Node globalNode;
	
	/**GLOBAL HELPERS
	 * 
	 * varToAbstract		  : Stores the current Variable to Abstract. 
	 * whereDetected		  : Indicates the existence of local Definitions.
	 * localRecursionDetected : Indicates the existence of recursion within local Definitions.
	 * DEFAULT				  : Used to indicate Abstraction of local Definitions only.
	 */
	private String varToAbstract;
	private boolean whereDetected;
	private boolean localRecursionDetected;
	private String DEFAULT = "";
	
	
	/**CONSTRUCTOR
	 * 
	 * Initializes global helpers.
	 */
	public Abstractor() {
		whereDetected = false;
		localRecursionDetected = false;
		varToAbstract = DEFAULT;
		globalNode = null;
	}
	
	//-----------------------------------------------------------------------------------------------------
	//------------------------------Helper-Functions-------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------
	
	
	/**RESET 
	 * 
	 * Resets global Helpers to default Value.
	 */
	public void reset() {
		varToAbstract = DEFAULT;
		whereDetected = false;
		localRecursionDetected = false;
		globalNode = null;
	}		
	
	/**GETVARTOABSTRACT
	 * 
	 * Used to synchronize, when using multiple Abstractors. 
	 * 
	 * @return current 'varToAbstract'.
	 */
	public String getVarToAbstract() {
		return varToAbstract;
	}
	
	/**SETVARTOABSTRACT
	 * 
	 * Used to synchronize, when using multiple Abstractors. 
	 * 
	 * @param var which is set as new 'varToAbstract'.
	 */
	public void setVarToAbstract(String var) {
		this.varToAbstract = var;
	}

	/**GETAST
	 * 
	 * @return 'globalNode', which is root of Abstracted Tree.
	 */
	public AST_Node getAST() {
		return globalNode;
	}
	
	/**CHECKFORLOCALDEF
	 * 
	 * Wrapper Function for safer use of 'whereDetected'.
	 * It invokes 'containsWhere', which checks for local Definitions.
	 * 
	 * @param subRoot where search for 'Where' Node starts
	 * @return true if there are local Definitions else false.
	 */
	public boolean checkForLocalDef(AST_Node subRoot) {
		whereDetected = false;
		containsWhere(subRoot);
		return whereDetected;
	}
	/**CONTAINSWHERE 
	 * 
	 * Traverse Subtree since a 'Where' Node is caught.
	 * If there is a 'Where' Node 'whereDetected' is set to true.
	 * 
	 * @param node where search for 'Where' Node starts.
	 */
	private void containsWhere(AST_Node node) {		
		if(node instanceof AST_Where)
			whereDetected = true;
		else if(node == null)
			;
		else {
			containsWhere(node.getLeftChild());
			containsWhere(node.getRightChild());
		}
	}	
		
	/**CHECKFORRECURSION
	 * 
	 * Traverse Tree and Check for Occurrence of varToAbstract.
	 * Used to search for local Recursion in local Definitions with
	 * just ONE Definition.
	 * 'localRecursionDetecte' is set true if 'varToAbstract' exists in local Function.
	 * Otherwise its set to false.
	 * 
	 * @param node where search for 'varToAbstract' starts.  
	 */
	private void checkForRecursion(AST_Node node) {
		if(node == null)
			;
		else if(node.toString().equals(varToAbstract))
			localRecursionDetected = true;			
		else {
			checkForRecursion(node.getLeftChild());
			checkForRecursion(node.getRightChild());			
		}
		
	}	

	/**CHECKRECURSIONINMULTIPLEDEFINITIONS
	 * 
	 * Traverse Tree and Check for Occurrence of stack entries.
	 * Used to search for local Recursion in local Definitions with
	 * MULTIPLE Definitions.
	 * 'localRecursionDetecte' is set true if a Variable is caught that contains in stack.
	 * Otherwise its set to false.
	 * 
	 * @param node where search for local Recursion starts.
	 * @param stack which contains the Function Names, used to identify local Recursion
	 */
	private void checkRecursionInMultipleDefinitions(AST_Node node, Stack<String> stack) {
		if(node instanceof AST_Semicolon) {
			checkRecursionInMultipleDefinitions(node.getLeftChild(), stack);
			checkRecursionInMultipleDefinitions(node.getRightChild(), stack);
		} else if(node instanceof AST_Application) {
			checkRecursionInMultipleDefinitions(node.getLeftChild(), stack);
			checkRecursionInMultipleDefinitions(node.getRightChild(), stack);
		} else if(node instanceof AST_Abstraction) {
			checkRecursionInMultipleDefinitions(node.getRightChild(), stack);			
		} else {
			if(stack.contains(node.toString())) {
				localRecursionDetected = true;
			}
		}		
	}
	
	/**HANDLESINGLELOCALDEFINITION
	 * 
	 * Function to handle the U-Transformation on 
	 * local Definitions with just ONE function.
	 * 
	 * Algorithm:
	 * - First accept right Child (Always AST_Abstraction in correct Code) to abstract local Definition ('DEF').
	 * - Then accept Body (left Child of Where) to abstract local Function name (which is in 'varToAbstract' now) form Body ('BODY').
	 * - Then check for local Recursion using the checkForRecursion-Method defined above.
	 * 		- If there is local Recursion we have to abstract the function off itself and Build: [f]BODY @ ([f]([x]DEF))
	 * 		- Else Build: [f]BODY @ [x]DEF
	 * 			
	 * @param where: Node currently visited.
	 */
	private void handleSingleLocalDefiniton(AST_Where where) {
		//ArrayList<AST_node> backupStack = AST_stack;
		//AST_stack.clear();
		Abstractor abst = new Abstractor();
		where.getRightChild().accept(abst);
		where.getLeftChild().accept(abst);
		this.varToAbstract = abst.getVarToAbstract();
		
		//Check for occurrences of varToAbstract in Where.getRightChild().getRightChild()
		checkForRecursion(where.getRightChild().getRightChild());
		if(localRecursionDetected) {
			//If there is local Recursion
			localRecursionDetected = false; //Safety First		
			//Create local Abstractor 
			Abstractor localAbst = new Abstractor();
			localAbst.setVarToAbstract(varToAbstract);
			//accept where.getRightChild().getRightChild() with local Abstractor				
			where.getRightChild().getRightChild().accept(localAbst);
			//Build: tempTree = Y @ (result of local Abstractor)
			AST_Application tempApp = new AST_Application(new AST_BuiltIn(AST_NodeType.Y), localAbst.getAST());
			//where.getRightChild().setRightChild() = tempTree 
			where.getRightChild().setRightChild(tempApp);
			//Delete local Abstractor
			localAbst = null;
		}
		
		//Build: ([f]E1) @ ([x]E2)
		where.setLeftChild(new AST_Application(abst.getAST(), where.getRightChild().getRightChild()));		
	}
	
	/**HANDLEMULTIPLELOCALDEFINITIONS
	 * 
	 * Function to handle the U-Transformation on 
	 * local Definitions with MULTIPLE functions. 
	 * 
	 * Algorithm:
	 * - First we abstract all local Definitions and store them in 'backupDefinitons': [x0]E0, ..., [xn]En .
	 * - Then we build a list out of abstracted Definitions: [[x0]E0, ..., [xn]En] .
	 * - Then we have to abstract local Function names from BODY (right Child of Where) and Build: U @ ([f](U @ [g](K @ E1))) .
	 * - Now we have to check if there is recursion inside local Definitions
	 * 		- If there is no recursion we just apply both results like this: U @ ([f](U @ [g](K @ E1))) @ [[x]E2, [y]E3]
	 * 		- If there is recursion we have to abstract local Function names from themselves,build Y-U-Transformation and apply this to other result:
	 * 		  Y @ (U @ ([f0](U @ [f1](...[fi](K @ [[x0]E0, [x1]E1, ..., [xi]Ei]))))) -> U @ ([f](U @ [g](K @ E1))) @ (Y @ (U @ ([f0](U @ [f1](...[fi](K @ [[x0]E0, [x1]E1, ..., [xi]Ei]))))))  
	 * 
	 * @param where: Node currently visited.  
	 */
	private void handleMultipleLocalDefinitions(AST_Where where) {				
		//HELPER DECLARATIONS
		ArrayList<AST_Node> backupDefinitions = new ArrayList<AST_Node>();
		Stack<String> abstractVarStack = new Stack<String>();
		Stack<String> abstractVarStackBackup = new Stack<String>();
		Abstractor abst = new Abstractor();
		AST_Node listNode;
		//end HELPER DECLARATIONS
		
		/*Abstract local Definitions*/
		
		//Iterate right Child while right Child is Semicolon
		for(AST_Node i = where.getRightChild(); i instanceof AST_Semicolon; i = i.getRightChild()) {
			//Accept left Child			
			i.getLeftChild().accept(abst);
			//Backup Result in Array (currentNode.leftChild.rightChild)
			backupDefinitions.add(i.getLeftChild().getRightChild());
			//Backup varToAbastract in Stack
			abstractVarStack.push(abst.getVarToAbstract());
			abstractVarStackBackup.push(abst.getVarToAbstract());
			abst.reset();
			//If right Child of current Semicolon is not Semicolon than accept right Child
			if(!(i.getRightChild() instanceof AST_Semicolon)) {
				//Accept currentNode.rightChild
				i.getRightChild().accept(abst);
				//Backup Result in Array (currentNode.rightChild.rightChild)
				backupDefinitions.add(i.getRightChild().getRightChild());
				//Backup varToAbstract in Stack
				abstractVarStack.push(abst.getVarToAbstract());
				abstractVarStackBackup.push(abst.getVarToAbstract());
				abst.reset();
			}
		}					
		
		/*** Invariant: [x]E2, [y]E3,... | backUpDefinitions := [x]Ei | abstractVarStack := g,f,..(Function names in upside down order) ***/
		
		/*Build List out of Backup Array. Build: [[x]E2, [y]E3]*/
		
		Stack<AST_Node> stackOfListElements = new Stack<AST_Node>();
		//Iterate Backup Array		
		Iterator<AST_Node> iter = backupDefinitions.iterator();
		while(iter.hasNext()) {
			//Build: ':' @ (currentElement) and push to Stack
			stackOfListElements.push(new AST_Application(new AST_BuiltIn(AST_NodeType.COLON), iter.next()));
		}		
		//Build: (Stack.pop) @ nil and store in temporary Node
		listNode = new AST_Application(stackOfListElements.pop(), new AST_BuiltIn(AST_NodeType.NIL));				
		//Iterate since Stack is empty
		while(!stackOfListElements.isEmpty()) {
			//Build (Stack.pop) @ (temporary Node)
			listNode = new AST_Application(stackOfListElements.pop(), listNode);
		}	
		
		/*** Invariant: [[x0]E0, [x1]E1, ..., [xi]Ei] -> listNode | Content of backupDefinitions is not changed ***/
		
		/*Abstract local function calls from left Child. Build: U @ ([f](U @ [g](K @ E1)))*/		
		
		//Build: K @ (where.leftChild) and set as new left Child of Where
		where.setLeftChild(new AST_Application(new AST_BuiltIn(AST_NodeType.K), where.getLeftChild()));		
		//Pop varToAbstractBackup Stack since its empty
		abst.reset(); //saftey first
		while(!abstractVarStackBackup.isEmpty()) {
			//varToAbstract = Stack.pop
			abst.setVarToAbstract(abstractVarStackBackup.pop());
			//accept where.leftChild
			where.getLeftChild().accept(abst);
			//Build U @ (result of accepted left Child) and set as new left Child of Where
			where.setLeftChild(new AST_Application(new AST_BuiltIn(AST_NodeType.U), abst.getAST()));
			abst.reset();
		}			
		
		/*** Invariant: Body abstracted := U @ ([f0](U @ [f1](...[fi](K @ E_Body)))) -> where.leftChild ***/ 		
		
		//Check for Recursion in local Definitions
		localRecursionDetected = false; //Saftey First
		checkRecursionInMultipleDefinitions(where.getRightChild(), abstractVarStack);
		if(!localRecursionDetected) {
			//IF there is no Recursion: Build normal U-Transformation		
			localRecursionDetected = false; //reset local Recursion Indicator
			
			/*Build final Form: U @ ([f](U @ [g](K @ E1))) @ [[x]E2, [y]E3]*/
			
			//Build (where.leftChild) @ (listNode) and set as new Left Child of Where	
			where.setLeftChild(new AST_Application(where.getLeftChild(), listNode));
		
		} else {
			//IF there is Recursion then do Y-U-Transformation
			localRecursionDetected = false; //reset local Recursion Indicator
			
			//Build K @ [[x0]E0, [x1]E1, ..., [xi]Ei] and set as new right child of where
			where.setRightChild(new AST_Application(new AST_BuiltIn(AST_NodeType.K), listNode));
			abst.reset();
			//Pop varToAbstract Stack since its empty
			while(!abstractVarStack.isEmpty()) {
				//varToAbstract = stack.pop
				abst.setVarToAbstract(abstractVarStack.pop());
				//accept where.rightChild
				where.getRightChild().accept(abst);
				//Build U @ (result of accepted right Child) and set as new right Child of Where
				where.setRightChild(new AST_Application(new AST_BuiltIn(AST_NodeType.U), abst.getAST()));
				abst.reset();
			}
			
			/*** Invariant: expr := U @ ([f0](U @ [f1](...[fi](K @ [[x0]E0, [x1]E1, ..., [xi]Ei])))) ***/
			
			//Build Y @ expr and set as new right child of where
			where.setRightChild(new AST_Application(new AST_BuiltIn(AST_NodeType.Y), where.getRightChild()));
			//Build (where.leftChild) @ (where.rightChild) and set as new left child of where 
			where.setLeftChild(new AST_Application(where.getLeftChild(), where.getRightChild()));
		}
	}
	
	//--------------------------------------------------------------------------------------------	
	//-----------------------------AST-Visits-----------------------------------------------------
	//--------------------------------------------------------------------------------------------
	
	/**AST_ABSTRACTION (VISIT)
	 * 
	 * When on Abstraction, we have to abstract local Variables (always on
	 * left Side) from the Definitions Body (always on right side).
	 * 
	 * Algorithm:
	 * - Iterate through left children of Abstraction
	 * 		- If there is an AST_Argument 
	 * 			- Set its right Child as 'varToAbstract'
	 * 			- accept right Child to abstract Variable form Body
	 * 		- If there is not an AST_Argument, there must be an Identifier which indicates the function name
	 * 			- set function name as 'varToAbstract' and return 
	 * - If there are no Arguments with the function, we also have to check for local Definitions and accept Body if there is 
	 */
	@Override
	public void visit(AST_Abstraction abstr) {

		//Iterate over left Subtree
		for(AST_Node subNode = abstr.getLeftChild(); subNode != null ; subNode = subNode.getLeftChild()) {
			if(subNode instanceof AST_Argument) {
				//On Argument Node set varToAbstract to right child
				varToAbstract = subNode.getRightChild().toString();
				//and accept Abstractions' Definition
				abstr.getRightChild().accept(this);							
				//Build Tree and append as Abstractions' Left Child
				abstr.setRightChild(getAST());
			} else {
				//Else we have to be on the Functionname Identifier
				//If we are on local Definition we have to Abstract Functionname from Body so
				//set varToAbstract to functionname
				if(!(abstr.getLeftChild() instanceof AST_Argument)) {
					//There are no Arguments with the Function
					//Check if there are local Definitions
					if(checkForLocalDef(abstr.getRightChild())) {
						//If yes, then abstract RightChild
						varToAbstract = DEFAULT;
						abstr.getRightChild().accept(this);
						abstr.setRightChild(getAST());
					}
				} 
				varToAbstract = subNode.toString();
			}
		}												
	}	

	/**AST_WHERE (VISIT)
	 * 
	 * When on Where, we have to Accept its local Definitions and abstract them from Wheres Body.
	 * Note:
	 *  - If there is AST_Semicolon on right side of Where there are multiple Definitions, if not there is just one
	 *  - If backed up 'varToAbstract' equals 'DEFAULT' we don't have to abstract it from wheres Body. 
	 *    (DEFAULT indicates that there are no global Arguments within the function we are currently Abstracting)
	 *  - After Where is handled the Where Node itself is deleted.
	 */
	@Override
	public void visit(AST_Where where) {
		//Backup varToAbstract
		String tempBackup = this.varToAbstract;
		//Check if Right Child of Where is Semicolon
		if(where.getRightChild() instanceof AST_Semicolon)
			handleMultipleLocalDefinitions(where);											
		else 
			handleSingleLocalDefiniton(where);		
		
		this.varToAbstract = tempBackup;
		
		if(!this.varToAbstract.equals(DEFAULT))
			where.getLeftChild().accept(this);
		else 
			globalNode = where.getLeftChild();
	}		

	/**AST_APPLICATION (VISIT)
	 * 
	 * When on AST_Application ('@'), we have to first accept left Child ('left') and right Child
	 * ('right') and then do S Transformation as follows:
	 * 	
	 * @ -> S @ left @ right 
	 */
	@Override
	public void visit(AST_Application app) {
		app.getLeftChild().accept(this);
		AST_Node left = globalNode;
		app.getRightChild().accept(this);
		AST_Node right = globalNode;
		AST_Application tempApp = new AST_Application(new AST_BuiltIn(AST_NodeType.S), left);
		globalNode = new AST_Application(tempApp, right);					
	}

	/**AST_BUILTIN (VISIT)
	 * 
	 * When on AST_BuiltIn ('BI') we just have to do K-Transformation as follows:
	 * 
	 * BI -> K @ BI
	 */
	@Override
	public void visit(AST_BuiltIn node) {
		globalNode = new AST_Application(new AST_BuiltIn(AST_NodeType.K), node);				
	}

	/**AST_IDENTIFIER (VISIT)
	 * 
	 * When on AST_Identifier ('id') we have to check if 'id' held a Variable we have to Abstract and therefore we have to do I-Transformation:
	 * id -> I
	 * Otherwise we have to do K-Transformation:
	 * id -> K @ id
	 */
	@Override
	public void visit(AST_Identifier node) {
		if(node.getValue().equals(varToAbstract)) 
			globalNode = new AST_BuiltIn(AST_NodeType.I);
		else
			globalNode = new AST_Application(new AST_BuiltIn(AST_NodeType.K), node);
	}

	/**AST_BOOL (VISIT)
	 * 
	 * When on AST_Bool ('BO') we just have to do K-Transformation as follows:
	 * 
	 * BO -> K @ BO
	 */
	@Override
	public void visit(AST_Bool node) {	
		globalNode = new AST_Application(new AST_BuiltIn(AST_NodeType.K), node);
	}

	/**AST_NUM (VISIT)
	 * 
	 * When on AST_Num ('NUM') we just have to do K-Transformation as follows:
	 * 
	 * NUM -> K @ NUM
	 */
	@Override
	public void visit(AST_Num node) {
		globalNode = new AST_Application(new AST_BuiltIn(AST_NodeType.K), node);
	}

	/**AST_STRING (VISIT)
	 * 
	 * When on AST_String ('STR') we just have to do K-Transformation as follows:
	 * 
	 * STR -> K @ STR
	 */
	@Override
	public void visit(AST_String node) {
		globalNode = new AST_Application(new AST_BuiltIn(AST_NodeType.K), node);
	}
	
	/**AST_SEMICOLON (VISIT)
	 * 
	 * We never accept AST_Semicolons, because it is handled within Where Nodes.
	 */
	@Override
	public void visit(AST_Semicolon semi) {} //Not in use here, its already treated by Where
	
	/**AST_LEAF (VISIT)
	 * 
	 * We do not accept AST_Leafs directly. We accept Subclasses individually.
	 */
	@Override
	public void visit(AST_Leaf leaf) {}	//Not in use here, we use derivations of Leaf

	/**AST_ARGUMENT (VISIT)
	 * 
	 * We never accept AST_Arguments, because it is handled within Abstraction Nodes. 
	 */
	@Override
	public void visit(AST_Argument args) {} //Not in use here, they are all abstracted away
	

}
