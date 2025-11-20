package AST;

import TYPES.TYPE;

public class AST_DEC_ARRAY extends AST_DEC {
	
	public AST_ARRAY_DEC arrayDec;
	
	public AST_DEC_ARRAY(AST_ARRAY_DEC arrayDec, int lineNum) {
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> arrayDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.arrayDec = arrayDec;
		this.lineNum=lineNum;
		
	}

	/***********************************************/
	/* The default message for a dec array AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC ARRAY AST NODE */
		/************************************/
		System.out.print("AST NODE DEC ARRAY\n");

		/*****************************/
		/* RECURSIVELY PRINT arrayDec ... */
		/*****************************/
		if (arrayDec != null) arrayDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nARRAY");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arrayDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,arrayDec.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		return arrayDec.SemantMe();
	}
	
	
	
}
