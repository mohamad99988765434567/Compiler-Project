package AST;

public class AST_CLASS_DEC_EXTENDS extends AST_CLASS_DEC{
	
	
	
	public AST_CLASS_DEC_EXTENDS(String name1,String name2, AST_CFIELD_LIST cFieldList, int lineNum){
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList:cfl } \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.className = name1;
		this.fatherName = name2;
		this.cFieldList=cFieldList;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for a CLASS DEC EXTENDS AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = CLASS DEC EXTENDS AST NODE */
		/************************************/
		System.out.print("AST NODE CLASS DEC EXTENDS "+ className +" EXTENDS "+fatherName +"\n");

		/*****************************/
		/* RECURSIVELY PRINT cFieldList ... */
		/*****************************/
		if (cFieldList != null) cFieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC\nEXTENDS\n"+className+"\nEXTENDS\n"+fatherName);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cFieldList.SerialNumber);
			
	}
}
