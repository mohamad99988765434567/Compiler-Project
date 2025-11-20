package AST;
import TYPES.*;
import TEMP.*;
import IR.*;

public abstract class AST_DEC extends AST_Node {
	
	/*********************************************************/
	/* The default message for an unknown AST declaration node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST DECLARATION NODE");
	}
	
	public TYPE SemantMe() throws SemantErrorException
	{
		return null;
	}
	
	public TEMP IRme() {
		return null;
	}
}

