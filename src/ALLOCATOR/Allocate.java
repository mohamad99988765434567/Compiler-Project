package ALLOCATOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.*;

import TEMP.*;

public class Allocate {
	
	
	
	//TYPES OF instructions in MIPS
	Set<String> loadInstructions= new HashSet<>(Arrays.asList("lw", "la", "li", "lb")); //will not use it - this is the default case. special case is store/jump instructions
	Set<String> storeInstructions= new HashSet<>(Arrays.asList("sw", "sb"));
	Set<String> uncondJumpInstructions=new HashSet<>(Arrays.asList("j", "jr", "jal", "jalr"));
	Set<String> condJumpInstructions =new HashSet<>(Arrays.asList("beq", "beqz", "bne", "bgt", "bge", "blt", "ble"));
	List<instructionNode> instrctions; //instructions list
	LinkedHashMap<String, instructionNode> labelInstructions; //key - label , value - instruction to jump
	LinkedHashMap<String, List<instructionNode>> funcInstructions; //key - func, value - list of instructions
	LinkedHashMap<String, List<String>> totalNodes; //key - func, value - set of temps
	LinkedHashMap<String, Integer[]> coloring; //key - func, value - colors
	String currFunc=null;
	
	public Allocate() {
		this.instrctions=new ArrayList<>();
		this.labelInstructions=new LinkedHashMap<>();
		this.funcInstructions=new LinkedHashMap<>();
		this.totalNodes=new LinkedHashMap<>();
		this.coloring = new LinkedHashMap<>();
		
	}
	
	
	public void RegistersAllocation(String outputFilename) {
		parsing();
		findSuccessors();
		performLivenessAnalysis();
		updateTotalNodes();
		allocateRegisters();
		finalizeFile(outputFilename);
	}
	
	
	
	
	public void parsing() {
		String filePath="./output/MIPS_DRAFT.txt";
		String line;
		try {
			FileReader f=new FileReader(filePath);
			Scanner s= new Scanner(f);
			while (s.hasNextLine()) {
				line=s.nextLine();
				parseInstruction(line);
				
			}
			s.close();
			f.close();
		}
		
		catch(Exception e){
			e.printStackTrace();
			
		}
		
	
		
	}
	
	
	
	
	
	public void parseInstruction(String line) {
		String[] arr=line.trim().split("[\n\t\\s,()]+"); 
		int size=arr.length;
		String type=arr[0];
		if (uncondJumpInstructions.contains(type)|| condJumpInstructions.contains(type)){ //uncondjump
			jumpNode j=new jumpNode(type,0,arr[size-1]); //dstLabel=arr[size-1]
			for(int i=1; i<size; i++) {
				if(arr[i].startsWith("Temp_")) {
					j.readTemps.add(arr[i]);
				}
			}
			this.instrctions.add(j);
			if(currFunc!=null) {
				this.funcInstructions.get(currFunc).add(j); //add to the function's instruction list
			}
			return;
		}
		if (type.charAt(type.length()-1)==':') {//it's a label
			String Labelname=type.substring(0, type.length()-1);
			instructionNode n=new instructionNode(Labelname,1);
			if (currFunc==null &&Labelname.endsWith("_Prologue")){
				currFunc=this.instrctions.get(this.instrctions.size()-1).type;
				this.funcInstructions.put(currFunc, new ArrayList<>());
				
			}
			if(currFunc!=null &&Labelname.endsWith("_Epilogue")) { //end of function
				currFunc=null;
			}
			
			if(currFunc!=null && (!Labelname.endsWith("_Epilogue"))) {
				this.funcInstructions.get(currFunc).add(n); //add to the function instruction list
			}
			
			this.labelInstructions.put(Labelname, n);
			this.instrctions.add(n);
			return;

		}
		
		//not a label, not a jump/branch
		instructionNode n=new instructionNode(type,0);
		if(storeInstructions.contains(type)) {
			for(int i=1; i<size; i++) {
				if(arr[i].startsWith("Temp_")) {
					n.readTemps.add(arr[i]);
				}
			}
		}
		
		else { //not a store instruction, we write to the first temp (if its a temp)
			if (size>=2 &&arr[1].startsWith("Temp_")) {
				n.writeTemps.add(arr[1]); 
			}
			for(int i=2; i<size; i++) {
				if (arr[i].startsWith("Temp_")) {
					n.readTemps.add(arr[i]);
				}
				
			}
		}
		if(currFunc!=null) {
			this.funcInstructions.get(currFunc).add(n); //add to the function instruction list
		}
		this.instrctions.add(n);
	}
	
	
	
	
	//find successors for each command in each function.
	public void findSuccessors() {
		
		Set<String> funcs=this.funcInstructions.keySet();
		instructionNode n;
		jumpNode j;
		for (String func:funcs) { //for each function
			List<instructionNode> ins=this.funcInstructions.get(func); //instruction list of function
			for (int i=0; i<ins.size(); i++) {
				n=ins.get(i);
				if (n instanceof jumpNode) { //if jump instruction
					j=(jumpNode)n;
					if (uncondJumpInstructions.contains(j.type) && (!j.type.equals("jal"))&&(!j.type.equals("jalr"))) { //uncond jump - one successor
						j.successors.add(this.labelInstructions.get(j.dstLabel));
						//continue;
					}
					if (condJumpInstructions.contains(j.type)||j.type.equals("jal")||j.type.equals("jalr")) { //cond jump - 2 potential successors
						if(condJumpInstructions.contains(j.type)) {
							j.successors.add(this.labelInstructions.get(j.dstLabel));
						}
						if(i<ins.size()-1) { //if there is "next instruction"
							j.successors.add(ins.get(i+1)); //next instruction is a successor
						}
						
						
					}
				}
				else { //not a jump instruction
					if(i<ins.size()-1) {
						n.successors.add(ins.get(i+1));
					}
				}
				
			}
		}

	}
	
	
	//run liveness analysis for each function
	public void performLivenessAnalysis(){
		Set<String> funcs=this.funcInstructions.keySet();
		instructionNode n;
		for (String func:funcs) { //for each function
			boolean updated=true;
			while(updated) {
				List<instructionNode> ins=this.funcInstructions.get(func); //instruction list of function
				for (int i= ins.size()-1; i>=0; i--) {
					n=ins.get(i);
					for(instructionNode successor : n.successors) { //update LiveOutSet by successor's liveIn
						if (successor!=null) {
							if(successor.in.size()>0) { n.out.addAll(successor.in);}
						}
						
					}
					n.in.addAll(n.out);
					n.in.removeAll(n.writeTemps);
					n.in.addAll(n.readTemps);
				}
				
				updated=false;
				
				for (int i= ins.size()-1; i>=0; i--) { //check if there is any update from the previous iteration
					n=ins.get(i);
					if(!n.in.equals(n.inUpdate)||(!n.out.equals(n.outUpdate))) {
						updated=true; //there is an update, we will perform another iteration
						break;
					}
				}
				
				for (int i= ins.size()-1; i>=0; i--) { //update the "check update" sets
					n=ins.get(i);
					n.inUpdate.clear();; //delete all items 
					n.inUpdate.addAll(n.in); //add items from the regular LiveIN set
					n.outUpdate.clear(); //delete all items
					n.outUpdate.addAll(n.out);// add items from the regular LiveOUT set
				}
			}

		}
	}
		
	
	
	
	//found how many nodes in the graph
	public void updateTotalNodes() {
		Set<String> funcs=this.funcInstructions.keySet();
		instructionNode n;
		for (String func:funcs) { //for each function
			HashSet<String> allTemps=new HashSet<>(); 
			List<instructionNode> ins=this.funcInstructions.get(func); //instruction list of function
			for (int i= ins.size()-1; i>=0; i--) {
				n=ins.get(i);
				allTemps.addAll(n.readTemps);
				allTemps.addAll(n.writeTemps);
			}
			
			List<String> tempList= new ArrayList<>(allTemps);
			Collections.sort(tempList);
			this.totalNodes.put(func, tempList);
		}
	}
	
	
	
	
	

	
	//create interference graph 
	public interferenceGraph createInteferencrGraph(String funcName,List<String> temps) {
				
		interferenceGraph g=new interferenceGraph(temps.size()); //create a new graph
		
		List<instructionNode> ins=this.funcInstructions.get(funcName);
		instructionNode n;
		for (int i= ins.size()-1; i>=0; i--) { //for each instruction node
			n=ins.get(i);
			for (String temp: n.in) { //find neighbors from "in set" and add edges to the graph 
				for (String temp1 : n.in) {
					if(!temp.equals(temp1)) {
						g.addEdge(temps.indexOf(temp), temps.indexOf(temp1));
					}
				}
			}
			for (String temp: n.out) { //find neighbors from "out set" and add edges to the graph 
				for (String temp1 : n.out) {
					if(!temp.equals(temp1)) {
						g.addEdge(temps.indexOf(temp), temps.indexOf(temp1));
					}
				}
			}
		}
		
		return g; //return the graph
	}
	
	
	public void allocateRegisters() {
		Set<String> funcKeys=this.totalNodes.keySet();
		for (String func : funcKeys) {
			interferenceGraph g=createInteferencrGraph(func, this.totalNodes.get(func));
			Integer[] coloring=g.colorGraph(); //colors[x]=y means temp number x assigned to register y
			this.coloring.put(func, coloring);
			
		}
	}
	
	
	
	public void finalizeFile(String outputFilename) {
		String inputfilePath="./output/MIPS_DRAFT.txt";
		String line;
		String outputfilePath=outputFilename;
		int lineNum=0;
		
		String temp;
		int inFunc=0;
		String funcName=null;
		try {
			FileReader f=new FileReader(inputfilePath);
			PrintWriter g= new PrintWriter(outputfilePath);
			
			Scanner s= new Scanner(f);
			while (s.hasNextLine()) {
				lineNum++;
				line=s.nextLine();
				if (line.contains("_Prologue")) {
					inFunc=1;
					String line2=line.trim();
					funcName=line2.substring(0, line2.length()-10);
					g.println(line);
					g.flush();
					continue;
				}
				if(line.contains("_Epilogue")&&!(this.instrctions.get(lineNum-1) instanceof jumpNode)) {
					inFunc=0;
					funcName=null;
					g.println(line);
					g.flush();
					continue;
				}
				//else
				if(funcName==null) {
					g.println(line);
					g.flush();
					continue;
				}
				//we are inside a function
				List<String> funcTemps=this.totalNodes.get(funcName);
				Integer[] funcColorTemps=this.coloring.get(funcName);
				if (funcTemps!=null) {
					for (int i= funcTemps.size()-1; i>=0; i--) {
						temp=funcTemps.get(i);
						String finalTemp=String.format("\\$t%d", funcColorTemps[i]);
						line=line.replaceAll(temp, finalTemp);

						
					}
				}

				
				g.println(line);
				g.flush();
				
			}
			s.close();
			f.close();
			g.close();
		}
		
		catch(Exception e){
			e.printStackTrace();
			
		}

	}
		
	
	
	
}



