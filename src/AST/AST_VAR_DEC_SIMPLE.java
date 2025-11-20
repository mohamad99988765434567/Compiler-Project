package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

public class AST_VAR_DEC_SIMPLE extends AST_VAR_DEC {
	
	public AST_VAR_DEC_SIMPLE(AST_TYPE type, String name,int lineNum){
		
		super(type,name,lineNum);
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID SEMICOLON \n");
		
		
	}
	
	/***********************************************/
	/* The default message for a VAR DEC SIMPLE AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = VAR DEC SIMPLE AST NODE */
		/************************************/
		System.out.print("AST NODE VAR DEC SIMPLE "+ name +"\n");

		/*****************************/
		/* RECURSIVELY PRINT type ... */
		/*****************************/
		if (type != null) type.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR\nDEC\nSIMPLE\n"+name);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantErrorException
	{
		
		String errMessage;
		
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		
		TYPE t = SYMBOL_TABLE.getInstance().findType(type.name);
		if (t == null)//////////////////////////////////?????////////
		{
			errMessage=String.format("non existing type %s\n", this.type.name);
			throw new SemantErrorException(errMessage, this.lineNum);
			//error handling
		}
		
		/**************************************/
		/* [2] Check That Name does NOT exist in the current scope */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().findInCurrScope(this.name) != null)
		{
			
			errMessage=String.format("variable %s already exists in scope\n", this.name);
			throw new SemantErrorException(errMessage, this.lineNum);
			//error handling
		}
		
		//if the declaration is inside a class - we will check the inheritance in CFIELD_VAR_DEC
	
		//NO OTHER CHECKS HERE, LETS ENTER IT TO THE CURRENT SCOPE 
		
		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_VAR typeToEnter=new TYPE_VAR(this.name,t);
		SYMBOL_TABLE.getInstance().enterVariable(this.name,typeToEnter);
		
		
		//SAVE METADATA
		Metadata m=SYMBOL_TABLE.getInstance().findMetadata(this.name);
		if (m!=null) {
			this.metadata=m;
		}
		
		
		
		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations
		 * BUT - CFIELD_VAR_DEC MAY USE IT - SO WE RETURN THE FOLLOWING: */
		/*********************************************************/
		
		TYPE_CLASS_VAR_DEC result= new TYPE_CLASS_VAR_DEC(t, name, m.offset);
		
		return  result; 		
	}
	
	
	public TEMP IRme() {
		
		if(this.metadata.type==0||this.metadata.type==2) { //global or local
			if(this.type.name.equals("string")) {
				
				
				if(this.metadata.type==0) { //global
					//System.out.println("GLOBALLLLLLLLLLLLLLLLLLLLLLL");
					//System.out.println(this.name);
					IR.getInstance().Add_IRcommand(new IRcommand_string_content(this.name+"_str", ""));
					IR.getInstance().Add_IRcommand(new IRcommand_string_label(this.name, this.name+"_str"));

				}
				
				if(this.metadata.type==2) {  //local
					TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
					
					String str_const = IRcommand.getFreshLabel("str_const");
					
					IR.getInstance().Add_IRcommand(new IRcommand_string_content(str_const, ""));
					
					IR.getInstance().Add_IRcommand(new IRcommand_load_address(t1, str_const));
					
					//write to the stack ??????
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(this.metadata.offset, t1));

				}
				
			}
			
			else {
				if(this.metadata.type==0) { //global
					IR.getInstance().Add_IRcommand(new IRcommand_global_var(name, 0));
				}
				if(this.metadata.type==2) {
					TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
					IR.getInstance().Add_IRcommand(new IRcommandConstInt(t1, 0));
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(this.metadata.offset, t1));

				}
			
			}
		}
		return null;
		
		
			
	}
		
		
	
	
	public TEMP IRme(TYPE_CLASS t) { //called from cfield_var_Dec -this.metadata.type=3
		
		return null;
		
	}
	

}
