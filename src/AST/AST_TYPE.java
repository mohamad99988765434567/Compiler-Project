package AST;

public abstract class AST_TYPE extends AST_Node {
	
	String name;
	public AST_TYPE(String type) {
		this.name=type;
	}
	/*public AST_TYPE(String name) {
		
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.name=name;
	}*/
	
	/*public TYPE SemantMe() {
		
		return null;
		
	}*/
}
