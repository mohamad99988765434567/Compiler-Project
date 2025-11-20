package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_PROGRAM extends AST_Node{
	public AST_DEC_LIST decList;
	
	public AST_PROGRAM(AST_DEC_LIST decList) {

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== program -> decList");
		

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.decList=decList;
		
		
	}
	
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NODE PROGRM */
		/*******************************/
		System.out.format("AST NODE PROGRM\n");
		
		/***********************************/
		/* RECURSIVELY PRINT decList ... */
		/***********************************/
		
		if (decList!=null) decList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,"PROGRAM");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (decList!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, decList.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantErrorException {
		
		SYMBOL_TABLE.getInstance().beginScope("global", null);
		decList.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}
	
	public TEMP IRme() {
		IR.getInstance().Add_IRcommand(new IRcommand_define_str_concationation_func());
		IR.getInstance().Add_IRcommand(new IRcommand_define_str_eq_func());
		IR.getInstance().Add_IRcommand(new IRcommand_define_exit_div_by_zero());
		IR.getInstance().Add_IRcommand(new IRcommand_define_exit_invalid_pointer());
		IR.getInstance().Add_IRcommand(new IRcommand_define_exit_out_of_bound());


		decList.IRme();
		return null;
		
	}
	
	

}
