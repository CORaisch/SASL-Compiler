package BCT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class BCT_Visualizer implements BCT_Visitor {
		
	private BCT_Node root;
	private String sourcename;
	private String lastNodeName;
	private TreeMap<String, ArrayList<String>> edgeMap;
	private ArrayList<String> nodeList;
	private int nodeIndex;
	private HashMap<BCT_Node, String> visitedList;
	
	
	public BCT_Visualizer(String source) {
		root = null;
		sourcename = source;
		lastNodeName = "";
		edgeMap = new TreeMap<String, ArrayList<String>>();
		nodeList = new ArrayList<String>();
		visitedList = new HashMap<BCT_Node, String>();
		nodeIndex = 0;
	}
	
	public void reset() {
		this.root = null;
		this.edgeMap.clear();
		this.nodeList.clear();
		this.visitedList.clear();
		this.nodeIndex = 0;
		lastNodeName = "";
	}
	
	public void createGraph(String name, BCT bct) {		
		root = bct.getRoot();		
		root.accept(this);
		
		//Write to File		
		try{
			FileWriter fw = new FileWriter(sourcename+"."+name+".dot");
			BufferedWriter out = new BufferedWriter(fw);
			
			//Build Header																none
			String header = "digraph g {\ngraph [ nodesep=0.5 ranksep=0.3 ];\nnode [ shape=circle ];\nedge [labelangle = 40,labeldistance=1.5];\n"; 
			out.write(header+"\n");
			
			Iterator<String> iter = nodeList.iterator();
			while(iter.hasNext()) {
				out.write(iter.next());
			}
			
			out.write("\n");
			
			Iterator<Entry<String, ArrayList<String>>> it = edgeMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, ArrayList<String>> pairs = (Map.Entry<String, ArrayList<String>>) it.next();
				Iterator<String> innerIter = pairs.getValue().iterator();
				while(innerIter.hasNext())
					out.write(pairs.getKey()+" -> "+innerIter.next()+";\n");					
			}
			
			out.write("}\n");
			out.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		//Reset Visualizer State for next use
		this.reset();
	}		
	
	//---------------------------------------------------------------------------------------------------------
	//----------------------------------------------Visits-----------------------------------------------------
	//---------------------------------------------------------------------------------------------------------

	@Override
	public void visit(BCT_Application node) {
		
		if(visitedList.containsKey(node)) {
			ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);
			listOfLastNode.add(visitedList.get(node));
			edgeMap.put(lastNodeName, listOfLastNode);
		} else {							
			
			//Wenn App nicht Root, dann Male Kante zu vorherigen App
			if(nodeIndex != 0) {
				//Male Kante zu vorherigen Knoten
				ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);
				listOfLastNode.add("N"+nodeIndex);
				edgeMap.put(lastNodeName, listOfLastNode);
			} //Sonst male keine Kante, da Root
			
			int localIndex = nodeIndex;
			lastNodeName = "N"+localIndex;				
			
			visitedList.put(node, lastNodeName);
			
			nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");								
			
			edgeMap.put(lastNodeName, new ArrayList<String>());
			
			nodeIndex++;			
			node.getLeftChild().accept(this);
			
			lastNodeName = "N"+localIndex;
			
			nodeIndex++;			
			node.getRightChild().accept(this);						
		}		
	}

	@Override
	public void visit(BCT_BuiltIn node) {
		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
		
		ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);
		if(listOfLastNode != null) {
			listOfLastNode.add("N"+nodeIndex);						
			edgeMap.put(lastNodeName, listOfLastNode);
		}
	}

	@Override
	public void visit(BCT_ConstantNum node) {

		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
		
		ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);		
		if(listOfLastNode != null) {												
			listOfLastNode.add("N"+nodeIndex);						
			edgeMap.put(lastNodeName, listOfLastNode);
		}
	}

	@Override
	public void visit(BCT_ConstantString node) {

		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
		
		ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);		
		if(listOfLastNode != null) {												
			listOfLastNode.add("N"+nodeIndex);						
			edgeMap.put(lastNodeName, listOfLastNode);
		}
	}

	@Override
	public void visit(BCT_ConstantBool node) {

		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
		
		ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);		
		if(listOfLastNode != null) {												
			listOfLastNode.add("N"+nodeIndex);						
			edgeMap.put(lastNodeName, listOfLastNode);
		}
	}

	@Override
	public void visit(BCT_Variable node) {

		nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");
		
		ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);		
		if(listOfLastNode != null) {												
			listOfLastNode.add("N"+nodeIndex);						
			edgeMap.put(lastNodeName, listOfLastNode);
		}
	}

	@Override
	public void visit(BCT_Pair node) {

		if(visitedList.containsKey(node)) {
			ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);
			listOfLastNode.add(visitedList.get(node));
			edgeMap.put(lastNodeName, listOfLastNode);
		} else {							
			
			//Wenn App nicht Root, dann Male Kante zu vorherigen App
			if(nodeIndex != 0) {
				//Male Kante zu vorherigen Knoten
				ArrayList<String> listOfLastNode = edgeMap.get(lastNodeName);
				listOfLastNode.add("N"+nodeIndex);
				edgeMap.put(lastNodeName, listOfLastNode);
			} //Sonst male keine Kante, da Root
			
			int localIndex = nodeIndex;
			lastNodeName = "N"+localIndex;				
			
			visitedList.put(node, lastNodeName);
			
			nodeList.add("N"+nodeIndex+"[label=\""+node.toString()+"\"]\n");								
			
			edgeMap.put(lastNodeName, new ArrayList<String>());
			
			nodeIndex++;			
			node.getLeftChild().accept(this);
			
			lastNodeName = "N"+localIndex;
			
			nodeIndex++;			
			node.getRightChild().accept(this);						
		}		
	}	//There are no Pairs after Compilation
	
}
