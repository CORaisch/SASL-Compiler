/**
 * COMPILER
 * 
 * Schedules the compilation Phase.
 * 
 */
package Compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import AST.*;
import BCT.*;

public class Compiler {

	/**GLOBAL DATA
	 * 
	 * defList  : parsed Data. Associates function names to parsed ASTs.
	 * compList : compiled Data. Associates function names to compiled BCTs.
	 */
	private HashMap<String, AST> defList;
	private HashMap<String, BCT> compList;
	
	/**COMPILATION STEPS
	 * 
	 * linker      : Visitor to link function calls into functions.
	 * transformer : Visitor to convert AST to BCT
	 * abstractor  : Visitor to transform parsed ASTs into S,K,I,Y,U equivalents 
	 */
	private Linker linker;
	private Transformer transformer;
	private Abstractor abstractor;
	
	/**CONSTRUCTOR
	 * 
	 * Initializes Data for Compilation Step.
	 * 
	 * @param defList contains parsed Data to compile
	 */
	public Compiler(HashMap<String, AST>defList) {
		this.defList = defList;
		compList = new HashMap<String, BCT>();
		linker = new Linker();
		transformer = new Transformer();
		abstractor = new Abstractor();
	}
	
	/**COMPILE (MAIN ROUTINE) 
	 * 
	 * Main Routine of Compilation Phase. 
	 * First iterates through parsed Data (defList) and accepts
	 * every Function (except 00FuncCall00) with Abstractor, to
	 * build SKIYU-Representation and then accepts with Transformer,
	 * to convert from AST to BCT.
	 * If there are local Definitions in '00FuncCall00' we proceed the
	 * same way with it, else we do nothing with it.
	 * At the end the Linker will link all left Function calls to their
	 * compiled Representation in 'compList'.
	 * Final Results of compilation phase will be stored in 'compList'.
	 * 
	 * @return compList, that contains compiled Representation of source code. It's ready to reduce.
	 */
	public HashMap<String, BCT> compile() {
		
		//Iterate over all Global Functions and accept on Root
		Iterator<Entry<String, AST>> defIter = defList.entrySet().iterator();
		while(defIter.hasNext()) {
			Entry<String, AST> defPair = defIter.next();
			//Main function call has to be treat different 
			if(!defPair.getKey().equals("00FuncCall00")) {
				//Treat global Definitions
				defPair.getValue().getRoot().accept(abstractor);
				abstractor.reset();
				//Convert Tree to BCT
				defPair.getValue().getRoot().getRightChild().accept(transformer);				
				compList.put(defPair.getKey(), new BCT(transformer.getBCT()));
			} else {
				//Treat main function call				
				if(abstractor.checkForLocalDef(defList.get("00FuncCall00").getRoot())) { 
					defList.get("00FuncCall00").getRoot().accept(abstractor);											
					defList.put("00FuncCall00", new AST(abstractor.getAST()));
				}
				//Just copy it back
				defList.get("00FuncCall00").getRoot().accept(transformer);
				compList.put("00FuncCall00", new BCT(transformer.getBCT()));
			}
		}
		
		/*Invariant: All Functions are freed from its Variables => There are just function calls left*/
		
		//Replace function calls
		
		//Initialize replacer
		linker.init(compList);
		//Iterate over compList
		Iterator<Entry<String, BCT>> compIter = compList.entrySet().iterator();
		while(compIter.hasNext()) {
			Entry<String, BCT> compPair = compIter.next();
			//Accept Root with Replacer-Visitor			
			compPair.getValue().getRoot().accept(linker);
			compList.put(compPair.getKey(), new BCT(linker.getBCT()));			
		}
		
		//We have abstracted and replaced BCTs now in compList
		return compList;				
	}	
	
}