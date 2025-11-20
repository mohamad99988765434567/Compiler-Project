package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public int totalLocals; //count number of local variables entered to the symbol table
	public int totalParams; //count number of parameters entered to the symbol table
	
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.totalLocals=0;
		this.totalParams=0;
	}
	
	//increment number of parameters when we enter a new parameter to the symbol table
	public int incParam() {
		this.totalParams++;
		return this.totalParams;
	}
	
	
	//increment number of locals when we enter a new local variable to the symbol table
	public int incLocal() {
		this.totalLocals++;
		return this.totalLocals;
	}
	
	
	
}
