package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import IR.*;

public abstract class AST_VAR extends AST_Node
{
	Metadata metadata;
	
	public TYPE SemantMe() throws SemantErrorException
	{
		return null;
	}
	
	public TEMP IRme() {
		return null;
	}
	
	public TEMP IRmeWrite(TEMP t) {
		return null;
	}
}
