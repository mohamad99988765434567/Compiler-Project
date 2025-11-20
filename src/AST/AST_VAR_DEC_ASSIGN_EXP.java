package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_VAR_DEC_ASSIGN_EXP extends AST_VAR_DEC {
	
	public AST_EXP exp;
	
	public AST_VAR_DEC_ASSIGN_EXP(AST_TYPE type, String name, AST_EXP exp, int lineNum) {
		
		super(type, name,lineNum);
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON \n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
	}
	
	
	/***********************************************/
	/* The default message for a VAR DEC ASSIGN EXP AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = VAR DEC ASSIGN EXP AST NODE */
		/************************************/
		System.out.print("AST NODE VAR DEC ASSIGN EXP "+ name +"\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if (exp  != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR\nDEC\nASSIGN\nEXP\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException
	{
		
		String errMessage;
		TYPE t;
		TYPE exp_type;
		
		//if current scope is a class, variable must be initialized with constant value
		String currScopeType=SYMBOL_TABLE.getInstance().getCurrScopeType();
		if (currScopeType.equals("class")) {
			if (!(this.exp instanceof AST_EXP_INT || this.exp instanceof AST_EXP_STRING || this.exp instanceof AST_EXP_NIL)) {
				errMessage=String.format("a declared data member inside a class can be initialized only with a "
						+ "constant value\n");
				throw new  SemantErrorException(errMessage, this.lineNum);
			}	
		}
		
		
		/****************************/
		/* [0] Semant exp */
		/****************************/
		exp_type=exp.SemantMe();
		

		
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
		
		
		/***************************************************/
		/* [3] CHECK THE ASSIGNMETN */ 
		/***************************************************/
		if ( !(exp_type.name.equals(t.name)) ) {
			if(!TYPE.check_unequal_cases(t, exp_type)){
				errMessage=String.format("Illegal Assignmet between types: %s , %s", t.name, exp_type.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
		}
		
		
		
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
		/* [5] Return value is irrelevant for class declarations
		 * BUT - CFIELD_VAR_DEC MAY USE IT - SO WE RETURN THE FOLLOWING:  */ 
		/*********************************************************/
		TYPE_CLASS_VAR_DEC result= new TYPE_CLASS_VAR_DEC(t, name, m.offset);
		return result;		
	}
	
	
	
	//declaration - global/local - in a function
	public TEMP IRme() {
		TEMP t1 = null;
		
		
		if (this.metadata.type==0) { //global - exp is constant - string, int or nil
			this.exp.IRme(this.name);
		}
			
			
				
		if(this.metadata.type==2){//local
			t1=this.exp.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(this.metadata.offset, t1));
		}
		
		return null;
		
	}
	
	
	public TEMP IRme(TYPE_CLASS classType) { //called from cfield_var_Dec -this.metadata.type=3
		TYPE_CLASS_VAR_DEC ctype= (TYPE_CLASS_VAR_DEC)classType.Allfields.get(this.name);
		TEMP t1 = null;
		if (this.exp!=null) {
			//t1=this.exp.IRme();
			ctype.initialValue=this.exp;
		}
		
		
		return null;
		
	}

}
