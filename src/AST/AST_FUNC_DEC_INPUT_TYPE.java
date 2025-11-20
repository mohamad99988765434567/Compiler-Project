package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

import java.util.HashMap;
import java.util.Map;

import IR.*;

public class AST_FUNC_DEC_INPUT_TYPE extends AST_FUNC_DEC {
	
	AST_TYPE_ID_LIST typeList;
	//AST_STMT_LIST stmtList;
	
	
	
	public AST_FUNC_DEC_INPUT_TYPE(AST_TYPE type, String name, AST_TYPE_ID_LIST typeList, AST_STMT_LIST stmtList,int lineNum) {
		super(type, name,lineNum);
		//AST_STMT_LIST tmp;
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID ([type ID [,type ID]) {stmt [stmt]*}; \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.typeList = typeList;
		this.stmtList = stmtList;
	}
	
	
	/***********************************************/
	/* The default message for a FUNC DEC INTPUT TYPE AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = FUNC DEC INTPUT TYPE AST NODE */
		/************************************/
		System.out.print("AST NODE FUNC DEC INTPUT TYPE "+ name +"\n");

		/*****************************/
		/* RECURSIVELY PRINT type... */
		/*****************************/
		if (type != null) type.PrintMe();
		if (typeList!= null) typeList.PrintMe();
		if (stmtList!=null) stmtList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nDEC\nINPUT\nTYPE\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (typeList!= null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeList.SerialNumber);
		if (stmtList!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
			
	}
	public TYPE SemantMe() throws SemantErrorException
	{
		String errMessage;
		TYPE tmp;
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST param_list = null;
		String AST_param_type;
		String AST_param_ID;
		Map<String, TYPE> MyMap = new HashMap<>();
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

		/***************************/
		/* [2] check Input Params is legal + Semant Input Params */
		/***************************/
		for (AST_TYPE_ID_LIST it = typeList; it  != null; it = it.tail)
		{
			AST_param_ID = it.headIDName;
			AST_param_type = it.headType.name;
			/*******************/
			/*  void cant be a type of a param */
			if (AST_param_type.equals("void")) {
				errMessage=String.format("void can't be a type of a paramter\n");
				throw new SemantErrorException(errMessage, this.lineNum);
			
			}
			/*******************/


			/*******************/
			/*  convert AST_param_type to actual TYPE and name it t */
			t = SYMBOL_TABLE.getInstance().findType(AST_param_type);
			/*******************/

			/*******************/
			/*  make sure param type already defined */
			if (t == null)
			{
				errMessage=String.format("type %s was not found\n",AST_param_type);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			/*******************/
			/***************************/
			/* [3.1] make sure NO duplicate ID in the params list */
			if (MyMap.get(AST_param_ID) != null)
			{
				errMessage=String.format("pararmeter name %s was already declared\n",AST_param_ID);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			/***************************/
			MyMap.put(AST_param_ID, t);
			
			
			param_list = new TYPE_LIST(t, param_list);
		}
		
		//reverse
		param_list=reverse(param_list);
		
		
		/*******************/
		/*  create new function type and enter it to outer scope */
		TYPE_FUNCTION func_type = new TYPE_FUNCTION(returnType, name, param_list);
		SYMBOL_TABLE.getInstance().enter(name, func_type);
		/*******************/

		SYMBOL_TABLE.getInstance().beginScope("function", func_type);
		SYMBOL_TABLE.getInstance().enter("$return$", func_type.returnType);
		
		for (AST_TYPE_ID_LIST it = typeList; it  != null; it = it.tail)
		{
			AST_param_ID = it.headIDName;
			AST_param_type = it.headType.name;
			t = SYMBOL_TABLE.getInstance().findType(AST_param_type);
			SYMBOL_TABLE.getInstance().enterParam(AST_param_ID, new TYPE_VAR(AST_param_ID,t));
		}

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
	
	
	public TYPE_LIST reverse(TYPE_LIST l) {
		TYPE_LIST reversed_list=null;
		TYPE t;
		while (l!=null) {
			t=l.head;
			reversed_list=new TYPE_LIST(t,reversed_list);
			l=l.tail;
			
		}
		return reversed_list;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
