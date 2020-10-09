package AST;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Iterator;


public class AST_Visualizer implements AST_Visitor {
	
	private AST_Node root;
	private TreeMap<String, ArrayList<String>> edgeMap;
	private ArrayList<String> nodeList;
	private int nodeIndex;
	private String sourceName;
	
	public AST_Visualizer(String sourceName) {
		this.root = null;
		this.sourceName = sourceName;
		edgeMap = new TreeMap<String, ArrayList<String>>();
		nodeList = new ArrayList<String>();
		nodeIndex = 0;
	}
	
	public void reset() {
		this.root = null;
		this.edgeMap.clear();
		this.nodeList.clear();
		this.nodeIndex = 0;
	}
	
	public void createGraph(String defName, AST ast) {
		this.root = ast.getRoot();
		this.root.accept(this);
		
		//Print into Test File
		try{
			//Open OutputStream
			FileWriter fw = new FileWriter(sourceName+"."+defName+".dot");
			BufferedWriter out = new BufferedWriter(fw);					
			
			//Include Header																none
			String header = "digraph g {\ngraph [ nodesep=0.5 ranksep=0.3 ];\nnode [ shape=circle ];\nedge [labelangle = 40,labeldistance=1.5];\n"; 
			out.write(header+"\n");		
			
			//Print Node Definitions
			Iterator<String> iter = nodeList.iterator();
			while(iter.hasNext()) {
				out.write(iter.next());				
			}
			
			//Print Edge Definitions
			Iterator<Entry<String, ArrayList<String>>> it = edgeMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, ArrayList<String>> pairs = (Map.Entry<String, ArrayList<String>>) it.next();
				Iterator<String> innerIter = pairs.getValue().iterator();
				while(innerIter.hasNext())
					out.write(pairs.getKey()+" -> "+innerIter.next()+";\n");					
			}
			
			//Add closing Bracket
			out.write("}\n");
			//Close Stream
			out.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		this.reset();
	}
	
	private void operateOnNode(AST_Node node) {
		int localIndex = this.nodeIndex;
		ArrayList<String> localList = new ArrayList<String>();
		
		nodeList.add("N"+localIndex+"[label=\""+node.toString()+"\"]\n");
		
		this.nodeIndex++;			
		localList.add("N"+this.nodeIndex);
		edgeMap.put("N"+localIndex, localList);
		node.getLeftChild().accept(this);
		
		this.nodeIndex++;
		localList.add("N"+this.nodeIndex);
		edgeMap.put("N"+localIndex, localList);
		node.getRightChild().accept(this);
	}
	
	//---------------------__VISITs_---------------------------------------------------------------
	
	@Override
	public void visit(AST_Leaf leaf) {
		nodeList.add("N"+nodeIndex+"[label=\""+leaf.toString()+"\"]\n");
	}
	
	@Override
	public void visit(AST_Application app) {
		this.operateOnNode(app);
	}

	@Override
	public void visit(AST_Abstraction abstr) {		
		this.operateOnNode(abstr);
	}

	@Override
	public void visit(AST_Argument args) {
		this.operateOnNode(args);
	}

	@Override
	public void visit(AST_Semicolon semi) {
		this.operateOnNode(semi);
	}

	@Override
	public void visit(AST_Where where) {
		this.operateOnNode(where);
	}

	@Override
	public void visit(AST_BuiltIn node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
	}

	@Override
	public void visit(AST_Identifier node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
	}

	@Override
	public void visit(AST_Bool node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
	}

	@Override
	public void visit(AST_Num node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
	}

	@Override
	public void visit(AST_String node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
	}

}
