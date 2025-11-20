package AST;

import IR.*;
import TEMP.*;
import TYPES.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	public TYPE SemantMe  () throws SemantErrorException{
		
		String errMessage;
		

		TYPE exp_type = exp.SemantMe();
		TYPE varType = var.SemantMe();
		if ( !(exp_type.name.equals(varType.name)) ) {
			if(!TYPE.check_unequal_cases(varType, exp_type)){
				errMessage=String.format("Illegal Assignmet between types: %s , %s", varType.name, exp_type.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
		}
		return null;
	}
	
	
	public TEMP IRme()
	{
		//this.var.IRme(); //get dst
		if (this.var instanceof AST_VAR_SUBSCRIPT) {
			AST_VAR_SUBSCRIPT x=(AST_VAR_SUBSCRIPT)this.var;
			TEMP offset=x.getOffset();
			TEMP value = exp.IRme(); //get value
			x.IRmeWrite(value, offset);
		}
		else {
			TEMP value = exp.IRme(); //get value
			this.var.IRmeWrite(value);
		}
		
		
		return null;
	}
	
	
}
