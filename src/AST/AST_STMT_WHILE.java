package AST;

import TYPES.*;
import IR.IR;
import IR.IRcommand;
import IR.IRcommand_Jump_If_Eq_To_Zero;
import IR.IRcommand_Jump_Label;
import IR.IRcommand_Label;
import SYMBOL_TABLE.*;
import TEMP.TEMP;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;  

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE (exp) {stmt [stmt]*}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for a while statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST WHILE STATEMENT */
		/********************************************/
		System.out.print("AST NODE WHILE STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT cond,body ... */
		/***********************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"WHILE\n(cond)\n{body}");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
	
	
	public TYPE SemantMe() throws SemantErrorException{
		
		
		String errMessage;
		
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe() != TYPE_INT.getInstance())
		{
			errMessage=String.format("condition inside WHILE is not integral\n");
			throw new SemantErrorException(errMessage, this.lineNum);
			
		}
		
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope("while", null);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe();

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
	
	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("end");
		String label_start = IRcommand.getFreshLabel("start");

		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_start));

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRme();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(label_start));

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));

		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}
		
		
		
	
}