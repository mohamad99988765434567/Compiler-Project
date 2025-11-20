package AST;

public class AST_TYPE_VOID extends AST_TYPE {
	
	public AST_TYPE_VOID() {
		
		super("void");
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type -> TYPE_VOID\n");
		
		
		
	}

	/***********************************************/
	/* The default message for a type void AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = TYPE VOID AST NODE */
		/************************************/
		System.out.print("AST NODE TYPE VOID\n");

		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE\nVOID");

			
	}
}
