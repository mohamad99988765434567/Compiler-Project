package AST;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD {
	
	AST_VAR_DEC varDec;
	
	public AST_CFIELD_VAR_DEC(AST_VAR_DEC varDec, int lineNum){
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> varDec\n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for a CFIELD VAR DEC AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = CFIELD VAR DEC AST NODE */
		/************************************/
		System.out.print("AST NODE CFIELD VAR DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT varDec ... */
		/*****************************/
		if (varDec != null) varDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nVAR\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
			
	}
	
	public TYPE SemantMe(TYPE_CLASS typeClass) throws SemantErrorException {
		
		String errMessage;
		
		TYPE t=varDec.SemantMe(); //if error - it will be there
		
		if (typeClass.father==null) { //just put it in the dataMembers and continue
			//CHECK HERE OR IN SEMANT ME if already in scope +ERROR handling????
			//typeClass.addToMap(t.name, t);
			return t; //OK
		}
		
		//check if exists in father's class
		
		
		String varName=((TYPE_CLASS_VAR_DEC)t).name;
		
		
		//System.out.println(varName);
		
		TYPE ft=searchInClassHierarchy(varName,typeClass.father);  //START FROM DAD 
		if (ft!=null) { //varName is already exists
			
			
			if ((ft instanceof TYPE_FUNCTION)) {
				errMessage=String.format("%s variable's name is already taken in class hierarchy, "
						+ "but it's a function\n",varName);
				throw new SemantErrorException(errMessage, lineNum);
			}
				
			
			TYPE_CLASS_VAR_DEC x=(TYPE_CLASS_VAR_DEC)ft;
			if (!((x.t.name).equals(((TYPE_CLASS_VAR_DEC)t).t.name))) { //check if they have the same type
				errMessage=String.format("%s is already defined as %s in class hierarchy\n"
						,varName,x.t.name);
				throw new SemantErrorException(errMessage, lineNum);
			}
					
					
		}
		//variable does not exist in father	OR exist with the same type		
		//typeClass.addToMap(varName, t);
		
		
		return t;
	}
	
	
	public TEMP IRme(TYPE_CLASS c) {
		
		this.varDec.IRme(c);
		
		return null;
		
	}

}
