package AST;
import TEMP.*;
import IR.*;
import TYPES.*;

public abstract class AST_FUNC_DEC extends AST_Node{

	AST_TYPE type;
	String name;
	
	AST_STMT_LIST stmtList;
	
	int numOfParams;
	int numOflocals;
	
	String className;
	
	
	public AST_FUNC_DEC(AST_TYPE type, String name, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
		this.lineNum=lineNum;
		this.className=null;
	}
	
	public TYPE SemantMe() throws SemantErrorException{
		return null;
	}
	
	public TEMP IRme() {
		
		String funcName=this.name;
		
		if (this.className!=null) {
			funcName=this.className+"_"+this.name;
		}
		
		if(this.name.equals("main")) {
			funcName="user_main";
		}
		
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(funcName));
		IR.
		getInstance().
		Add_IRcommand(new IRCommand_Func_Prologue(funcName,this.numOflocals)); //numOfParams=0
		
		if (stmtList != null) stmtList.IRme();
		
		
		
		
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Func_Epilogue(funcName));
		
		return null;
	}
}
