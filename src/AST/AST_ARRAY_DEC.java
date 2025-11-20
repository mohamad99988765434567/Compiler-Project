package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARRAY_DEC extends AST_Node{
	
	String name;
	AST_TYPE type;
	
	public AST_ARRAY_DEC(String name, AST_TYPE type, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== arrayDec -> ARRAY ID EQ type [];\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.type = type;
		this.lineNum=lineNum;
	}
	
	/***********************************************/
	/* The default message for an array dec AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = ARRAY DEC AST NODE */
		/************************************/
		System.out.print("AST NODE ARRAY DEC"+name+"\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ARRAY\nDEC\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
			
	}
	public TYPE SemantMe() throws SemantErrorException {
		
		String errMessage;
		
		TYPE t = null;
		TYPE arrayType = null;
		
		
		/*******************/
		/* [1] make sure array type is already defined */
		/*******************/
		arrayType = SYMBOL_TABLE.getInstance().findType(this.type.name);
		if (arrayType == null)
		{
			errMessage=String.format("non existing type %s\n", this.type.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		/*******************/
		/* [1] check if ID is already taken  */
		/*******************/
		t = SYMBOL_TABLE.getInstance().find(this.name);
		if (t != null)
		{
			errMessage=String.format("name %s already exists in the scope", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
		}
		
		/***************************************************/
		/* [3] Enter the ARRAy Type to the Symbol Table */
		/***************************************************/
		TYPE_ARR arr=new TYPE_ARR(name,arrayType);
		SYMBOL_TABLE.getInstance().enter(name, arr);
		return arr;
	}
		
	
}
