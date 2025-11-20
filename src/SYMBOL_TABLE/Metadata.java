package SYMBOL_TABLE;

public class Metadata {
	
	
	/*type: 0 - global ????
	 * 		1 - parameter
	 * 		2 - local
	 * 		3 - class field ???? */
	
	public int type;
	public int offset;
	
	public Metadata(int type, int offset) {
		this.type=type;
		this.offset=offset;
		
	}
}	
