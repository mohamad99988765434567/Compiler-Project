package AST;
import TYPES.*;

import java.util.ArrayList;
import java.util.List;

import IR.IR;
import IR.IRcommand_PrintInt;
import IR.IRcommand_PrintString;
import IR.IRcommand_callFunc;
import IR.IRcommand_jal;
import IR.IRcommand_load_return_value;
import IR.IRcommand_pop_stack;
import IR.IRcommand_push_arg;
import SYMBOL_TABLE.*;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;

public class AST_STMT_FUNC extends AST_STMT {
	
	public String name;
	public AST_EXP_LIST expList;
	public TYPE_CLASS my_class;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_FUNC(String name, AST_EXP_LIST  expList, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (expList != null) System.out.print("====================== stmt -> ID LPAREN expList RPAREN SEMICOLON\n");
		if (expList == null) System.out.print("====================== stmt -> ID LPAREN RPAREN SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.expList=expList;
		this.lineNum=lineNum;
		this.my_class=null;
	}

	/*********************************************************/
	/* The printing message for an func statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST FUNC STATEMENT */
		/********************************************/
		System.out.print("AST NODE FUNC STMT\n");
		 
		/***********************************/
		/* RECURSIVELY PRINT expLists ... */
		/***********************************/
		if (expList != null) expList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (expList == null) AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\n"+name+"();\n");
		
		if (expList != null) AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"FUNC\n"+name+"\n(expList);\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
		
	}
	/****************************************/
	/* addition for hw3  */
	/****************************************/

	public TYPE SemantMe() throws SemantErrorException {
		
		String errMessage;
		
		TYPE toReturn;
		TYPE t;
		TYPE_LIST paramList = null;
		/*******************/
		/* [0] make sure function already defined */
		/*******************/
		
		TYPE_CLASS typeclass = SYMBOL_TABLE.getInstance().getCurrClass();
		my_class = typeclass;
		if (typeclass != null) { //we are in a class scope

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
				errMessage=String.format("%s caller list is greater then the paramList\n", this.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			t = it.head.SemantMe();
			
			
			if (!(paramList.head.name.equals(t.name))) {
				if (!TYPE.check_unequal_cases(paramList.head, t)){
					//error
					errMessage=String.format("function %s was called with wrong parameters\n", this.name);
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
		
		if (my_class != null) //we are in a class scope
		{
			String new_name = my_class.name + "_" + this.name;
			IR.getInstance().Add_IRcommand(new IRcommand_callFunc(null, new_name, params));
			//System.out.format("in AST_STMT_FUNC: %s\n", this.name);

		}
		else
		{
			IR.getInstance().Add_IRcommand(new IRcommand_callFunc(null, this.name, params));
		}

		return null;
	}
	
	
	


}
