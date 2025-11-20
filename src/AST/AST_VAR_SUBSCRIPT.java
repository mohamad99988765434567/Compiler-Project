package AST;
import IR.*;
import TEMP.*;
import TYPES.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int lineNum) throws SemantErrorException
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.lineNum=lineNum;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		
		String errMessage;
		
		TYPE varType=this.var.SemantMe();
		
		if(!(varType instanceof TYPE_ARR)) {
			errMessage=String.format("varaible is not an array\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		TYPE subscriptType= this.subscript.SemantMe();
		if (!(subscriptType instanceof TYPE_INT)) {
			errMessage=String.format("subscript expression must has an integer type\n");
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		
		TYPE_ARR arrType=(TYPE_ARR)varType;
		
		
		return arrType.typeArray;
		
	}
	
	public TEMP IRme() {
		TEMP offset = subscript.IRme();
		TEMP address = var.IRme();
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_ArrayAccess(t, address, offset));
		return t;
	}
	
	public TEMP IRmeWrite(TEMP value, TEMP offset) {
		//TEMP offset=subscript.IRme();
		TEMP address = var.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_ArraySet(value,address,offset));
		return null;

	}
	
	public TEMP getOffset() {
		TEMP offset = subscript.IRme();
		return offset;
	}
	
}
