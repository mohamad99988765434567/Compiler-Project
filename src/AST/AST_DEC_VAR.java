package AST;

import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_DEC_VAR extends AST_DEC {
	
	public AST_VAR_DEC varDec;
	
	public AST_DEC_VAR(AST_VAR_DEC varDec, int lineNum) {
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
		this.lineNum = lineNum;
		
	}

	/***********************************************/
	/* The default message for a dec var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC VAR AST NODE */
		/************************************/
		System.out.print("AST NODE DEC VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT varDec ... */
		/*****************************/
		if (varDec != null) varDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		return varDec.SemantMe();
	}
	
	public TEMP IRme() {
		return varDec.IRme();
	}
	
	
	
	
	
}
