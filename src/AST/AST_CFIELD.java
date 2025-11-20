package AST;

import TYPES.*;
import IR.*;
import TEMP.*;

public abstract class AST_CFIELD extends AST_Node{
	
	public TYPE SemantMe(TYPE_CLASS typeClass) throws SemantErrorException{
		return null;
	}
	
	
	public TEMP IRme(TYPE_CLASS c) {
		return null;
	}
	

}
