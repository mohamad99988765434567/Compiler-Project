package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;


public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public TYPE_CLASS tclass;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name,int lineNum)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.lineNum=lineNum;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
	
	
	
	
	
	public TYPE SemantMe() throws SemantErrorException
	{
		String errMessage;
		
		
		
		TYPE_CLASS typeclass= SYMBOL_TABLE.getInstance().getCurrClass();
		
		if (typeclass!=null) { //we are in a class scope
			TYPE varType=SYMBOL_TABLE.getInstance().findInClassScope(name);
			this.tclass=typeclass;
			
			if (varType!=null){
				if (!(varType instanceof TYPE_VAR)) {
					errMessage=String.format("variable %s does not exists", this.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
				else {
					//SAVE METADATA
					Metadata m=SYMBOL_TABLE.getInstance().findMetadataInCurrClass(name);
					this.metadata=m;
					//RETURN TYPE
					return ((TYPE_VAR)varType).t;
					
				}
				
			}
			
			
			// search in dad
			TYPE t= searchInClassHierarchy(this.name, typeclass.father);
			if (t!=null) {
				if(!(t instanceof TYPE_FUNCTION)) {
					TYPE_CLASS_VAR_DEC x= (TYPE_CLASS_VAR_DEC)t;
					this.metadata=new Metadata(3,x.offset); //save metadata
					return x.t; //return type
				}
				else { // t is type_function
					errMessage=String.format("variable %s does not exists", this.name);
					throw new SemantErrorException(errMessage, this.lineNum);
				}
			}
			
			//BUT IF IT WAS FOUND AS FUNCTION - IS IT AN ERROR OR DO WE NEED TO KEEP SEARCH?
			
			//var was not found in dad's class
			//search in the global scope
			varType=SYMBOL_TABLE.getInstance().findInGlobalScope(name);
			if (varType==null || !(varType instanceof TYPE_VAR)) { //no variable
				errMessage=String.format("variable %s does not exists", this.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			//SAVE METADATA
			Metadata m=SYMBOL_TABLE.getInstance().findMetadataInGlobalScope(name);
			if(m!=null) {
				this.metadata=m;
			}
			return ((TYPE_VAR)varType).t; //variable was found
				
		}
		//MAKE SURE THAT THE REST OF THE FLOW IS RIGHT
		else { //no class - no inheritance - regular search
			TYPE varType=SYMBOL_TABLE.getInstance().find(name);
			if (varType==null || !(varType instanceof TYPE_VAR)) {
				errMessage=String.format("variable %s does not exists", this.name);
				throw new SemantErrorException(errMessage, this.lineNum);
			}
			
			//SAVE METADATA
			Metadata m=SYMBOL_TABLE.getInstance().findMetadata(this.name);
			if (m!=null) {
				this.metadata=m;
			}
			
			return ((TYPE_VAR)varType).t;
				
		}
		
		
	}
	
	
	public TEMP IRme()
	{
		
		//local vs global ?
		//read vs write ?
		
		//x=3 - write
		//y=x+3 - read
		//i guess that when we call IRme() is for read
		TEMP t1= TEMP_FACTORY.getInstance().getFreshTEMP();

		if(this.metadata.type==2){// local variable
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Load_Local(t1,this.metadata.offset));
			return t1;
		}
		
		if(this.metadata.type==0){// global variable
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Load(t1,"global_"+this.name)); //load
			return t1;
		}
		
		if (this.metadata.type==1) { //parameter
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Load_Param(t1,this.metadata.offset));
			return t1;
		}
		
		if(this.metadata.type==3) {
			IR.getInstance().Add_IRcommand(new IRcommand_Load_Class_Field(t1,this.tclass.fieldIndex.indexOf(this.name)+1));
			return t1;
		}
		
		return null;
		//other options ?
		
	}
	
	public TEMP IRmeWrite(TEMP value) {

		if(this.metadata.type==2){// local variable
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Store_Local(this.metadata.offset, value));
		}
		
		if(this.metadata.type==0){// global variable
			
			
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Store("global_"+this.name, value)); //load
			
		}
		
		if(this.metadata.type==1) {
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Store_Param(value, this.metadata.offset));
			 
		}
		
		if(this.metadata.type==3) {
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Class_Field(value,this.tclass.fieldIndex.indexOf(this.name)+1));
		}
		
		return null;

	}
	
	
}
