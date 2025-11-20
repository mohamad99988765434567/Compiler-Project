package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_VAR_DEC_ASSIGN_NEW_EXP extends AST_VAR_DEC{
	
	AST_NEW_EXP newExp;
	
	public AST_VAR_DEC_ASSIGN_NEW_EXP(AST_TYPE type, String name, AST_NEW_EXP newExp, int lineNum){
		
		super(type, name, lineNum);
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID ASSIGN newExp SEMICOLON \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.newExp = newExp;
		
	}
	
	/***********************************************/
	/* The default message for a VAR DEC ASSIGN NEW EXP AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = VAR DEC ASSIGN NEW EXP AST NODE */
		/************************************/
		System.out.print("AST NODE VAR DEC ASSIGN NEW EXP "+ name +"\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if (newExp  != null) newExp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR\nDEC\nASSIGN\nNEWEXP\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (newExp  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException
	{
		
		String errMessage;
		TYPE t;
		TYPE exp_type;
		
		//first check if is it a class scope - if is is, we cannot declare variable non-contant variable
		String currScopeType=SYMBOL_TABLE.getInstance().getCurrScopeType();
		if (currScopeType.equals("class")) {
			errMessage=String.format("a declared data member inside a class can be initialized only with a "
																				+ "constant value\n");
			throw new  SemantErrorException(errMessage, this.lineNum);
		}
		
		
		/****************************/
		/* [0] Semant newExp */
		/****************************/
		exp_type=newExp.SemantMe(); //if error - it will be here
		
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().findType(type.name);
		if (t == null)
		{
			errMessage=String.format("non existing type %s\n",type.name);
			throw new  SemantErrorException(errMessage, this.lineNum);
		}
		
		
		
		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().findInCurrScope(this.name) != null)
		{
			errMessage=String.format("variable %s already exists in scope\n", this.name);
			throw new  SemantErrorException(errMessage, this.lineNum);
		}
		
		//if the declaration is inside a class - we will check the inheritance in CFIELD_VAR_DEC, not here.
		
	
		//WE JUST NEED TO CHECK THE ASSIGNMENT
		/***************************************************/
		/* [3] CHECK THE ASSIGNMETN */ 
		/***************************************************/
		
		
		if (exp_type instanceof TYPE_ARR) {
			
			TYPE_ARR expArrType=(TYPE_ARR)exp_type;
			
			if (!(t instanceof TYPE_ARR )) {
				errMessage=String.format("Illegal Assignmet between types: %s , array of %s\n", t.name, expArrType.typeArray.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			TYPE_ARR tArraytype=(TYPE_ARR)t;
			
			if (!(tArraytype.typeArray.name.equals(expArrType.typeArray.name))) {
				if(!TYPE.check_unequal_cases(tArraytype.typeArray, expArrType.typeArray)){
					errMessage=String.format("Illegal Assignmet between types: %s array, %s array\n", tArraytype.typeArray.name, expArrType.typeArray.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
		}
		
		
		
		
		else { //exp_type is TYPE_CLASS
			if (!(t instanceof TYPE_CLASS)) {
				errMessage=String.format("Illegal Assignmet between types: %s , %s", t.name, exp_type.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			if ( !(exp_type.name.equals(t.name)) ) {
				if(!TYPE.check_unequal_cases(t, exp_type)){
					errMessage=String.format("Illegal Assignmet between types: %s , %s", t.name, exp_type.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
			
		}
		
		
		
		
		
		//IF WE GOT HERE, EVERTYTHING IS FINE
		
		/***************************************************/
		/* [4] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_VAR typeToEnter=new TYPE_VAR(this.name,t);
		SYMBOL_TABLE.getInstance().enterVariable(name,typeToEnter);
		
		
		
		//SAVE METADATA
		Metadata m=SYMBOL_TABLE.getInstance().findMetadata(this.name);
		if (m!=null) {
			this.metadata=m;
		}

		/*********************************************************/
		/* [5] Return value is irrelevant for array declarations
		 * BUT - CFIELD_VAR_DEC MAY USE IT - SO WE RETURN THE FOLLOWING:  */ 
		/*********************************************************/
	
		TYPE_CLASS_VAR_DEC result= new TYPE_CLASS_VAR_DEC(t, name, m.offset);
		return result;		
	}
	
	
	public TEMP IRme() { //local variable declaration (Array or class)
		
		/*cannot be a global - "you may assume that if a global variable is initialized,
		 *  then the initial value is a constant" (ex4 doc)
		 *it cannot be a class field "a declared data member inside a class, can be initialized only with
		 *                             a constant value" (ex3 doc)"*/
		
		//TEMP t1=null;
		TEMP t1=this.newExp.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(this.metadata.offset, t1));
		
		
	
		
		return null;
		
	}

		
	

}
