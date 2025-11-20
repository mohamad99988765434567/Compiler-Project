package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE_ID_LIST extends AST_Node{
	
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_TYPE headType;
	public String headIDName;
	public AST_TYPE_ID_LIST tail;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE_ID_LIST(AST_TYPE headType, String headName ,AST_TYPE_ID_LIST tail, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== typeIDList -> type ID COMMA typeIDList\n");
		if (tail == null) System.out.print("====================== typeIDList -> type ID \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.headType=headType;
		this.headIDName=headName;
		this.tail=tail;
		this.lineNum=lineNum;
	}
	
	/******************************************************/
	/* The printing message for a type ID list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST TYPE ID LIST */
		/**************************************/
		System.out.print("AST TYPE ID LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (headType != null) headType.PrintMe();
		if (tail!=null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"TYPE\nID\nLIST\n"+headIDName);
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (headType != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,headType.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
	
	public TYPE_LIST SemantMe() throws SemantErrorException
	{
		
		String errMessage;
		TYPE t = SYMBOL_TABLE.getInstance().find(headType.name);
		
		if (t== null) 
		{
			
			errMessage=String.format("non existing type\n",headType.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		/*******************************************************/
		/* Enter var with name=name and type=t to symbol table */
		/*******************************************************/
		//SYMBOL_TABLE.getInstance().enter(headIDName,t);
		
		
		if (tail==null) {
			return new TYPE_LIST(t,null);
		}
		
		else {
			return  new TYPE_LIST(t,tail.SemantMe());

			
		}
		
	}
	
}
