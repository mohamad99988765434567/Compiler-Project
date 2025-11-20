package ALLOCATOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class interferenceGraph {
	
	public LinkedHashMap<Integer,HashSet<Integer>> adjMat;
	
	public interferenceGraph(int NumberOfNodes) {
		this.adjMat=new LinkedHashMap<>();
		for (int i=0; i<NumberOfNodes; i++) {
			this.adjMat.put(i, new HashSet<Integer>());
		}	
	}
	
	
	
	public void addEdge(int node, int neighbor) {
		HashSet<Integer> s=this.adjMat.get(node);
		if (!s.contains(neighbor)) {
			s.add(neighbor);
		}
	}
	
	public Integer[] colorGraph(){
		Integer[] coloring =new Integer[this.adjMat.size()]; //index - node's index, item - color's number (0-9)
		LinkedHashMap<Integer,HashSet<Integer>> stack=new LinkedHashMap<>();
		while (this.adjMat.size()>0) {
			int sign=0;
			Set<Integer> nodesLeft=this.adjMat.keySet();
			for(Integer node : nodesLeft) {
				HashSet<Integer> neighbors=this.adjMat.get(node);
				if (neighbors.size()<10) {
					stack.put(node,neighbors); //put in stack
					for (Integer neighbor: neighbors) { //remove edges between node and it's neighbors
						this.adjMat.get(neighbor).remove(node);
					}
					this.adjMat.remove(node); //remove node from graph 
					sign=1;
					break;
				}	
			}
			if (sign==0 && this.adjMat.size()>0) { //we did not picked a node, the graph is not empty
				System.out.println("cannot color graph1\n");
				System.exit(0);
			}
			
			
			
			
		}
		
		//now all nodes are in the stack, we want color the graph
		List<Integer> nodes= new ArrayList<Integer>(stack.keySet());
		Collections.reverse(nodes);
		//let's start pop and color
		for (Integer node : nodes) {
			int[] colors=new int[10]; //everything is zero, if 1 - color is unavailable;
			HashSet<Integer> neighbors=stack.get(node);
			this.adjMat.put(node, neighbors);
			for(Integer neighbor:neighbors) {
				Set<Integer> s=this.adjMat.get(neighbor);
				if (s!=null) { //if neighbor in graph. (i think that this check is unnecessary).
					s.add(node); //add node to it's neighbors
					int color=coloring[neighbor]; //neighbor's color
					colors[color]=1; //mark neighbor color as unavailable
					
				}
			}
			int node_color=-1;
			for (int i=0; i<10;i++) {
				if (colors[i]==0) {
					node_color=i;
					coloring[node]=i;
					break;
				}
			}
			
			if (node_color==-1){
				System.out.println("cannot color graph2\n");
				System.exit(0);
			}
			
			
		}
		
		
		return coloring;

	}
	
	
	
	

}
