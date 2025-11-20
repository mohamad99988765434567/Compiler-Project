package AST;

import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_DEC_LIST extends AST_Node {
	
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC head;
	public AST_DEC_LIST tail;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== dec -> dec decs\n");
		if (tail == null) System.out.print("====================== dec -> dec      \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head=head;
		this.tail=tail;
		this.lineNum=lineNum;
		
	}
	
	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE DEC LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	 
	public TYPE SemantMe() throws SemantErrorException {
		
		
		head.SemantMe();
		
		
		if (tail!=null) {
			tail.SemantMe();
		}
		
		return null;
	}
	
	
	public TEMP IRme(){
		
		
		head.IRme();
		
		
		if (tail!=null) {
			tail.IRme();
		}
		
		return null;
	}
} 
	
	
	

