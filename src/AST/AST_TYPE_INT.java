package AST;

public class AST_TYPE_INT extends AST_TYPE {
	
	
	public AST_TYPE_INT() {
		
		super("int");
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type -> TYPE_INT\n");
		
		
		
		
	}

	/***********************************************/
	/* The default message for a type int AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = TYPE INT AST NODE */
		/************************************/
		System.out.print("AST NODE TYPE INT\n");

		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE\nINT");

			
	}



}
