package AST;

public class AST_TYPE_ID extends AST_TYPE {
	
	
	public AST_TYPE_ID(String name, int lineNum){
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		super(name);
		this.lineNum=lineNum;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type -> TYPE_ID\n");
		
		

		
	}

	/***********************************************/
	/* The default message for a type id AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = TYPE ID AST NODE */
		/************************************/
		System.out.print("AST NODE TYPE ID "+name+"\n");

		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE\nID("+name+")");

			
	}
	
	/*public TYPE SemantMe() {
		TYPE t = SYMBOL_TABLE.getInstance().find(name);
		if (t == null)
		{
			System.out.format(">> ERROR non existing type %s\n");
			//error handling
			System.exit(0);
		}
		
		return t;
	}*/
	
	
	

}
