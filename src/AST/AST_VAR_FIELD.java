package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

import IR.*;
import TEMP.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public TYPE_CLASS tclass;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.lineNum=lineNum;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantErrorException{
		
		String errMessage;
		
		TYPE varType=this.var.SemantMe();
		
		
		if (varType==null) { //we don't really need to check it.. but it doesn't matter
			errMessage=String.format("variable does not exist\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		if (!(varType instanceof TYPE_CLASS)) {
			errMessage=String.format("variable type isn't a class type - it has no field\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		//now we are looking for the field
		TYPE_CLASS varClass=(TYPE_CLASS)varType;
		this.tclass=varClass;
		
		
		//we don't need to search in the SYMBOL TABLE, we can look for the variable in the class data members !
		//even if the class scope is still open - if field was declared, it's already exist in the data_members Map
		TYPE t=searchInClassHierarchy(this.fieldName,varClass);
		
		if (t!=null) {
			
			if (t instanceof TYPE_FUNCTION) {
				errMessage=String.format("%s is not a field in class %s\n", this.fieldName, varClass.name);
				throw new SemantErrorException(errMessage, this.lineNum);	
			}
			
			else { //t instance of TYPE_CLASS_VAR_DEC
				TYPE_CLASS_VAR_DEC x= (TYPE_CLASS_VAR_DEC)t;
				this.metadata=new Metadata(3,x.offset); //save metadata
				return x.t;	//return the "real" type					
			}
		}
		//t is null - no field was found - ERROR.
		errMessage=String.format("variable has no field\n");
		throw new SemantErrorException(errMessage, this.lineNum);
		
				
	}
	
	
	public TEMP IRme() {
		TEMP t1=this.var.IRme(); //the class object
		TEMP dst=TEMP_FACTORY.getInstance().getFreshTEMP();
		int index=this.tclass.fieldIndex.indexOf(this.fieldName)+1;
		IR.getInstance().Add_IRcommand(new IRcommand_fieldAccess(dst, t1, index));
		return dst;
		
		
	}
	
	public TEMP IRmeWrite(TEMP value) {
		TEMP t1=this.var.IRme(); //the class object
		int index=this.tclass.fieldIndex.indexOf(this.fieldName)+1;
		IR.getInstance().Add_IRcommand(new IRcommand_fieldSet(value, t1, index));

		
		return null;
		
		
	}
	
	
	
	
}
