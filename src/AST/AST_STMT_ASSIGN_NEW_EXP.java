package AST;

import TEMP.TEMP;
import TYPES.*;


public class AST_STMT_ASSIGN_NEW_EXP extends AST_STMT {
	
	/***************/
	/*  var := newExp */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP newExp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW_EXP(AST_VAR var,AST_NEW_EXP newExp,int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.newExp = newExp;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for an assign new statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT NEW STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN NEW STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (newExp != null) newExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := new right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}
	public TYPE SemantMe() throws SemantErrorException{
		
		String errMessage;

		TYPE newExpType = newExp.SemantMe();
		
		TYPE varType = var.SemantMe();
		
		
		if (newExpType instanceof TYPE_ARR) {
			
			TYPE_ARR expArrType=(TYPE_ARR)newExpType;
			
			if (!(varType instanceof TYPE_ARR )) {
				errMessage=String.format("Illegal Assignmet between types: %s , array of %s\n", varType.name, expArrType.typeArray.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			TYPE_ARR varArrType=(TYPE_ARR)varType;
			
			if (!(varArrType.typeArray.name.equals(expArrType.typeArray.name))) {
				if(!TYPE.check_unequal_cases(varArrType.typeArray, expArrType.typeArray)){
					errMessage=String.format("Illegal Assignmet between types: %s array, %s array\n", varArrType.typeArray.name, expArrType.typeArray.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
		}
		
		
		
		else { //newExpType is TYPE_CLASS
			if (!(varType instanceof TYPE_CLASS)) {
				errMessage=String.format("Illegal Assignmet between types: %s , %s", varType.name, newExpType.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			if ( !(newExpType.name.equals(varType.name)) ) {
				if(!TYPE.check_unequal_cases(varType, newExpType)){
					errMessage=String.format("Illegal Assignmet between types: %s , %s", varType.name, newExpType.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
			
		}
			
		return null;
	}
	
	
	public TEMP IRme()
	{
		
		//TEMP value = newExp.IRme();
		//this.var.IRmeWrite(value);
		
		
		//this.var.IRme(); //get dst
		if (this.var instanceof AST_VAR_SUBSCRIPT) {
			AST_VAR_SUBSCRIPT x=(AST_VAR_SUBSCRIPT)this.var;
			TEMP offset=x.getOffset();
			TEMP value = newExp.IRme(); //get value
			x.IRmeWrite(value, offset);
		}
		
		
		else {
			TEMP value = newExp.IRme(); //get value
			this.var.IRmeWrite(value);
		}
		
		
		return null;
	}

}
