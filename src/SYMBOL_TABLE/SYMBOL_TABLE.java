/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;
	private int scopesCounter=0; //how many scopes are exists
	private int globalVarCounter=0; //how many global variables already entered - we will use it for metadata
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name,TYPE t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name,t,hashValue,next,top,top_index++,scopesCounter, null);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}
	
	
	//when we enter a function parameter (when we declare a function) - we will call this function
	public void enterParam(String name,TYPE t) {
		TYPE_FUNCTION funcType=getCurrFunc();
		if (funcType!=null) {
			int offset=funcType.incParam();
			Metadata metadata= new Metadata(1,offset); //1 means parameter
			enterWithMetadata(name, t, metadata);
		}
	}

	
	//when we enter a variable (when we declare a variable) - we will call this method
	//we will check here if it's a local variable or a class field.
	public void enterVariable(String name,TYPE t) {
		//System.out.println("ENTER VARIABLE");
		//System.out.println(name);

		TYPE_FUNCTION funcType=getCurrFunc();
		if(funcType!=null) {//we are inside a function scope, it's a declaration of local variable !
			//System.out.println("LOCAL");
			int offset=funcType.incLocal();
			Metadata metadata= new Metadata(2,offset); //2 means local variable
			enterWithMetadata(name, t, metadata);
			return;
		}
		
		TYPE_CLASS classType=getCurrClass();
		if(classType!=null) { //we are inside a class scope, it's a field
			//System.out.println("CLASS");
			int offset=classType.incField();
			Metadata metadata=new Metadata(3,offset); //3 means a class field
			enterWithMetadata(name, t, metadata);
			return;
		}
		if (isGlobalScope()) { //it's a global variable
			//System.out.println("GLOBAL");
			globalVarCounter++;
			Metadata metadata=new Metadata(0,globalVarCounter); //0 means a global variable
			enterWithMetadata(name, t, metadata);
			return;
		}
		
		
		
		
	}
	
	
	//same as enter, just include the meta data - i did not want to change the call in all of the classes
	public void enterWithMetadata(String name,TYPE t, Metadata metadata) {
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name,t,hashValue,next,top,top_index++,scopesCounter, metadata);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	
	}
	
	public Metadata findMetadata(String name) {
		
		SYMBOL_TABLE_ENTRY e;
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.metadata;
			}
		}
		
		return null;
		
	}
	
	public Metadata findMetadataInCurrClass(String name) {
		TYPE_FOR_SCOPE_BOUNDARIES currClass= getCurClassBoundary();
		if (currClass==null){
			return null;
		}
		
		int classScopeNum=currClass.scopeNum;
		SYMBOL_TABLE_ENTRY e;
		for (e = table[hash(name)]; e != null; e = e.next)
		{	
			if ( (name.equals(e.name))&& (e.scopeNum>=classScopeNum))
			{
				return e.metadata;
			}
		}
		return null;
		
	}
	
	
	public Metadata findMetadataInGlobalScope(String name) {
		SYMBOL_TABLE_ENTRY e;
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			
			if ( (name.equals(e.name)) && ( (e.scopeNum==0) || (e.scopeNum==1) )) //zero for the default types and functions;
			{
				return e.metadata;
			}
		
		}
		return null;
	}
	
	
	
	
	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	
	//REGULAR DEAFAULT FIND
	public TYPE find(String name)
	{
		SYMBOL_TABLE_ENTRY e;
				
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type;
			}
		}
		
		return null;
	}
	
	//FIND ONLY IN LAST SCOPE
	/*check if the name is already exists in the current scope*/
	public TYPE findInCurrScope(String name) {
		
		if (this.scopesCounter==1) {
			return findInGlobalScope(name);
		}
		
		SYMBOL_TABLE_ENTRY e;
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if ((name.equals(e.name)) && (e.scopeNum==this.scopesCounter))
			{
				return e.type;
			}
		
		}
		return null;
		
	}
	
	//SEARCH ONLY IN TTHE GLOBAL SCOPE
	public TYPE findInGlobalScope(String name) {
		
		SYMBOL_TABLE_ENTRY e;
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			
			if ( (name.equals(e.name)) && ( (e.scopeNum==0) || (e.scopeNum==1) )) //zero for the default types and functions;
			{
				return e.type;
			}
		
		}
		return null;
		
	}
	
	//IF CLASS SCOPE IS EXIST - RETURN IT'S TYPE_CLASS.
	//OTHERWISE - RETURN NULL
	public TYPE_CLASS getCurrClass(){

		SYMBOL_TABLE_ENTRY e;
		String name="SCOPE-BOUNDARY";		
		for (e = table[hash(name)]; e != null; e = e.next)
		{	
			if ( (name.equals(e.name))&& (e.type.name.equals("class")))
			{
				TYPE_FOR_SCOPE_BOUNDARIES scopeType=(TYPE_FOR_SCOPE_BOUNDARIES)e.type;
				TYPE_CLASS classType= (TYPE_CLASS)scopeType.type;
				return classType;
			}
		}
		
		return null;
		
		
	}
	
	
	public TYPE_FUNCTION getCurrFunc() {
		SYMBOL_TABLE_ENTRY e;
		String name="SCOPE-BOUNDARY";		
		for (e = table[hash(name)]; e != null; e = e.next)
		{	
			if ( (name.equals(e.name))&& (e.type.name.equals("function")))
			{
				TYPE_FOR_SCOPE_BOUNDARIES scopeType=(TYPE_FOR_SCOPE_BOUNDARIES)e.type;
				TYPE_FUNCTION funcType= (TYPE_FUNCTION)scopeType.type;
				return funcType;
			}
		}
		
		return null;
	}
	
	
	
	public TYPE_FOR_SCOPE_BOUNDARIES getCurClassBoundary() {
		SYMBOL_TABLE_ENTRY e;
		String name="SCOPE-BOUNDARY";		
		for (e = table[hash(name)]; e != null; e = e.next)
		{	
			if ( (name.equals(e.name))&& (e.type.name.equals("class")))
			{
				TYPE_FOR_SCOPE_BOUNDARIES scopeType=(TYPE_FOR_SCOPE_BOUNDARIES)e.type;
				return scopeType;
			}
		}
		
		return null;
	}
	
	//FIND IN SCOPES BUT NOT OUTSIDE OF CLASS SCOPE
	public TYPE findInClassScope(String name) {
		TYPE_FOR_SCOPE_BOUNDARIES currClass= getCurClassBoundary();
		if (currClass==null){
			return null;
		}
		
		int classScopeNum=currClass.scopeNum;
		SYMBOL_TABLE_ENTRY e;
		for (e = table[hash(name)]; e != null; e = e.next)
		{	
			if ( (name.equals(e.name))&& (e.scopeNum>=classScopeNum))
			{
				return e.type;
			}
		}
		return null;
				
				
				
	}

	
	
	

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope(String name, TYPE type)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		
		this.scopesCounter++;
		
		
		
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES(name, type, this.scopesCounter));

		
		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;
		
		this.scopesCounter--;
		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
	}
	
	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();
			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/
			instance.enter("void",TYPE_VOID.getInstance());
			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						TYPE_INT.getInstance(),
						null)));
			
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(
						TYPE_VOID.getInstance(),
						"PrintString",
						new TYPE_LIST(
							TYPE_STRING.getInstance(),
							null)));
			
		}
		return instance;
	}
	
	
	
	/*Check if current scope it the global scope*/
	public boolean isGlobalScope() {
		if (this.scopesCounter==1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*find TYPE - when we search a type, we want to make suer that the name 
	 * that was founded is a name of type and not a name of a variable.
	 * In addition, type void is allowed only as a return type of a function*/
	public TYPE findType(String name) {
		
		if (name=="void") {return null;}
		
		TYPE type=find(name);
		
		if (type==null) { return null;}
		
		if (  !(type.isVar())  &&  !(type instanceof TYPE_FUNCTION)) {
			return type;
		}
		return null;
		
	}
	
	/*find Return type of a function. Void type is allowed*/
	public TYPE findReturnType(String name) {
		if (name=="void") {
			return TYPE_VOID.getInstance();
		}
		return findType(name);
		
		
		
	}
	
	//return true if current scope is a global scope, otherwise return false
	public String getCurrScopeType() {
		SYMBOL_TABLE_ENTRY e;
		String name="SCOPE-BOUNDARY";
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type.name;
			}
		}
		
		return null;
	}
	
	
	
	
	
}
