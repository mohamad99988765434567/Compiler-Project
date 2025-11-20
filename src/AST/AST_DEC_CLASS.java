package AST;

import TYPES.*;

import TEMP.*;

import IR.*;

public class AST_DEC_CLASS extends AST_DEC {
	
	public AST_CLASS_DEC classDec;
	
	public AST_DEC_CLASS(AST_CLASS_DEC classDec, int lineNum) {
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> classDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.classDec = classDec;
		this.lineNum=lineNum;
		
	}

	/***********************************************/
	/* The default message for a dec class AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC CLASS AST NODE */
		/************************************/
		System.out.print("AST NODE DEC CLASS\n");

		/*****************************/
		/* RECURSIVELY PRINT classDec ... */
		/*****************************/
		if (classDec != null) classDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nCLASS");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (classDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,classDec.SerialNumber);
			
	}
	

	public TYPE SemantMe() throws SemantErrorException {
		
		return classDec.SemantMe();
		
	}
	
	public TEMP IRme() {
		return classDec.IRme();
	}
	
}
