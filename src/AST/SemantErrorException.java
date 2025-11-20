package AST;

public class SemantErrorException extends Exception {
	
	public String message;
	public int lineNum;
	
	public SemantErrorException(String message, int lineNum) {
		super(String.format(">> ERROR(%d) %s", lineNum, message));
		this.lineNum=lineNum;
	}
	


}
