package AST;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	public int concatenation;
	
	public int intEq;
	public int stringEq;
	public int arrayEq;
	public int classEq;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.lineNum=lineNum;
		
		this.concatenation=0;
		this.intEq=0;
		this.stringEq=0;
		this.arrayEq=0;
		this.classEq=0;
		
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}
		
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantErrorException
	{
		
		
		String errMessage;
		
		TYPE t1 = null;
		TYPE t2 = null;
		
		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();
		
	
		
		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()))
		{
			if (OP==3) {
				//check division by zero
				if (right instanceof AST_EXP_INT) {
					if (((AST_EXP_INT)right).value==0){
						errMessage=String.format("division by zero\n");
						throw new SemantErrorException(errMessage, lineNum);
						//error handling/////////////////////////////////////////////////////////////
					}
				}
			}
			return TYPE_INT.getInstance();
		}
		
		if ( (t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance()) && (OP==0) ) {
			this.concatenation=1;
			return TYPE_STRING.getInstance();
		}
		
		if(OP==6) {
			
			//i added checks for IRme()
			if(t1==TYPE_INT.getInstance()) {
				this.intEq=1;
			}
			
			if(t1==TYPE_STRING.getInstance()) {
				this.stringEq=1;
			}
			
			if(t1 instanceof TYPE_ARR || t2 instanceof TYPE_ARR) {
				this.arrayEq=1;
			}
			if(t1 instanceof TYPE_CLASS || t2 instanceof TYPE_CLASS) {
				this.classEq=1;
			}
			
			
			
			
			/////////////////////////////////////////////////////////////
			
			
			
			
			if (t1.name.equals(t2.name)){
				return TYPE_INT.getInstance();
			}
			
			
			
			if((t2.name.equals("nil"))&&( (t1 instanceof TYPE_ARR)||(t1 instanceof TYPE_CLASS))) {
				return TYPE_INT.getInstance();
			}
			
			if((t1.name.equals("nil"))&&( (t2 instanceof TYPE_ARR)||(t2 instanceof TYPE_CLASS))) {
				return TYPE_INT.getInstance();
			}
			
			if ((t1 instanceof TYPE_CLASS)&&(t2 instanceof TYPE_CLASS)) {
				TYPE_CLASS isFather=(TYPE_CLASS)t1;
				TYPE_CLASS isSon=(TYPE_CLASS)t2;
				for (TYPE_CLASS t=isSon; t!=null; t=t.father) {
					if(t.name.equals(isFather.name)){
						return TYPE_INT.getInstance();
					}
				}
			}
			
			if ((t1 instanceof TYPE_CLASS)&&(t2 instanceof TYPE_CLASS)) {
				TYPE_CLASS isFather=(TYPE_CLASS)t2;
				TYPE_CLASS isSon=(TYPE_CLASS)t1;
				for (TYPE_CLASS t=isSon; t!=null; t=t.father) {
					if(t.name.equals(isFather.name)){
						return TYPE_INT.getInstance();
					}
				}
			}
			
			
		}
		
		
		
		errMessage=String.format("Illegal Equality Testing\n");
		throw new SemantErrorException(errMessage, lineNum);
		
	}
	
	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
				
		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();
		
		if (OP == 0)
		{
			if(this.concatenation==1) {
				IR.getInstance().Add_IRcommand(new IRcommand_string_concat(dst,t1,t2));
				//return dst;
			}
			
			else {
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
				IR.
				getInstance().Add_IRcommand(new IRcommand_Binop_limit(dst));
			}
			
		}
		
		
		if (OP==1)
		{
			IR.getInstance().
			Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
			IR.
			getInstance().Add_IRcommand(new IRcommand_Binop_limit(dst));

		}
		
		if (OP == 2)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
			IR.
			getInstance().Add_IRcommand(new IRcommand_Binop_limit(dst));

			
		}
		if (OP == 3)
		{
			
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
			
			IR.
			getInstance().Add_IRcommand(new IRcommand_Binop_limit(dst));
			//return dst;

			
		}
		if (OP == 4)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
			
			//return dst;
		}
		
		if (OP == 5)
		{
			IR.getInstance().
			Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
			//return dst;
		}
		
		
		if (OP == 6)
		{
			
			if(this.stringEq==1) {
				
				IR.getInstance().Add_IRcommand(new IRcommand_string_eq(dst,t1,t2));
				//return dst;
			}
			
			else {
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
				//return dst;
			}
			
			
		}
		
		
		
		return dst;
	}
	
	
	
}
