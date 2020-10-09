/**DRIVER
 * 
 * 
 * Manager Class. Schedules the different Compiler Stages.
 * 
 * 
 */
package Driver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;

import AST.AST;
import AST.AST_Visualizer;
import Lexer.Lexer;
import Optimizer.Optimizer;
import Parser.Parser;
import SKIReductionMachine.ReductionMachine;
import Compiler.Compiler;
import BCT.BCT;
import BCT.BCT_Visualizer;

public class Driver{
	
	/**FLAGS
	 * 
	 * Flags for controlling Optimization and Visualization 
	 */
	private static boolean optimize = false;	
	private static boolean visualize = false;
	private static boolean debugMessages = false;
	
	/**GLOBAL HELPERS
	 * 
	 * filename storing source path 
	 * file     storing source file
	 * time     Variable to clock
	 */
	private static String filename;
	private static File file;
	private static long time;
	
	/**COMPILER UNITS
	 * 
	 * lexer as an instance of the Lexicographical Analyzer 
	 * parser as an instance of the parser unit
	 * compiler as an instance of the compilation unit
	 * optimizer as an instance of the optimization unit
	 * reductionMachine as an instance of the reduction unit
	 * 
	 * If visualize-Flag is set graphical representations of each compilation 
	 * Stage are printed out in .dot-Files.
	 */
	private static Lexer lexer;
	private static Parser parser;
	private static Compiler compiler;
	private static Optimizer optimizer;
	private static ReductionMachine reductionMachine;	
	
	/**VISUALIZER
	 * 
	 * AST_visualizer used for visualizing parsed ASTs
	 * BCT_visualizer used for visualizing compiled and optimized BCTs
	 */
	private static AST_Visualizer AST_visualizer;
	private static BCT_Visualizer BCT_visualizer;
	
	/**GLOBAL DATA
	 * 
	 * defList       : Map holding parsed data. Associates function names to parsed tree representations.
	 * compiledList  : Map holding compiled data. Associates function names to compiled tree representations.
	 * optimizedList : Map holding optimized data. Associates function names to optimized tree representations.
	 */
	private static HashMap<String, AST> defList;
	private static HashMap<String, BCT> compiledList;
	private static HashMap<String, BCT> optimizedList;
	
	
	/**MAIN ROUTINE
	 * 
	 * Initializing and starting up several compilation units.
	 * First the lexicographic Analyzer is initialized.
	 * Then the parser is initialized and starts up parsing the source code,
	 * storing results in defList. 
	 * In third step the compiler unit is initialized and start up transforming 
	 * parsed trees into compiled trees using defList, storing its result in compiledList.
	 * If optimized-Flag is set, data from compiledList is used to setting up the optimizer unit,
	 * which shortens compiled trees if possible, storing results in optimizedList.
	 * At the end the reduction machine reduces the compiled (and may optimized) data and
	 * prints the results on output Stream.
	 * 
	 * @param args expects path to source code
	 */
	public static void main(String[] args) {
		/*
		 * Parsing Input 
		 * 
		 */
		
		//Parsing Input
		parseInput(args);		
		
		//GET INITAL TIME
		time = System.currentTimeMillis();
		
		//Init Lexer
		lexer = new Lexer(file);
		parser = new Parser(lexer);		
		
		defList = parser.createDefList();
		
		/*
		 * If visualize Flag is set parsed Trees will be printed
		 */
		if(visualize) {
			//AST VISUALIZING
			filename = filename.replaceAll(".txt", "");
			filename = filename.replaceAll(".sasl", "");
			System.out.println("Number of parsed Definitions: "+defList.size());
			AST_visualizer = new AST_Visualizer(filename);
			Iterator<Entry<String, AST>> parsIter = defList.entrySet().iterator();
			while(parsIter.hasNext()) {
				Map.Entry<String, AST> pair = (Map.Entry<String, AST>)parsIter.next();
				AST_visualizer.createGraph("ParsedOut."+pair.getKey(), pair.getValue());
			}
		}
		/*
		 * END Printing
		 */
		
		//Initialize Compiler
		compiler = new Compiler(defList);
		compiledList = compiler.compile();								
		
		/*
		 * If visualize Flag is set, compiled Trees will be printed
		 */
		if(visualize) {
			//BCT VISUALIZING
			BCT_visualizer = new BCT_Visualizer(filename);		
			System.out.println("Number of compiled Definitions: "+compiledList.size());
			Iterator<Entry<String, BCT>> compIter = compiledList.entrySet().iterator();
			while(compIter.hasNext()) {
				Map.Entry<String, BCT> pair = (Map.Entry<String, BCT>)compIter.next();
				BCT_visualizer.createGraph("CompiledOut."+pair.getKey(), pair.getValue());
			}
		}
		/*
		 * END Printing
		 */
		
		//Optimization
		if(optimize){
			optimizer = new Optimizer(compiledList, debugMessages);
			optimizedList = optimizer.optimize(2);	
			
			/*
			 * If visualize Flag is set, optimized Trees will be printed
			 */
			if(visualize) {
				System.out.println("Number of optimized Definitions: "+optimizedList.size());
				Iterator<Entry<String, BCT>> optIter = optimizedList.entrySet().iterator();
				while(optIter.hasNext()) {
					Map.Entry<String, BCT> pair = (Map.Entry<String, BCT>)optIter.next();
					BCT_visualizer.createGraph("OptimizedOut."+pair.getKey(), pair.getValue());
				}
			}
			/*
			 * END Printing
			 */
			
		} else {
			optimizedList = compiledList;
		}
		
		//Init ReductionMachine		
		reductionMachine = new ReductionMachine(optimizedList.get("00FuncCall00"));
		reductionMachine.reduce();
		
		//GET TIME PROGRAM NEEDED
		time = System.currentTimeMillis() - time;
		
		//Print result (hopefully correct)
		reductionMachine.printResult();				
		
		//PRINT OUT TIME PROGRAM NEEDED
		System.out.println("\nBuild Time: "+time/1000.f+"s");
	}


	private static void parseInput(String[] args) {
		/*If there is no argument print help*/
		if(args.length == 0) {
			//Print Help			
			printHelp();
			//and exit
			System.exit(0);
			
		/*If there is just one argument it has to be the filename*/
		} else if(args.length == 1){		
			//Files has to end with .sasl
			if(args[0].endsWith(".sasl")) {
				//If filename is correct start to compile
				filename = args[0];
				file = new File(filename);
			} else {
				//If not exit
				System.out.println("Wrong file format. Make sure that your file ends with .sasl");
				System.exit(1);
			}
		
		/*If there are two arguments the first one has to be option and the second one has to be filename*/
		} else if(args.length == 2) {
			//First argument has to start with -
			if(args[0].startsWith("-")) {
				//If first argument is correct parse Option
				setFlags(args[0]);
			} else {
				//If not exit
				System.out.println("Wrong usage.");
				printHelp();
				System.exit(1);
			}
			//Second argument has to be filename
			if(args[1].endsWith(".sasl")) {
				//If filename is correct start to compile
				filename = args[1];
				file = new File(filename);
			} else {
				//If not exit
				System.out.println("Wrong file format. Make sure that your file ends with .sasl");
				System.exit(1);
			}
			
		/*Its only possible to use saslc the way: [File]... or [Option]... [File]*/	
		} else {
			//If used wrong exit
			System.out.println("Undefined input.");
			System.exit(1);
		}
	}


	private static void setFlags(String option) {
		switch(option) {
		case "-help":
			printHelp();
			System.exit(0);
			break;
		case "-O":
			optimize = true;
			break;
		case "-D":
			optimize = true;
			debugMessages = true;
			break;
		case "-V":
			visualize = true;
			break;
		case "-OV":
			optimize = true;
			visualize = true;
			break;
		case "-all":
			optimize = true;
			visualize = true;
			debugMessages = true;
			break;
		default:
			System.out.println("Wrong usage.");
			printHelp();
			System.exit(1);			
		}
	}


	private static void printHelp() {
		System.out.println(
				 "Usage: [Option]... [File]...\n"
				+"   or: [File]...\n"
				+"\nCompiles File and print out result. By default, runs without optimization and visualization.\n"
				+"\nOptions:\n"
				+"-help\t\tprint this message.\n"
				+"-O\t\tenable optimization.\n"
				+"-D\t\tenable debug messages. Implies optimization flag.\n"
				+"-V\t\tenable visualization. Parsed ASTs and compiled BCTs are stored as .dot-files.\n"
				+"-OV\t\tenable optimization and visualization.\n"
				+"-all\t\tenable optimization, visualization and debug messages.");
	}	
	
}