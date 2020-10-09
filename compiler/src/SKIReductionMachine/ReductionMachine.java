package SKIReductionMachine;

import java.util.Stack;

import BCT.*;

public class ReductionMachine {
	
	/*
	 * leftAncestorStack
	 */
	private Stack<BCT_Node> leftAncestorStack;
	
	/*
	 * Constructors
	 */
	public ReductionMachine(BCT compiledProgramme) {
		leftAncestorStack = new Stack<BCT_Node>();
		leftAncestorStack.push(compiledProgramme.getRoot());
	}
	
	public ReductionMachine(BCT_Node compiledProgramme) {
		leftAncestorStack = new Stack<BCT_Node>();
		leftAncestorStack.push(compiledProgramme);
	}
	
	/*
	 * Main-Routine: 
	 * - Der Visitor fuehrt bei grossen Programmen wie zB unsere Primzahlprogramm zu einem StackOverflowError, da die 
	 *   Rekursionstiefe ueberschritten wird (meine Theorie). 
	 *   Hier werde ich deshalb mal den Visitor entfernen und eine iterative Variante der reduce() Funktion implementieren.
	 */
	public void reduce() {
		//leftAncestorStack.peek().accept(this);
		
		//Iterate as long as there is no Constant
		while(true) {
			//Get Current Node
			BCT_Node currentNode = leftAncestorStack.peek();
			//Check for Node -Type
			if(currentNode instanceof BCT_Application) {
				leftAncestorStack.push(currentNode.getLeftChild());
			} else if(currentNode.getNodeType() == BCT_NodeType.NIL) {
				break;	//BUG ?
			} else if(currentNode instanceof BCT_BuiltIn) {
				handleBuiltIn();
			} else {				
				break;
			}
		}
	}		
	
	/*
	 *Returns Result of Reduction 
	 */
	public BCT_Node getResult() {
		return leftAncestorStack.peek();
	}
	
	public void printResult() {		
		if (getResult() instanceof BCT_Pair){
			System.out.print("[");
			while(getResult() instanceof BCT_Pair) {
			
				BCT_Node x = leftAncestorStack.pop();
								
				leftAncestorStack.push(x.getLeftChild());
				reduce();
				printResult();
				
				leftAncestorStack.push(x.getRightChild());
				reduce();
				
				if(getResult().getNodeType() == BCT_NodeType.NIL) { 
					System.out.print("]");
					break;								
				} else {
					System.out.print(", ");
				}								
			}
		} else{
			System.out.print(getResult());
		}
	}
	
	//--------------------------------------------------------------------
	//---------------Helpers----------------------------------------------
	//--------------------------------------------------------------------
	

	private void handleBuiltIn() {
		//Pop top of Stack to get Builtin
		BCT_Node builtIn = leftAncestorStack.pop();
		
		//Switch for Node-Type
		switch(builtIn.getNodeType()) {
		case S		:	handleS();
						break;
		case K		:	handleK();
						break;				
		case I	 	:	handleI();
						break;
		case Y		:   handleY();
						break;
		case U		:   handleU();
						break;
		case B		:	handleB();
						break;
		case C		:	handleC();
						break;
		case S_		:	handleS_();
						break;
		case B_		:	handleB_();
						break;
		case C_		:	handleC_();
						break;
		case PLUS 	:	
		case MINUS	:	
		case MUL	:	
		case DIV	:				
		case EQ		:	
		case NEQ	:
		case LT		:
		case LEQ	:
		case GT		:
		case GEQ	:	
		case AND	:
		case OR		:
						handleBinaryOperation(builtIn.getNodeType());
						break;
		case COND	:	handleConditionOperation();
						break;
		case NOT	:	handleUnaryOperation(builtIn.getNodeType());
						break;
		case COLON  :   handleListOperator(); 
						break;
		case HD		:	handleHD();
						break;
		case TL		:	handleTL();
						break;						
		default:
			System.out.println("Caught "+builtIn.toString()+": Reduction functionality is not implemented yet.");
			System.exit(1);
		}
	}
	
	private void handleC_() {
		BCT_Node c = leftAncestorStack.pop().getRightChild();
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		
		//c @ (f @ x) @ g
		BCT_Application tempApp = new BCT_Application(c, new BCT_Application(f, x));
		top.setLeftChild(tempApp);
		top.setRightChild(g);
	}

	private void handleB_() {
		BCT_Node c = leftAncestorStack.pop().getRightChild();
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		
		//c @ (f @ (g @ x))
		BCT_Application tempApp = new BCT_Application(f, new BCT_Application(g, x));
		top.setLeftChild(c);
		top.setRightChild(tempApp);
	}

	private void handleS_() {
		BCT_Node c = leftAncestorStack.pop().getRightChild();
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		
		//c @ (f @ x) @ (g @ x)
		BCT_Application tempApp = new BCT_Application(c, new BCT_Application(f, x));
		top.setLeftChild(tempApp);
		top.setRightChild(new BCT_Application(g, x));
	}

	private void handleC() {
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		
		//f @ x @ g
		top.setLeftChild(new BCT_Application(f, x));
		top.setRightChild(g);
	}

	private void handleB() {
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		
		//f @ (g @ x)
		top.setLeftChild(f);
		top.setRightChild(new BCT_Application(g, x));
	}

	private void handleHD() {
		BCT_Node x = leftAncestorStack.peek();
		leftAncestorStack.push(x.getRightChild());
		reduce();
		BCT_Node tempResult = leftAncestorStack.pop();
		if(tempResult instanceof BCT_Pair){
			x.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			x.setRightChild(tempResult.getLeftChild());							
		} else {
			System.err.println("HD used on nonlist value");
			System.exit(1);
		}
	}
	
	private void handleTL() {
		BCT_Node x2 = leftAncestorStack.peek();
		leftAncestorStack.push(x2.getRightChild());
		reduce();
		BCT_Node tempResult2 = leftAncestorStack.pop();
		if(tempResult2 instanceof BCT_Pair){
			x2.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			x2.setRightChild(tempResult2.getRightChild());							
		} else {
			System.err.println("TL used on nonlist value");
			System.exit(1);
		}
	}
	
	private void handleS() {
		//Get Functions
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node g = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node x = top.getRightChild();
		//Build: (f @ x) @ (g @ x) 		
		BCT_Application gxApp = new BCT_Application(g, x);
		top.setRightChild(gxApp);
		BCT_Application fxApp = new BCT_Application(f, x);
		top.setLeftChild(fxApp);
	}
	
	private void handleK() {		
		//Get Functions
		BCT_Node x = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		//Build: I @ x
		top.setRightChild(x);
		top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
	}
	
	private void handleI() {
		//Get Function
		BCT_Node x = leftAncestorStack.pop().getRightChild();
		//Push Pointer to x
		leftAncestorStack.push(x);
	}
	
	private void handleY() {
		BCT_Node top = leftAncestorStack.peek();		
		top.setLeftChild(top.getRightChild());
		top.setRightChild(top);
	}
	
	private void handleU() {
		BCT_Node f = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node z = top.getRightChild();
		
		//Build: hd @ z
		BCT_Node hdNode = new BCT_Application(new BCT_BuiltIn(BCT_NodeType.HD), z);
		//Build: f @ hdNode
		BCT_Node leftNode = new BCT_Application(f, hdNode);
		//Append to top
		top.setLeftChild(leftNode);
		//Build: tl @ z
		BCT_Node rightNode = new BCT_Application(new BCT_BuiltIn(BCT_NodeType.TL), z);
		//Append to top
		top.setRightChild(rightNode);
	}

	private void handleBinaryOperation(BCT_NodeType opIndicator) {
		BCT_Node x = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node y = top.getRightChild();
		leftAncestorStack.push(x);
		reduce();
		BCT_Node resultX = leftAncestorStack.pop();
		leftAncestorStack.push(y);
		reduce();
		BCT_Node resultY = leftAncestorStack.pop();
		if((resultX instanceof BCT_ConstantNum) && (resultY instanceof BCT_ConstantNum)) {
			//Build: I @ (x.y)
			int iX = ((BCT_ConstantNum)resultX).getValue();
			int iY = ((BCT_ConstantNum)resultY).getValue();
			top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			switch(opIndicator) {
			
			case PLUS:
					top.setRightChild(new BCT_ConstantNum(iX+iY));
					break;
			case MINUS:					
					top.setRightChild(new BCT_ConstantNum(iX-iY));
					break;
			case MUL:
					top.setRightChild(new BCT_ConstantNum(iX*iY));
					break;
			case DIV:					
					if(iY != 0) 
						top.setRightChild(new BCT_ConstantNum(iX/iY));
					else {
						System.err.println("Mathematical Error: Divide by Zero.");
						System.exit(1);
					}
					break;
			case EQ:
					top.setRightChild(new BCT_ConstantBool(iX == iY));
					break;
			case NEQ:
					top.setRightChild(new BCT_ConstantBool(iX != iY));
					break;
			case LT:
					top.setRightChild(new BCT_ConstantBool(iX < iY));
					break;
			case LEQ:
					top.setRightChild(new BCT_ConstantBool(iX <= iY));
					break;
			case GT:
					top.setRightChild(new BCT_ConstantBool(iX > iY));
					break;
			case GEQ:
					top.setRightChild(new BCT_ConstantBool(iX >= iY));
					break;
			default:
					System.out.println("We never should be here (Reductionmachine: handleArithmetic)!!");
			}
		} else if((resultX instanceof BCT_ConstantBool) && (resultY instanceof BCT_ConstantBool)) {
			//handle Bool Comparisons: '=', '!='
			boolean bX = ((BCT_ConstantBool)resultX).getValue();
			boolean bY = ((BCT_ConstantBool)resultY).getValue();
			top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			switch(opIndicator) {
			case EQ : 
				top.setRightChild(new BCT_ConstantBool(bX == bY));
				break;
			case NEQ:
				top.setRightChild(new BCT_ConstantBool(bX != bY));
				break;
			case OR :
				top.setRightChild(new BCT_ConstantBool(bX || bY));
				break;
			case AND:
				top.setRightChild(new BCT_ConstantBool(bX && bY));
				break;
			default:
				System.err.println("Semantic Error: Applied Compareoperation on invalid Types.");				
			}
		} else if(resultX.getNodeType() == BCT_NodeType.NIL || resultY.getNodeType() == BCT_NodeType.NIL){
			//handle List Comparison: '=', '!=' with 'nil'
			top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			switch(opIndicator) {
			case EQ :
				if((resultX.getNodeType() == BCT_NodeType.NIL) && (resultY.getNodeType() == BCT_NodeType.NIL)) 
					top.setRightChild(new BCT_ConstantBool(true));
				else
					top.setRightChild(new BCT_ConstantBool(false));
				break;
			case NEQ:
				if((resultX.getNodeType() == BCT_NodeType.NIL) && (resultY.getNodeType() == BCT_NodeType.NIL)) 
					top.setRightChild(new BCT_ConstantBool(false));
				else
					top.setRightChild(new BCT_ConstantBool(true));
				break;
			default:
				System.err.println("Semantic Error: Undefined Compareoperation on Lists.");
				System.exit(1);
			}													
		} else {
			System.err.println("Semantic Error occurs: Invalid Operation.");
			System.exit(1);
		}
	}
	
	private void handleConditionOperation() {
		BCT_Node condition = leftAncestorStack.pop().getRightChild();			
		leftAncestorStack.push(condition);
		reduce();
		BCT_Node conditionValue = leftAncestorStack.pop();
		
		if(conditionValue instanceof BCT_ConstantBool){
			BCT_Node x = leftAncestorStack.pop().getRightChild();
			BCT_Node top = leftAncestorStack.peek();
			BCT_Node y = top.getRightChild();
			
			top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
			
			if(((BCT_ConstantBool)conditionValue).getValue())
				top.setRightChild(x);
			else 
				top.setRightChild(y);			
		} else {
			System.err.println("Semantic Error: Boolean Expression expected in If-Term.");
			System.exit(1);
		}
	}
	
	private void handleUnaryOperation(BCT_NodeType junctorType) {
			BCT_Node top = leftAncestorStack.peek();
			BCT_Node x = top.getRightChild();			
			leftAncestorStack.push(x);
			reduce();
			BCT_Node resultX = leftAncestorStack.pop();
			
			if (resultX instanceof BCT_ConstantBool){
				boolean valResultX = ((BCT_ConstantBool)resultX).getValue();				
				boolean result = !valResultX;				
				top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
				top.setRightChild(new BCT_ConstantBool(result));
			} else {
				System.err.println("Semantic Error: Boolean Expression expected behind 'not'.");
				System.exit(1);
			}
	}
	
	private void handleListOperator() {
		BCT_Node x = leftAncestorStack.pop().getRightChild();
		BCT_Node top = leftAncestorStack.peek();
		BCT_Node y = top.getRightChild();
		//Build I @ (x pair y)
		BCT_Pair pair = new BCT_Pair(x, y);
		top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.I));
		top.setRightChild(pair);		
	}	
}
