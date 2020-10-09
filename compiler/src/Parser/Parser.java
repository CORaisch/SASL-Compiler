package Parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import AST.*;
import Lexer.*;
import Tokens.*;

public class Parser {
	
	private HashMap<String, AST> defList;
	private Lexer lexer;
	
	/*
	 * Constructor
	 */
	public Parser(Lexer lex) {
		this.lexer = lex;
		this.defList = new HashMap<String, AST>();	
	}

	/*Creates an AST. 
	 *Later it will return a map of parsed definitions
	 */
	public HashMap<String, AST> createDefList() {
		system();
		return this.defList;
	}
	
	// PRIVATES		

	private void system() {
		
		if(lexer.lookAhead().getType() == TokenType.DEF) {
			funcdefs();	//RETURN Abstraction
			if(lexer.lookAhead().getType() == TokenType.DOT) {
				lexer.nextToken();
				AST_Node callTree = expr();	//RETURN Application
				String callString = "00FuncCall00";
				AST callAST = new AST(callTree);
				if(!defList.containsKey(callString)) {
					defList.put(callString, callAST);
				} else {
					System.err.println("Ambiguous Identifiers in Definitions!");
					System.exit(1);
				}
			} else {
				System.err.println("Expected . , but found "+TokenType.toString(lexer.lookAhead().getType()));
				System.exit(1);
			}
		} else {
			AST_Node callTree = expr();	//RETURN Application
			String callString = "00FuncCall00";
			AST callAST = new AST(callTree);
			if(!defList.containsKey(callString)) {
				defList.put(callString, callAST);
			}
		}
		/*
		if(lexer.lookAhead().getType() == TokenType.EOF) {
			System.out.println("File hopefully parsed correct.");
		}
		*/
	}
	
	private void funcdefs() {//Fertig
		
		if(lexer.lookAhead().getType() == TokenType.DEF) {
			lexer.nextToken();
			parseDef();//RETURN Abstraction
			funcdefs1();
		} else {
			System.err.println("Expected DEF, but found "+TokenType.toString(lexer.lookAhead().getType()));
			System.exit(1);
		}
	}
	
	private void funcdefs1() {//Fertig
		
		if(lexer.lookAhead().getType() == TokenType.DEF) {
			lexer.nextToken();
			parseDef();
			funcdefs1();
		} else {
			;
		}
	}

	private void parseDef() {//Fertig
		AST_Application nameDefApp = def();
		if(nameDefApp.getLeftChild().getNodeType() == AST_NodeType.IDENTIFIER) {
			AST_Identifier nameNode = (AST_Identifier) nameDefApp.getLeftChild();
			String key = nameNode.toString();
			AST defAST = new AST(nameDefApp.getRightChild());			
			if(!defList.containsKey(key))
				defList.put(key, defAST);
		} else {
			System.err.println("No Name found after DEF Statement.");
			System.exit(1);
		}
	}

	private AST_Application def() {//Fertig
		
		AST_Identifier nameNode = name();
		AST_Application tempApp = null;
		if(nameNode != null) {
		tempApp = new AST_Application(nameNode, abstraction(nameNode));	//ArgumentNode anlegen!!		
		} else {
			System.err.println("Cant find Identifier.");
			System.exit(1);			
		}
		return tempApp;
	}

	private AST_Abstraction abstraction(AST_Node nameNode) {//Fertig
		
		//Abstraction erzeugen
		AST_Abstraction tempAbstr;
		AST_Node tempNode;
		if(lexer.lookAhead().getType() == TokenType.EQ) {
			lexer.nextToken();
			tempNode = expr();	//Create Right Subtree
			tempAbstr = new AST_Abstraction(nameNode, tempNode);
		} else {	//Create Left Subtree
			tempNode = name(); 
			AST_Argument tempArg = new AST_Argument(nameNode, tempNode);
			tempAbstr = abstraction(tempArg);
		}
		return tempAbstr;
	}

	private AST_Identifier name() {//Fertig
		
		return id();
	}

	private AST_Identifier id() {//Fertig
		
		AST_Identifier idLeaf = null;
		Token tempToken = lexer.lookAhead();
		if(tempToken.getType() == TokenType.ID) {
			idLeaf = new AST_Identifier(tempToken.toString());
			lexer.nextToken();
		}
		return idLeaf;
	}

	
	/*
	 * Wenn man eine 'where' in der 'where' schachtelt, so sieht der Baum noch etwas komisch aus.
	 * Wir sollten uns noch eine bessere Variante ueberlegen.
	 * 
	 */
	private AST_Node expr() {//Fertig
		
		AST_Node condNode = condexpr();
		AST_Node expr1Node = expr1();
		if(expr1Node == null)
			return condNode;
		else {
			AST_Where tempWhere = (AST_Where) expr1Node;
			/*
			 * TEST ZONE \START
			 *
			System.out.println("#### EXPR SIZE OF WHERE DEF LIST: "+tempWhere.getLocalDefs().size()+"####");
			ASTVisualizer vis = new ASTVisualizer("LocalDefTest");
			Iterator<Entry<String, AST>> iter = tempWhere.getLocalDefs().entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, AST> tmpEntry = iter.next();
				vis.createGraph(tmpEntry.getKey(), tmpEntry.getValue());
			}			
			 *
			 * TEST ZONE \END
			 */
			if(tempWhere.getRightChild() == null)
				return new AST_Where(condNode, tempWhere.getLeftChild(), tempWhere.getLocalDefs());
			else
				return new AST_Where(condNode, tempWhere, tempWhere.getLocalDefs());
		}
	}

	private AST_Node expr1() {//Fertig
		
		if(lexer.lookAhead().getType() == TokenType.WHERE){
			lexer.nextToken();
			//Create local HashMap for Where Node
			HashMap<String, AST> tempDefList = new HashMap<String, AST>();
			AST_Node defsNode = defs(tempDefList);
			AST_Node exprNode = expr1();
			if(exprNode == null) 
				return new AST_Where(defsNode, null, tempDefList);//former:  return defsNode;
			else {
				AST_Where tempWhere = (AST_Where)exprNode;
				HashMap<String, AST> tempMap = tempWhere.getLocalDefs();
				tempMap = this.merge(tempMap, tempDefList);
				//tempDefList & exprNode.getLocalDefs() muessen gemerged werden damit keine Eintraege verloren gehen
				return new AST_Where(defsNode, tempWhere.getLeftChild(), tempMap);	
			}
		} else {
			return null;
		}
	}
	
	private HashMap<String, AST> merge(HashMap<String, AST> mapOne, HashMap<String, AST> mapTwo) {
		Iterator<Entry<String, AST>> iter = mapTwo.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, AST> tempEntry = iter.next();
			if(!mapOne.containsKey(tempEntry.getKey())) 
				mapOne.put(tempEntry.getKey(), tempEntry.getValue());
			else { 
				//System.err.println("Ambiguous Identifier in local Definition Hierarchy!");
			}
		}
		return mapOne;
	}

	private AST_Node defs(HashMap<String, AST> tempDefs) {//Fertig
		
		AST_Node tempDefNode = def();
		AST_Identifier idNode = (AST_Identifier) tempDefNode.getLeftChild();
		String key = idNode.toString();
		AST_Node defNode = tempDefNode.getRightChild();
		//Set local Def Entry in HashMap
		if(!tempDefs.containsKey(key)) 
			tempDefs.put(key, new AST(defNode));
		else {
			System.err.println("Ambiguousity in local Definitions !");		//ACHTUNG: Koennte zu ungewollten Abbruechen fuehren!!
			System.exit(1);
		}
		AST_Node nextDefNode = defs1(tempDefs);
		if(nextDefNode == null)
			return defNode;		
		else
			return new AST_Semicolon(defNode, nextDefNode);
	}

	private AST_Node defs1(HashMap<String, AST> tempDefs) {//Fertig
		
		if(lexer.lookAhead().getType() == TokenType.SEMICOLON) {
			lexer.nextToken();
			AST_Node tempDefNode = def();
			AST_Identifier idNode = (AST_Identifier) tempDefNode.getLeftChild();
			String key = idNode.toString();
			AST_Node defNode = tempDefNode.getRightChild();
			//Set local Def Entry in HashMap
			if(!tempDefs.containsKey(key))
				tempDefs.put(key, new AST(defNode));
			else {
				System.err.println("Ambiguousitiy in local Definitions");
				System.exit(1);
			}
			AST_Node nextDefNode = defs1(tempDefs);
			if(nextDefNode == null)
				return defNode;			
			else
				return new AST_Semicolon(defNode, nextDefNode);
		} else {
			return null;
		}
	}

	private AST_Node condexpr() {//Fertig
		
		AST_Node returnNode = null;
		if(lexer.lookAhead().getType() == TokenType.IF) {
			lexer.nextToken();
			AST_Node ifExpr = expr();
			if(lexer.lookAhead().getType() == TokenType.THEN) {
				lexer.nextToken();
				AST_Node thenExpr = condexpr();
				if(lexer.lookAhead().getType() == TokenType.ELSE) {
					lexer.nextToken();
					AST_Node condExpr = condexpr();
					AST_Application ifApp = new AST_Application(new AST_BuiltIn(AST_NodeType.COND), ifExpr);
					AST_Application thenApp = new AST_Application(ifApp, thenExpr);
					if(condExpr.getNodeType() != AST_NodeType.WHERE) {						
						returnNode = new AST_Application(thenApp, condExpr);						
					} else {
						AST_Where tempWhere = (AST_Where) condExpr;
						AST_Node elseExpr = tempWhere.getLeftChild();
						AST_Node localDefExpr = tempWhere.getRightChild();
						//Applications erzeugen										
						AST_Application condTree = new AST_Application(thenApp, elseExpr);
						returnNode = new AST_Where(condTree, localDefExpr, tempWhere.getLocalDefs());	//Kommt das Where nach einer 'else' -> Sichtbarkeit der lok. Defs ueberall!!
					}
				} else {
					System.err.println("ELSE Statement expected, but found "+TokenType.toString(lexer.lookAhead().getType()));
					System.exit(1);
				}
			} else {
				System.err.println("THEN Statement expected, but found "+TokenType.toString(lexer.lookAhead().getType()));
				System.exit(1);
			}			
		} else {
			returnNode = listexpr();
		}
		
		return returnNode;
	}

	private AST_Node listexpr() {//Fertig
		
		AST_Node opNode = opexpr();
		AST_Node listNode = listexpr1();
		if(listNode != null) {
			if(listNode.getNodeType() == AST_NodeType.WHERE) {
				AST_Where tempWhere = (AST_Where) listNode;
				AST_Node rightNode = tempWhere.getRightChild();
				AST_Node leftNode = tempWhere.getLeftChild();
				AST_Application listApp = new AST_Application(new AST_Application(new AST_BuiltIn(AST_NodeType.COLON), opNode), leftNode);				
				return new AST_Where(listApp, rightNode, tempWhere.getLocalDefs());
			}
			AST_Application listApp = new AST_Application(new AST_BuiltIn(AST_NodeType.COLON), opNode);
			return new AST_Application(listApp, listNode);
		}
		return opNode;

	}

	private AST_Node listexpr1() {	//FERTIG!!
		
		if(lexer.lookAhead().getType() == TokenType.COLON) {
			lexer.nextToken();
			return expr();
		} else {
			return null;
		}
	}
	
	private AST_Node opexpr() {//Fertig
		
		return opexpr1(conjunct());
	}

	private AST_Node opexpr1(AST_Node opNode) {	//Fertig
		
		AST_Node tempNode = opNode;
		if(lexer.lookAhead().getType() == TokenType.OR) {
			lexer.nextToken();
			AST_Node conjunctNode = conjunct();
			AST_Application opApp = new AST_Application(new AST_Application(new AST_BuiltIn(AST_NodeType.OR), opNode), conjunctNode);
			tempNode = opexpr1(opApp);
		}
		return tempNode;
	}

	private AST_Node conjunct() {//Fertig
		
		return conjunct1(compar());
	}

	private AST_Node conjunct1(AST_Node conjunctNode) {//Fertig
		
		AST_Node tempNode = conjunctNode;		
		if(lexer.lookAhead().getType() == TokenType.AND) {
			lexer.nextToken();
			AST_Node comparNode = compar();
			AST_Application conjunctApp = new AST_Application(new AST_Application(new AST_BuiltIn(AST_NodeType.AND), conjunctNode), comparNode);
			tempNode = conjunct1(conjunctApp);
		} 
		return tempNode;
	}

	private AST_Node compar() {//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>HIER_WEITER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		
		AST_Node addNode = add();
		return compar1(addNode);
	}

	private AST_Node compar1(AST_Node left) {
		
		if(isRelOP(lexer.lookAhead().getType())) {
			AST_BuiltIn rel = relop();
			AST_Node addNode = add();
			AST_Application relApp = new AST_Application(new AST_Application(rel, left), addNode);
			return compar1(relApp);
		} else {
			return left;
		}
	}

	private AST_BuiltIn relop() {	
		
		Token tempToken = lexer.lookAhead();
		AST_BuiltIn result = null;
		lexer.nextToken();
		
		switch(tempToken.getType()) {
		
		case LT:
			result = new AST_BuiltIn(AST_NodeType.LT);
			break;
		case GT:
			result = new AST_BuiltIn(AST_NodeType.GT);
			break;
		case LEQ:
			result = new AST_BuiltIn(AST_NodeType.LEQ);
			break;
		case GEQ:
			result = new AST_BuiltIn(AST_NodeType.GEQ);
			break;
		case EQ:
			result = new AST_BuiltIn(AST_NodeType.EQ);
			break;
		case NEQ:
			result = new AST_BuiltIn(AST_NodeType.NEQ);
			break;
		default:
			System.err.println("Relational Operation expected, but found "+TokenType.toString(tempToken.getType()));
			System.exit(1);
		}
		return result;
	}	
	
	private AST_Node add() { //<<<<<<<<<<<<<<<<<<<<<<<<<<BIS_HIER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		AST_Node mul = mul();
		return add1(mul);
	}

	private AST_Node add1(AST_Node right) {
		
		if(isAddOP(lexer.lookAhead().getType())) {
			AST_BuiltIn op = addop();
			AST_Node mul = mul();
			AST_Application right1 = new AST_Application(new AST_Application(op, right), mul);
			return add1(right1);
		} else {
			return right;
		}
	}

	private AST_BuiltIn addop() {
		
		Token tempToken = lexer.lookAhead();
		lexer.nextToken();
		
		AST_NodeType type = null;
		
		switch (tempToken.getType()){
		case PLUS:
			type = AST_NodeType.PLUS;
			break;
		case MINUS:
			type = AST_NodeType.MINUS;
			break;
		default: 
			System.err.println("Addition Operation Expected, but found "+TokenType.toString(tempToken.getType()));
			System.exit(1);
		}
		
		return new AST_BuiltIn(type);
	}

	private AST_Node mul() {
		
		AST_Node right = factor();
		return mul1(right);
	}

	private AST_Node mul1(AST_Node right) {
		
		if(isMulop(lexer.lookAhead().getType())) {
			AST_BuiltIn op = mulop();
			AST_Node temp = factor();
			AST_Application result = new AST_Application(new AST_Application(op, right), temp);
			return mul1(result);
		} else {
			return right;
		}
	}

	private AST_BuiltIn mulop() {
		
		Token tempToken = lexer.lookAhead();
		lexer.nextToken();
		
		AST_NodeType type = null;
		
		switch (tempToken.getType()){
		case MUL:
			type = AST_NodeType.MUL;
			break;
		case DIV:
			type = AST_NodeType.DIV;
			break;
		default: 
			System.err.println("Multiplication Operation Expected, but found "+TokenType.toString(tempToken.getType()));
			System.exit(1);
		}
		
		return new AST_BuiltIn(type);
	}
	
	private AST_Node factor() {
		
		if(isPrefix(lexer.lookAhead().getType())) {
			AST_BuiltIn prefix = prefix();
			
			if (prefix.getNodeType() == AST_NodeType.NOT) {
				return new AST_Application(prefix, comb());
			} else {
				return new AST_Application(new AST_Application(prefix, new AST_Num(0)), comb());
			}
		} else {
			return comb();
		}
	}

	private AST_Node comb() {
		
		AST_Node sim = simple();
		return comb1(sim);
	}

	private AST_Node comb1(AST_Node left) {
			
		if(isSimple(lexer.lookAhead().getType())) {		
			AST_Application result = new AST_Application(left, simple());
			return comb1(result);
		} else {
			return left;
		}
	}

	private AST_Node simple() {
		
		AST_Node result = null;
		
		Token temp = lexer.lookAhead();
		if(isBuiltin(temp.getType())) {
			result = builtin();
		} else if(temp.getType() == TokenType.ID){
			result = name();
		} else if(isConstant(temp.getType())) {
			result = constant();
		} else if(temp.getType() == TokenType.LBRACKETR) {
			lexer.nextToken();
			AST_Node tempNode = expr();
			if(lexer.lookAhead().getType() != TokenType.RBRACKETR) {
				System.err.println("Expected ), but found "+TokenType.toString(lexer.lookAhead().getType()));
				System.exit(1);
			} else {
				lexer.nextToken();
				result = tempNode;
			}
		} else {
			System.err.println("Expected Simple, but found "+TokenType.toString(lexer.lookAhead().getType())); 	
			System.exit(1);
		}
		
		return result;
	}

	private AST_BuiltIn builtin() {
		
		Token curToken = lexer.lookAhead();
		AST_BuiltIn resultNode = null;
		
		lexer.nextToken();
		
		switch(curToken.getType()){
		
		case HD:
			resultNode = new AST_BuiltIn(AST_NodeType.HD);
			break;
		case TL:
			resultNode = new AST_BuiltIn(AST_NodeType.TL);
			break;
		default:
			System.err.println("HD or TL Expected, but found "+TokenType.toString(curToken.getType()));
			System.exit(1);			
		}
		return resultNode;
	}

	private AST_Node constant() {
		
		AST_Node result = null;
		TokenType type = lexer.lookAhead().getType();
		if(type == TokenType.NUM) {
			result = num();
		} else if(type == TokenType.BOOL) {
			result = bool();
		} else if(type == TokenType.STRING) {
			result = string();
		} else if(type == TokenType.NIL) {
			lexer.nextToken();
			result = new AST_BuiltIn(AST_NodeType.NIL);
		} else if(type == TokenType.LBRACKETS) {
			lexer.nextToken();
			result = list1();
		} else {
			System.err.println("Constant expected, but found "+TokenType.toString(type));
			System.exit(1);
		}
		return result;
	}

	private AST_Node list1() {
		
		AST_Node result = null;
		if(lexer.lookAhead().getType() == TokenType.RBRACKETS) {
			lexer.nextToken();
			result = new AST_BuiltIn(AST_NodeType.NIL);	//Nil = [] ?
		} else {
			AST_Node listElems = listelems();
			if(lexer.lookAhead().getType() == TokenType.RBRACKETS) {
				lexer.nextToken();
				result = listElems;
			} else {
				System.err.println("Expected ], but found "+TokenType.toString(lexer.lookAhead().getType()));
				System.exit(1);
			}
		}
		return result;
	}

	private AST_Node listelems() {
		
		AST_Node exprNode = expr();
		AST_Application listApp = new AST_Application(new AST_BuiltIn(AST_NodeType.COLON), exprNode);
		return new AST_Application(listApp, listelems1());
	}

	private AST_Node listelems1() {
		
		if(lexer.lookAhead().getType() == TokenType.COMMA) {
			lexer.nextToken();
			AST_Node exprNode = expr();
			AST_Application listApp = new AST_Application(new AST_BuiltIn(AST_NodeType.COLON), exprNode);
			return new AST_Application(listApp, listelems1());
		} else {
			return new AST_BuiltIn(AST_NodeType.NIL);
		}
	}

	private AST_String string() {	//Fertig
		
		T_String stringToken = (T_String)lexer.lookAhead();
		AST_String temp = new AST_String(stringToken.getValue());
		lexer.nextToken();
		return temp;
	}

	private AST_Bool bool() {	//Fertig
		
		T_Bool boolToken = (T_Bool)lexer.lookAhead();
		AST_Bool temp = new AST_Bool(boolToken.getValue());
		lexer.nextToken();
		return temp;
	}

	private AST_Num num() {	//Fertig
		
		T_Num numToken = (T_Num)lexer.lookAhead();
		AST_Num temp = new AST_Num(numToken.getValue());
		lexer.nextToken();		
		return temp;
	}

	private AST_BuiltIn prefix() {	//Fertig
		
		T_Symbol tempToken = (T_Symbol)lexer.lookAhead();		
		lexer.nextToken();
		
		AST_NodeType type = null;
		
		switch (tempToken.getType()){
		case PLUS:
			type = AST_NodeType.PLUS;
			break;
		case MINUS:
			type = AST_NodeType.MINUS;
			break;
		case NOT:
			type = AST_NodeType.NOT;
			break;
		default:
			System.err.println("Prefix Expected, but found "+TokenType.toString(tempToken.getType()));
			System.exit(1);
		}
		
		return new AST_BuiltIn(type);
	}
	
	// HELPERS
	
	private boolean isRelOP(TokenType type) {
		return (type == TokenType.EQ || type == TokenType.NEQ || type == TokenType.LT || type == TokenType.GT || type == TokenType.LEQ || type == TokenType.GEQ);
	}

	private boolean isAddOP(TokenType type) {
		return (type == TokenType.PLUS || type == TokenType.MINUS);
	}
	
	private boolean isMulop(TokenType type) {
		return (type == TokenType.MUL || type == TokenType.DIV);
	}

	private boolean isPrefix(TokenType type) {
		return (type == TokenType.MINUS || type == TokenType.PLUS || type == TokenType.NOT);
	}

	private boolean isSimple(TokenType type) {
		return (type == TokenType.ID || isBuiltin(type) || isConstant(type) || type == TokenType.LBRACKETR);
	}

	private boolean isConstant(TokenType type) {
		return (type == TokenType.NUM || type == TokenType.BOOL || type == TokenType.STRING || type == TokenType.NIL || type == TokenType.LBRACKETS);
	}

	private boolean isBuiltin(TokenType type) {
		return (type == TokenType.HD || type == TokenType.TL);
	}

	
}
