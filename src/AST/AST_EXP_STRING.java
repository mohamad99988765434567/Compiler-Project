package AST;

import IR.IR;
import IR.IRcommand;
import IR.IRcommand_load_address;
import IR.IRcommand_string_content;
import IR.IRcommand_string_label;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.*;


public class AST_EXP_STRING extends AST_EXP {
	
	public String value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(String value, int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STRING( %s )\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
		this.lineNum=lineNum;
	}

	/************************************************/
	/* The printing message for a STRING EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STRING EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING(%s)",value));
	}
	
	public TYPE SemantMe()
	{
		return TYPE_STRING.getInstance();
	}
	
	public TEMP IRme() {
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		String str_const = IRcommand.getFreshLabel("str_const");
		IR.getInstance().Add_IRcommand(new IRcommand_string_content(str_const, this.value));
		IR.getInstance().Add_IRcommand(new IRcommand_load_address(dst, str_const));

		
		return dst;
		

	}
	
	
	public TEMP IRme(String name) { //called when declare a global string
		String str_const = IRcommand.getFreshLabel("str_const");
		IR.getInstance().Add_IRcommand(new IRcommand_string_content(str_const, this.value));
		IR.getInstance().Add_IRcommand(new IRcommand_string_label(name, str_const));
		
		return null;
	}
	
	
	
}
