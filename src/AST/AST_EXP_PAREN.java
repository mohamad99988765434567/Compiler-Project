package AST;
import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_EXP_PAREN extends AST_EXP {
	
	public AST_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_PAREN(AST_EXP exp, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> LPAREN exp RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for an exp paren AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP PAREN AST NODE */
		/************************************/
		System.out.print("AST NODE EXP IN PAREN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"(EXP)\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}
	
	
	public TYPE SemantMe() throws SemantErrorException {
		if (exp!=null) {
			return exp.SemantMe();
		}
		
		//error handling
		return null;
	}
	
	
	public TEMP IRme() {
		return this.exp.IRme();
	}
}
