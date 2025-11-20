package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

import TEMP.*;

import java.util.LinkedHashMap;
import java.util.Set;

import IR.*;

public class AST_NEW_EXP extends AST_EXP {
	
	public AST_TYPE type;
	public AST_EXP exp;
	public TYPE realTYPE;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP(AST_TYPE type, AST_EXP exp, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== exp -> NEW type LBRACK exp RBRACK\n");
		if (exp == null) System.out.print("====================== exp -> NEW type\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.exp = exp;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for an new exp AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = NEW EXP AST NODE */
		/************************************/
		System.out.print("AST NODE NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp !=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}
	public TYPE SemantMe() throws SemantErrorException{
		
		String errMessage;
		/****************************************/
		/* lets find the TYPE */
		/****************************************/
		TYPE t=SYMBOL_TABLE.getInstance().findType(type.name);
		
		//TYPE MUST BE CLASS OR ARRY, CANNOT BE PRIMITIVE
		if (t == null)//////////////////////////////////?????////////
		{
			errMessage=String.format("non existing type %s\n", this.type.name);
			throw new SemantErrorException(errMessage, this.lineNum);
			//error handling
		}
		
		this.realTYPE=t;
		
		/****************************************/
		/* if (exp!= null) then its Array hence we will make sure exp is int */
		/****************************************/
		
		if (exp!= null){
			
			
			TYPE exp_type = exp.SemantMe();
			
			if (!(exp_type.name.equals(TYPE_INT.getInstance().name))){
				errMessage=String.format("allocating array with the new operator must be done with"
						+ "an integral size");
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			
			
			
			
			if (exp instanceof AST_EXP_INT){
				int val = ((AST_EXP_INT)exp).value;
				if (val<=0){ //VALUE MUST BE GREATER THAN ZERO 
					errMessage=String.format("when allocating array with constant size, it must be greater than zero");
					throw new SemantErrorException(errMessage, this.lineNum);
				}
				
			}
			
			TYPE_ARR returnType=new TYPE_ARR(null, t);
			return returnType;
			
		}
		
		
		else { //exp=null
			if (!(t instanceof TYPE_CLASS)) {
				errMessage=String.format("when allocating a class object using the new operator,);"
						+ "the class name can't be a primitive Type and can't be array type if it's not allocated with"
						+ "an integral value");
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			return t;
			
		}
	
	}
	
	public TEMP IRme() {
		
		if (exp==null) { //then this.realTYPE is class !
			TYPE_CLASS tclass=(TYPE_CLASS)this.realTYPE;
			TEMP dst= TEMP_FACTORY.getInstance().getFreshTEMP();
			TEMP size=TEMP_FACTORY.getInstance().getFreshTEMP();
			int sizeInBytes=4*( (tclass.Allfields.size()) + 1);
			String className=this.type.name;
			IR.getInstance().Add_IRcommand(new IRcommand_NewClassObject(dst,className,sizeInBytes,tclass));
			
			
//			IR.getInstance().Add_IRcommand(new IRcommandConstInt(size,sizeInBytes));
//			
//			IR.getInstance().Add_IRcommand(new IRcommand_malloc(dst,size));
//			
//			
//			//load pointer to Vtable to t1
//			TEMP t1=TEMP_FACTORY.getInstance().getFreshTEMP();
//			IR.getInstance().Add_IRcommand(new IRcommand_load_address(t1, "vt_"+this.type.name));
//			
//			//save pointer to vtable in the first allocated "cell";
//			IR.getInstance().Add_IRcommand(new IRcommand_sw(t1,dst,0));
			
			
			//initialized values
			
			IR.getInstance().Add_IRcommand(new IRcommand_initializeClassFields(dst, tclass.Allfields));
			
			return dst;
			
			//what about initialized values ? do we need to save their values ? yes.
			
		}
		else { //exp!=null - we need to allocate array
			TEMP t1=this.exp.IRme(); //size
			TEMP dst=TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_NewArray(dst,t1));
			return dst;
//			TEMP t2=TEMP_FACTORY.getInstance().getFreshTEMP();
//			IR.getInstance().Add_IRcommand(new IRcommand_Add_Immediate(t2,t1,1)); //t2=t1+1=size_of_array+1
//			TEMP t3=TEMP_FACTORY.getInstance().getFreshTEMP();
//			IR.getInstance().Add_IRcommand(new IRcommandConstInt(t3,4)); //t3=4
//			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(t2,t2,t3)); //t2=t2*t3
//			
//			//now t2 contains the size that we want to allocate
//			//malloc
//			TEMP dst=TEMP_FACTORY.getInstance().getFreshTEMP();
//			IR.getInstance().Add_IRcommand(new IRcommand_malloc(dst,t2));
//			
//			//write the size of the array (t1) in the first "cell" of the array
//			IR.getInstance().Add_IRcommand(new IRcommand_sw(t1,dst,0));
			//return dst;
		}
	
	}
	

}
