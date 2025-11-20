package AST;

import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(int value, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> INT( %d )\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
		this.lineNum=lineNum;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE INT( %d )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("INT(%d)",value));
	}
	
	
	public TYPE SemantMe()
	{
		return TYPE_INT.getInstance();
	}
	
	
	public TEMP IRme() {
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, this.value));
		return dst;
	}
	
	
	public TEMP IRme(String name) {
		IR.getInstance().Add_IRcommand(new IRcommand_global_var(name, this.value));
		
		
		return null;
	}
	
	
	
	
	
	
	
	
	
}
