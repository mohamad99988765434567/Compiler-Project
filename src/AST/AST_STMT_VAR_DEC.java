package AST;
import TYPES.*;
import TEMP.*;


public class AST_STMT_VAR_DEC extends AST_STMT {
	
	public AST_VAR_DEC varDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_VAR_DEC(AST_VAR_DEC varDec, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
		this.lineNum = lineNum;
	}

	/*********************************************************/
	/* The printing message for a var declaration statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST VAR DEC STATEMENT */
		/********************************************/
		System.out.print("AST NODE VAR DEC STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT varDec ... */
		/***********************************/
		if (varDec != null) varDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nVAR\nDEC");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		this.varDec.SemantMe();
		return null; //OK
	}
	
	public TEMP IRme() {
		this.varDec.IRme();
		return null;
	}

}
