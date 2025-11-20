package ALLOCATOR;

public class jumpNode extends instructionNode {
	
	String dstLabel;
	
	public jumpNode(String type, int isLabel, String dstLabel) {
		super(type, isLabel);
		this.dstLabel=dstLabel;
	}
}
