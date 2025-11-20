package AST;
import TEMP.*;
import TYPES.*;

public abstract class AST_EXP extends AST_Node
{
	public TYPE SemantMe() throws SemantErrorException
	{
		return null;
	}
	
	public TEMP IRme()
	{
		return null;
	}
	
	public TEMP IRme(String name) {return null;}
	
	
}