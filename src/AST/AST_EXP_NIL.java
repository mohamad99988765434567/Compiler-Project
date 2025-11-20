package AST;

import TYPES.*;

import IR.*;
import TEMP.*;


public class AST_EXP_NIL extends AST_EXP{
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_NIL(int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> NIL\n");
		
		this.lineNum=lineNum;
	}

	/************************************************/
	/* The printing message for an NIL EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NIL EXP */
		/*******************************/
		System.out.format("AST NODE NIL\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
	}
	
	public TYPE SemantMe()
	{
		return TYPE_NIL.getInstance();
	}
	
	
	
	public TEMP IRme() {
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, 0));
		return dst;
	}
	
	
	
	public TEMP IRme(String name) {
		IR.getInstance().Add_IRcommand(new IRcommand_global_var(name, 0));
		return null;
	}
	
}

