package Optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

import BCT.BCT;
import BCT.BCT_Application;
import BCT.BCT_BuiltIn;
import BCT.BCT_Node;
import BCT.BCT_NodeType;

/**
 * The Optimizer is an optional step of the compilation-process.
 * Some new combinators and additional rules help to speedup the
 * reduction-process.
 * 
 * @author David, Claudio
 */
public class Optimizer {
	
	/**
	 * optList stores all definitions to be optimized
	 * parentList stores the parent-node of every node
	 * leftAncestorStack is used during the optimization-process
	 * debugMessages defines whether messages are shown or not
	 */
	private HashMap<String, BCT> optList;
	private final HashMap<BCT_Node, BCT_Node> parentList;
	private final ArrayList<BCT_Node> visitedList;
	private final Stack<BCT_Node> leftAncestorStack;
	private final boolean debugMessages;
	private String defName;
	
	/**
	 * Constructor of an {@link Optimizer} initializes leftAncestorStack
	 * and sets optList
	 * 
	 * @param compList list of compiled definitions to be optimized
	 */
	public Optimizer(HashMap<String, BCT>compList, boolean debugMessages) {
		parentList = new HashMap<BCT_Node, BCT_Node>();
		leftAncestorStack = new Stack<BCT_Node>();
		visitedList = new ArrayList<BCT_Node>();
		optList = compList;
		defName = "";
		this.debugMessages = debugMessages;
	}

	/**
	 * optimize(int) is a public method used to start the optimization-process
	 * @param optimizationRounds the number of times a definition is run through
	 * the optimization-process
	 * @return HashMap of optimized definitions
	 */
	public HashMap<String, BCT> optimize(int optimizationRounds) {
		for(int i=0; i < optimizationRounds; i++){
			final Iterator<Entry<String, BCT>> optIter = optList.entrySet().iterator();
			while(optIter.hasNext()) {
				final Entry<String, BCT> compPair = optIter.next();
				defName = compPair.getKey();
				if(!defName.equals("00FuncCall00"))
					continue;
				leftAncestorStack.push(compPair.getValue().getRoot());
				
				optimize();
				
				visitedList.clear();
				leftAncestorStack.clear();
			}
		}
		
		return optList;				
	}	
	
	/**
	 * optimize() is used to handle the optimization-processes for one
	 * definition and one turn
	 */
	private void optimize() {
		while(true) {
			final BCT_Node currentNode = leftAncestorStack.peek();
			
			if(!visitedList.contains(currentNode)){
				if(currentNode instanceof BCT_Application) {
					parentList.put(currentNode.getLeftChild(), currentNode);
					parentList.put(currentNode.getRightChild(), currentNode);
					leftAncestorStack.push(currentNode.getLeftChild());
					
					visitedList.add(currentNode);
				} else if(currentNode.getNodeType() == BCT_NodeType.S) {
					leftAncestorStack.pop();
					if(leftAncestorStack.size() > 1)
						handleS();
					break;
				} else {
					 leftAncestorStack.pop();
					
					BCT_Node[] copy = new BCT_Node[leftAncestorStack.size()];
					leftAncestorStack.copyInto(copy);
					
					for(int i=0; i<copy.length; i++){
						BCT_Node nodeToDo = copy[i].getRightChild();
						
						if(nodeToDo.getNodeType() == BCT_NodeType.APPLICATION){
							optimize(nodeToDo);
						}
					}
					
					break;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * optimize(BCT_Node) is used to handle tree-traversal
	 * @param node
	 */
	private void optimize(BCT_Node node) {
		leftAncestorStack.clear();
		leftAncestorStack.push(node);
		optimize();
	}
	
	/**
	 * handleS() implements all optimization-rules. A rule can only apply
	 * if there is a S-node on the stack
	 */
	private void handleS() {
		/* Get Functions
		 * 
		 * If this method is called, then there is always a construct of
		 * following form on the stack:
		 * S @ x @ y
		 */
		parentList.put(leftAncestorStack.peek().getRightChild(), leftAncestorStack.peek());
		final BCT_Node x = leftAncestorStack.pop().getRightChild();
		final BCT_Node top = leftAncestorStack.peek();
		parentList.put(leftAncestorStack.peek().getRightChild(), leftAncestorStack.peek());
		final BCT_Node y = top.getRightChild();
		
		if(x instanceof BCT_Application){
			if(y instanceof BCT_Application){
				if(x.getLeftChild().getNodeType() == BCT_NodeType.K){
					if(y.getLeftChild().getNodeType() == BCT_NodeType.K){
						/* Rule:
						 * S @ (K @ f) @ (K @ g) --> K @ (f @ g)
						 * 
						 * x := K @ f
						 * y := K @ g
						 */
						if(debugMessages){
							System.out.println("S @ (K @ f) @ (K @ g) -> K @ (f @ g)");
						}
						
						final BCT_Node f = x.getRightChild();
						final BCT_Node g = y.getRightChild();
						
						/* Build K @ (f @ g) */
						top.setLeftChild(new BCT_BuiltIn(BCT_NodeType.K));
						top.setRightChild(new BCT_Application(f, g));
						
						/* Optimize f, g */
						parentList.put(f, top.getRightChild());
						optimize(f);
						parentList.put(g, top.getRightChild());
						optimize(g);
						
						return;
					} else if(y.getLeftChild() instanceof BCT_Application){
						if(y.getLeftChild().getLeftChild().getNodeType() == BCT_NodeType.B){
							/* Rule:
							 * S @ (K @ f) @ (B @ g @ h) --> B* @ f @ g @ h
							 * 
							 * x := K @ f
							 * y := B @ g @ h
							 */
							if(debugMessages){
								System.out.println("S @ (K @ f) @ (B @ g @ h) -> B* @ f @ g @ h");
							}
							
							final BCT_Node f = x.getRightChild();
							final BCT_Node g = y.getLeftChild().getRightChild();
							final BCT_Node h = y.getRightChild();
							
							/* Build B* @ f @ g @ h */
							top.setLeftChild(new BCT_Application(new BCT_Application(new BCT_BuiltIn(BCT_NodeType.B_), f), g));
							top.setRightChild(h);
							
							/* Optimize f, g, h */
							parentList.put(f, top.getLeftChild().getLeftChild());
							optimize(f);
							parentList.put(g, top.getLeftChild());
							optimize(g);
							parentList.put(h, top);
							optimize(h);
							
							return;
						}
					}
				}
				
				if(x.getLeftChild() instanceof BCT_Application && y.getLeftChild().getNodeType() == BCT_NodeType.K){
					if(x.getLeftChild().getLeftChild().getNodeType() == BCT_NodeType.B){
						/* Rule:
						 * S @ (B @ f @ g) @ (K @ h) --> C' @ f @ g @ h
						 * 
						 * x := B @ f @ g
						 * y := K @ h
						 */
						if(debugMessages){
							System.out.println("S @ (B @ f @ g) @ (K @ h) -> C' @ f @ g @ h");
						}
						
						final BCT_Node f = x.getLeftChild().getRightChild();
						final BCT_Node g = x.getRightChild();
						final BCT_Node h = y.getRightChild();
						
						/* Build C' @ f @ g @ h */
						top.setLeftChild(new BCT_Application(new BCT_Application(new BCT_BuiltIn(BCT_NodeType.C_), f), g));
						top.setRightChild(h);
						
						/* Optimize f, g, h */
						parentList.put(f, top.getLeftChild().getLeftChild());
						optimize(f);
						parentList.put(g, top.getLeftChild());
						optimize(g);
						parentList.put(h, top);
						optimize(h);
						
						return;
					}
				}
			}
			
			if(x.getLeftChild().getNodeType() == BCT_NodeType.K){
				if(y.getNodeType() == BCT_NodeType.I){
					/* Rule:
					 * S @ (K @ f) @ I --> f
					 * 
					 * x := K @ f
					 * y := I
					 */
					if(debugMessages){
						System.out.println("S @ (K @ f) @ I -> f");
					}
					
					final BCT_Node f = x.getRightChild();
					
					/* Build f */
					BCT_Node topParent = parentList.get(top);
					if(topParent != null){
						topParent.setRightChild(f);
						parentList.put(topParent.getRightChild(), topParent);
					} else {
						optList.get(defName).setRoot(f);
					}
					
					/* Optimize f */
					parentList.put(f, x);
					optimize(f);
					
					return;
				} else {
					/* Rule:
					 * S @ (K @ f) @ g --> B @ f @ g
					 * 
					 * x := K @ f
					 * y := g
					 */
					if(debugMessages){				
						System.out.println("S @ (K @ f) @ g -> B @ f @ g");
					}
					
					final BCT_Node f = x.getRightChild();
					final BCT_Node g = y;
					
					/* Build B @ f @ g */
					top.setLeftChild(new BCT_Application(new BCT_BuiltIn(BCT_NodeType.B), f));
					top.setRightChild(g);
					
					/* Optimize f, g */
					parentList.put(f, top.getLeftChild());
					optimize(f);
					parentList.put(g, top);
					optimize(g);
					return;
				}
			} else if(x.getLeftChild() instanceof BCT_Application){
				if(x.getLeftChild().getLeftChild().getNodeType() == BCT_NodeType.B){
					/* Rule:
					 * S @ (B @ f @ g) @ h --> S' @ f @ g @ h
					 * 
					 * x := B @ f @ g
					 * y := h
					 */
					if(debugMessages){
						System.out.println("S @ (B @ f @ g) @ h -> S' @ f @ g @ h");
					}
					
					final BCT_Node f = x.getLeftChild().getRightChild();
					final BCT_Node g = x.getRightChild();
					final BCT_Node h = y;
					
					/* Build S' @ f @ g @ h */
					top.setLeftChild(new BCT_Application(new BCT_Application(new BCT_BuiltIn(BCT_NodeType.S_), f), g));
					top.setRightChild(h);
					
					/* Optimize f, g, h */
					parentList.put(f, top.getLeftChild().getLeftChild());
					optimize(f);
					parentList.put(g, top.getLeftChild());
					optimize(g);
					parentList.put(h, top);
					optimize(h);
					
					return;
				}
			}
		}
		
		if(y instanceof BCT_Application){
			if(y.getLeftChild().getNodeType() == BCT_NodeType.K){
				/* Rule:
				 * S @ f @ (K @ g) --> C @ f @ g
				 * 
				 * x := f
				 * y := K @ g
				 */
				if(debugMessages){
					System.out.println("S @ f @ (K @ g) -> C @ f @ g");
				}
				
				final BCT_Node f = x;
				final BCT_Node g = y.getRightChild();
				
				/* Build C @ f @ g */
				top.setLeftChild(new BCT_Application(new BCT_BuiltIn(BCT_NodeType.C), f));
				top.setRightChild(g);
				
				/* Optimize f, g */
				parentList.put(f, top.getLeftChild());
				optimize(f);
				parentList.put(g, top);
				optimize(g);
				
				return;
			}
		}
		
		optimize(x);
		optimize(y);
	}
		
}