package AST;

//import TYPES.*;

//import java.util.LinkedHashMap;

//import SYMBOL_TABLE.*;

public class AST_CLASS_DEC_SIMPLE extends AST_CLASS_DEC{
	
	
	
	public AST_CLASS_DEC_SIMPLE(String name, AST_CFIELD_LIST cFieldList, int lineNum){
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== classDec -> CLASS ID { cFieldList:cfl } \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.className = name;
		this.cFieldList=cFieldList;
		this.fatherName=null;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for a CLASS DEC SIMPLE AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = CLASS DEC SIMPLE AST NODE */
		/************************************/
		System.out.print("AST NODE CLASS DEC SIMPLE "+ className +"\n");

		/*****************************/
		/* RECURSIVELY PRINT cFieldList ... */
		/*****************************/
		if (cFieldList != null) cFieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC\nSIMPLE\n"+className);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cFieldList.SerialNumber);
			
	}
	
	
	
	
	
}
