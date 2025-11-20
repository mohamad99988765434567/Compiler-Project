package AST;

import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_CFIELD_FUNC_DEC extends AST_CFIELD {
	
	AST_FUNC_DEC funcDec;
	
	public AST_CFIELD_FUNC_DEC(AST_FUNC_DEC funcDec, int lineNum){
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> funcDec\n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcDec = funcDec;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for a CFIELD FUNC DEC AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = CFIELD FUNC DEC AST NODE */
		/************************************/
		System.out.print("AST NODE CFIELD FUNC DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT funcDec ... */
		/*****************************/
		if (funcDec != null) funcDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nFUNC\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (funcDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
			
	}
	
	
	public TYPE	SemantMe(TYPE_CLASS typeClass) throws SemantErrorException {
		
		
		String errMessage;
		
		TYPE t=funcDec.SemantMe();
				
		if (typeClass.father==null) { //just put it in the dataMembers and continue
			//CHECK HERE OR IN SEMANT ME if already in scope +ERROR handling????
			//typeClass.addToMap(t.name, t);
			return t; //OK
		}
		//check if exists in father's class
		String funcName=((TYPE_FUNCTION)t).name; //take function name
		TYPE ft=searchInClassHierarchy(funcName,typeClass.father);  //START FROM DAD 
		if (ft!=null) { //if exists
			
			if (!(ft instanceof TYPE_FUNCTION)) { //if it's not a fucntion's name - it's an error (table 2.6)
				errMessage=String.format("%s function's name is already taken in father class, "
					+ "and it's not a function\n",funcName);
				throw new SemantErrorException(errMessage, lineNum);
			}
			
			
			//we have two functions with the same name. let's make sure that it's an overriding.
			TYPE_FUNCTION newFunc=(TYPE_FUNCTION)t;
			TYPE_FUNCTION oldFunc=(TYPE_FUNCTION)ft;
			checkOverload(newFunc, oldFunc); //if there will be a problom - there will be an error here
		}
	
		//OK
		//typeClass.addToMap(funcName, t);
		return t;
		
	}
	
	
	/*Input: TYPE FUNCTION of function and TYPE_FUNCTION of a function it overrides
	 * this method checks if they have the same signature 
	 * and reports a semantical error if not*/
	public void checkOverload(TYPE_FUNCTION func1, TYPE_FUNCTION func2) throws SemantErrorException {
		
		String errMessage; 
		//first, check the return TYPE
		if(!( func1.returnType.name.equals(func2.returnType.name))){
			errMessage=String.format("overloading - %s and %s have a different return type\n"
					, func1.name, func2.name);
			throw new SemantErrorException(errMessage, lineNum);
			//error
		}

		//now compare functions parameters
		int status=compareParams(func1.params, func2.params);
		
		if(status==0) {
			errMessage=String.format("function overloading - %s and %s have different list of paramaters\n"
					, func1.name, func2.name);
			throw new SemantErrorException(errMessage, lineNum);
			//error
		}
		
		//succsess
	}
	
	public TEMP IRme(TYPE_CLASS c) {
		this.funcDec.IRme();
		return null;
	}
}
