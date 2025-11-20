package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import TEMP.*;

import java.util.ArrayList;
import java.util.List;

import IR.*;

public class AST_EXP_FUNC extends AST_EXP {
	
	public String name;
	public AST_EXP_LIST expList;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_EXP_FUNC(String name, AST_EXP_LIST  expList, int lineNum) 
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (expList != null) System.out.print("====================== stmt -> ID LPAREN expList RPAREN\n");
		if (expList == null) System.out.print("====================== stmt -> ID LPAREN RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.expList=expList;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for a func expression AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST FUNC EXPRESSION */
		/********************************************/
		System.out.print("AST NODE FUNC EXP\n");
		 
		/***********************************/
		/* RECURSIVELY PRINT expList ... */
		/***********************************/
		if (expList != null) expList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (expList == null) AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\n"+name+"()\n");
		
		if (expList != null) AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"FUNC\n"+name+"\n(expList)\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
		
	}
	public TYPE SemantMe() throws SemantErrorException{
		
		String errMessage;
		
		TYPE toReturn;
		TYPE t;
		TYPE_LIST paramList = null;
		/*******************/
		/* [0] make sure function already defined */
		/*******************/
		
		TYPE_CLASS typeclass= SYMBOL_TABLE.getInstance().getCurrClass();
		
		if (typeclass!=null) { //we are in a class scope
			t=SYMBOL_TABLE.getInstance().findInClassScope(name);
			
			if (t!=null){
				if (!(t instanceof TYPE_FUNCTION)) {
					errMessage=String.format("function %s does not exist", this.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
			
			else { //t=null
				t= searchInClassHierarchy(this.name, typeclass.father);
				if (t!=null) {
					if(!(t instanceof TYPE_FUNCTION)) {
						errMessage=String.format("function %s does not exist", this.name);
						throw new SemantErrorException(errMessage, this.lineNum);
					}
				}
				
				else { //t=null
					t=SYMBOL_TABLE.getInstance().findInGlobalScope(name);
					if (t==null || !(t instanceof TYPE_FUNCTION)) { //no function
						errMessage=String.format("function %s does not exist", this.name);
						throw new SemantErrorException(errMessage, this.lineNum);
					}
				}
				
			}
			
		}
		
		
		else { //typeclass=null - we are in global scope
			t=SYMBOL_TABLE.getInstance().find(name);
			if (t==null || !(t instanceof TYPE_FUNCTION)) {
				errMessage=String.format("function %s does not exist", this.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
		}
		
		
		
		
		// if we got here, function exists
		/*******************/
		/* [1] validate the parameters */
		/*******************/
		toReturn = ((TYPE_FUNCTION)t).returnType;
		paramList = ((TYPE_FUNCTION)t).params;
		
		for (AST_EXP_LIST it = expList; it != null; it = it.tail)
		{
			if (paramList == null){
				errMessage=String.format("%s caller list is greater then the paramList", this.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			t = it.head.SemantMe();

			if (!(paramList.head.name.equals(t.name))) {
				if (!TYPE.check_unequal_cases(paramList.head, t)){
					//error
					errMessage=String.format("function %s was called with wrong parameters", this.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
			paramList = paramList.tail;
		}
		if (paramList != null){
			errMessage=String.format("%s paramList is greater then the caller list", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}

		return toReturn;
	}
	
	
	
	public TEMP IRme() {
		
		//AST_EXP_LIST r = reverse(expList); //reverse the "arguments list" - the last argument will be the first to push.
		
		
		
		List<TEMP> params=new ArrayList<TEMP>();
		for (AST_EXP_LIST it = expList; it != null; it = it.tail){
			TEMP t1=it.head.IRme();
			params.add(0,t1);
			
		}
		
		
		if(this.name.equals("PrintInt")) {
			IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(params.get(0)));
			return null;
		}
		
		if(this.name.equals("PrintString")) {
			IR.getInstance().Add_IRcommand(new IRcommand_PrintString(params.get(0)));
			return null;
		}
		
		TEMP dst=TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_callFunc(dst, this.name, params));
		return dst;
		
	}
	
	
	

	

}
