package AST;

import org.junit.Test;

public class AST_Test {

	@Test
	public void test() {
		//Test Tree erstellen
		AST testAST = createTestTree();
		AST_Visualizer visualizer = new AST_Visualizer("test");
		visualizer.createGraph("test", testAST);
		
	}

	private AST createTestTree() {
		/*
		 * Creates a Test Tree 
		 * 
		 * The Tree is built manually.
		 * It represents the AST for the 'map'-Function of the Prelude.
		 * 
		 */
		//Left Subtree of Map
		AST_Leaf map = new AST_Identifier("map");
		AST_Leaf f = new AST_Identifier("f");
		AST_Application app0 = new AST_Application(map, f);
		AST_Leaf l = new AST_Identifier("l");
		AST_Application app1 = new AST_Application(app0, l);
		
		//Right Subtree of Map
		
		//COND Tree
		AST_Leaf eq = new AST_BuiltIn(AST_NodeType.EQ);
		AST_Leaf l1 = new AST_Identifier("l");
		AST_Application app2 = new AST_Application(eq, l1);
		AST_Leaf nil = new AST_BuiltIn(AST_NodeType.NIL);
		AST_Application app3 = new AST_Application(app2, nil);
		AST_Leaf cond = new AST_BuiltIn(AST_NodeType.COND);
		AST_Application app4 = new AST_Application(cond, app3);
		AST_Leaf nil2 = new AST_BuiltIn(AST_NodeType.NIL);
		AST_Application app5 = new AST_Application(app4, nil2);
		
		//ELSE Tree -->Connect with Where
		AST_Leaf f1 = new AST_Identifier("f");
		AST_Leaf x = new AST_Identifier("x");
		AST_Application app6 = new AST_Application(f1, x);
		AST_Leaf colon = new AST_BuiltIn(AST_NodeType.COLON);
		AST_Application app7 = new AST_Application(app6, colon);
		AST_Leaf map1 = new AST_Identifier("map");
		AST_Leaf f2 = new AST_Identifier("f");
		AST_Application app8 = new AST_Application(map1, f2);
		AST_Leaf xs = new AST_Identifier("xs");
		AST_Application app9 = new AST_Application(app8, xs);
		AST_Application app10 = new AST_Application(app7, app9);
		
		//SemiColon Tree
		AST_Leaf hd = new AST_BuiltIn(AST_NodeType.HD);
		AST_Leaf l2 = new AST_Identifier("l");
		AST_Application app11 = new AST_Application(hd, l2);
		AST_Leaf x1 = new AST_Identifier("x");
		AST_Abstraction semiLeft = new AST_Abstraction(x1, app11);
		
		AST_Leaf tl = new AST_BuiltIn(AST_NodeType.TL);
		AST_Leaf l3 = new AST_Identifier("l");
		AST_Application app12 = new AST_Application(tl, l3);
		AST_Leaf xs1 = new AST_Identifier("xs");
		AST_Abstraction semiRight = new AST_Abstraction(xs1, app12);
		
		AST_Semicolon semi = new AST_Semicolon(semiLeft, semiRight);
		
		//Connect Rest with Where and Abstraction
		AST_Where where = new AST_Where(app10, semi);
		AST_Application app13 = new AST_Application(app5, where);
		AST_Abstraction root = new AST_Abstraction(app1, app13);
		
		return new AST(root);
		
	}

}
