package AST;

import TYPES.*;

import TEMP.*;
import IR.*;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}
	
	public TYPE SemantMe() throws SemantErrorException{
		return null;
	}
	
	public TEMP IRme() {
		return null;
	}
}
