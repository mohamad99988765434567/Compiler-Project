package AST;

public class AST_TYPE_STRING extends AST_TYPE{
	
	public AST_TYPE_STRING() {
		
		super("string");
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type -> TYPE_STRING\n");

		
	}

	/***********************************************/
	/* The default message for a type string AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = TYPE STRING AST NODE */
		/************************************/
		System.out.print("AST NODE TYPE STRING\n");

		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE\nSTRING");

			
	}

}
