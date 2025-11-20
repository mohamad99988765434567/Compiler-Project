package AST;

import TYPES.*;

import java.util.ArrayList;
import java.util.List;

import IR.*;
import TEMP.*;

public class AST_EXP_VAR_DOT_METHOD extends AST_EXP {
	
	public AST_VAR var;
	public String name;
	public AST_EXP_LIST expList;
	public TYPE_CLASS tclass;
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_EXP_VAR_DOT_METHOD(AST_VAR var, String name, AST_EXP_LIST  expList, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (expList != null) System.out.print("====================== stmt -> var DOT ID LPAREN expList RPAREN\n");
		if (expList == null) System.out.print("====================== stmt -> var DOT ID LPAREN RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.expList=expList;
		this.lineNum=lineNum;
	}

	/*********************************************************/
	/* The printing message for a var dot method expression AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST VAR DOT METHOD EXPRESSION */
		/********************************************/
		System.out.print("AST NODE VAR DOT METHOD EXP\n");
		 
		/***********************************/
		/* RECURSIVELY PRINT var, expList ... */
		/***********************************/
		if (var!= null) var.PrintMe();
		System.out.print("hi adan\n");
		if (expList != null)
		{
			expList.PrintMe();
		}
		else
		{
			System.out.print("hi shaimaa\n");
		}

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (expList == null) AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR.\nMETHOD\n"+name+"()\n");
		
		if (expList != null) AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"VAR.\nMETHOD\n"+name+"\n(exp)\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var!= null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
		
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		
		String errMessage;
		
		TYPE varType = this.var.SemantMe(); //if there is an error, it will be here.
		
		
		if (!(varType instanceof TYPE_CLASS)) {
			errMessage = String.format("variable's type is not class, so method %s cannot be called", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
			//ERROR
		
		}
		
		this.tclass = (TYPE_CLASS)varType; //add to assignment 4
		
		//variable exists, and it's type is class ! (AST_VAR WILL VALIDATE OTHRT THINGS...)
		
		//check if the method is defined in the current class or in it's fathers..
		TYPE methodType = searchInClassHierarchy(this.name,(TYPE_CLASS)varType);
		
		//if it's not defined, or it's not a function - we have an ERROR.
		if (methodType == null || !(methodType instanceof TYPE_FUNCTION)) {
			errMessage = String.format("method %s does not exist\n", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		//validate parameter
		TYPE_LIST paramList = ((TYPE_FUNCTION)methodType).params;
		
		TYPE t;
		
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
		
		
	
		if (paramList != null){ //error : wrong number of parameters
			errMessage=String.format("%s paramList is greater then the caller list", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		//if we got here, everything is fine, return method's return Type
		return ((TYPE_FUNCTION)methodType).returnType; //success
	}
	
	
	public TEMP IRme() {
		TEMP t1 = this.var.IRme(); //the class object
		int methodOffset = this.tclass.methodIndex.indexOf(this.name);
		List<TEMP> params = new ArrayList<TEMP>();
		if (this.expList!=null) {
			AST_EXP_LIST r = reverse(expList);
			for (AST_EXP_LIST it = r; it != null; it = it.tail){
				TEMP t2=it.head.IRme();
				params.add(t2);
			}
		}
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_virtual_call(dst,t1, methodOffset, params));
		
		return dst;
	}
}
