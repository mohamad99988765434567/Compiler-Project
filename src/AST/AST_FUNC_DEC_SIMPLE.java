package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

import TEMP.*;
import IR.*;
//import java.util.HashMap;
//import java.util.Map;

// a function in class, with no parameters
public class AST_FUNC_DEC_SIMPLE extends AST_FUNC_DEC {
	
	//AST_STMT_LIST stmtList;
	
	
	
	public AST_FUNC_DEC_SIMPLE(AST_TYPE type, String name, AST_STMT_LIST stmtList, int lineNum){
		
		super(type,name,lineNum);
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID () {stmt [stmt]*}; \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.stmtList = stmtList;
	}
	
	/***********************************************/
	/* The default message for a FUNC DEC SIMPLE AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = FUNC DEC SIMPLE AST NODE */
		/************************************/
		System.out.print("AST NODE FUNC DEC SIMPLE "+ name +"\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if (stmtList!=null) stmtList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nDEC\nSIMPLE\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (stmtList!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
			
	}

	public TYPE SemantMe () throws SemantErrorException
	{
		
		String errMessage;
		TYPE tmp;
		//TYPE t;
		TYPE returnType = null;
		TYPE_LIST param_list = null;
		//String AST_param_type;
		//String AST_param_ID;
		//Map<String, TYPE> MyMap = new HashMap<>();
		/*******************/
		/* [0] check if ID is already taken at the outer scope */
		/*******************/
		tmp = SYMBOL_TABLE.getInstance().findInCurrScope(name);
		if (tmp != null)
		{
			errMessage=String.format("the name %s is already taken \n", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		/*******************/
		/* [0] make sure return type already defined */
		/*******************/
		returnType = SYMBOL_TABLE.getInstance().findReturnType(type.name);
		if (returnType == null)
		{
			errMessage=String.format("non existing return type\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		/*******************/
		/*  create new function type and enter it to outter scope */
		TYPE_FUNCTION func_type = new TYPE_FUNCTION(returnType, name, param_list);
		SYMBOL_TABLE.getInstance().enter(name, func_type);
		/*******************/

		SYMBOL_TABLE.getInstance().beginScope("function", func_type);
		SYMBOL_TABLE.getInstance().enter("$return$", func_type.returnType);

		/*******************/
		/* [3] Semant Body */
		/*******************/
		stmtList.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
		
		//save meta data
		this.numOfParams=func_type.totalParams;
		this.numOflocals=func_type.totalLocals;
		
		//is it a class method ? check it, and if it does, save the class name!
		TYPE_CLASS c=SYMBOL_TABLE.getInstance().getCurrClass();
		if(c!=null) {
			this.className=c.name;
		}
		
		
		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return func_type;
	}
	
	

}
