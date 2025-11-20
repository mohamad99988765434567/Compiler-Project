package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_RETURN extends AST_STMT{
	
	
	public AST_EXP exp;
	public String funcName=null;
	public String className=null;
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp!=null) System.out.print("====================== stmt -> RETURN SEMICOLON\n");
		if (exp==null) System.out.print("====================== stmt -> RETURN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for an return statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST RETURN STATEMENT */
		/********************************************/
		System.out.print("AST NODE RETURN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT EXP ... */
		/***********************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	
	
	public TYPE SemantMe() throws SemantErrorException{
		
		String errMessage;
		
		TYPE returnFuncType = SYMBOL_TABLE.getInstance().find("$return$");
		
		if ((returnFuncType==null)){ //I DON'T KNOW IF IS IT A POSSIBLE CASE...BUT IT'S OK.
			errMessage=String.format("function has no return type\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		//if return type is void, but it's not an empty return statement (exp!=null) -it's an ERROR
		if (returnFuncType==TYPE_VOID.getInstance()&& this.exp!=null) {
			errMessage=String.format("return type is void - RETURN STMT MUST BE EMPTY\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		//if return type isn't void but it's an empty return statement
		if ((returnFuncType!=TYPE_VOID.getInstance())&&this.exp==null) {
			errMessage=String.format("return type is not void - RETURN STMT CANNOT BE EMPTY\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		
		if (this.exp!=null) {
			TYPE t = exp.SemantMe();
			if ( !(returnFuncType.name.equals(t.name)) ) {
				if(!TYPE.check_unequal_cases(returnFuncType, t)){
					errMessage=String.format("can't return %s type. return type must be %s\n",t.name,returnFuncType.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
		
		}
		
		//add it for IRme()
		TYPE_FUNCTION f= SYMBOL_TABLE.getInstance().getCurrFunc();
		if (f!=null) {
			this.funcName=f.name;
		}
		
		TYPE_CLASS c=SYMBOL_TABLE.getInstance().getCurrClass();
		if (c!=null) {
			this.className=c.name;
		}
		
		
		return null;
	}
	
	public TEMP IRme() {
		
		TEMP t1 = null;
		
		if (this.exp!=null) {
			t1=exp.IRme();
			
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Func_Return(t1));
		}
		
		String labelPrefix=this.funcName;
		if (this.className!=null) {
			labelPrefix=this.className+"_"+this.funcName;
		}
		
		if(this.funcName.equals("main")) {
			labelPrefix="user_main";
		}
		
		
		IR.
		getInstance().Add_IRcommand(new IRcommand_Jump_Label(labelPrefix+"_Epilogue")); //jump to epilogue
		
		
		
		
		return null;
		
	}

}
