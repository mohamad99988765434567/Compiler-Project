package AST;

import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_DEC_FUNC extends AST_DEC {
	
	public AST_FUNC_DEC funcDec;
	
	public AST_DEC_FUNC(AST_FUNC_DEC funcDec, int lineNum) {
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcDec = funcDec;
		this.lineNum=lineNum;
		
	}

	/***********************************************/
	/* The default message for a dec func AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC FUNC AST NODE */
		/************************************/
		System.out.print("AST NODE DEC FUNC\n");

		/*****************************/
		/* RECURSIVELY PRINT funcDec ... */
		/*****************************/
		if (funcDec != null) funcDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nFUNC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (funcDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		return funcDec.SemantMe();
	}
	
	public TEMP IRme() {
		return this.funcDec.IRme();
	}
	
}
