package ALLOCATOR;

import java.util.HashSet;
import java.util.Set;


public class instructionNode {
	
	public String type;
	public Set<String> readTemps;
	public Set<String> writeTemps;
	public Set<String> in;
	public Set<String> out;
	public Set<String> inUpdate;
	public Set<String> outUpdate;
	public Set<instructionNode> successors;
	public int isLabel=0;
	
	
	public instructionNode(String type, int isLabel) {
		this.type=type;
		this.readTemps=new HashSet<>();
		this.writeTemps= new HashSet<>();
		this.in=new HashSet<>();
		this.out=new HashSet<>();
		this.inUpdate=new HashSet<>();
		this.outUpdate=new HashSet<>();
		this.successors= new HashSet<>();
		this.isLabel=isLabel;
	}
	
	
	
	
}
