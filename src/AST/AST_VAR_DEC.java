package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import TEMP.*;

public abstract class AST_VAR_DEC extends AST_Node{
	
	AST_TYPE type;
	String name;
	Metadata metadata;
	
	public AST_VAR_DEC(AST_TYPE type, String name, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
		this.lineNum=lineNum;
	}
	
	public TYPE SemantMe() throws SemantErrorException{
		return null;
	}
	
	public TEMP IRme() {
		return null;
	}
	
	public TEMP IRme(TYPE_CLASS t) {
		return null;
	}
	
	
}
