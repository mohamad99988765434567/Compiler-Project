/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/**********************/
/* SYMBOL TABLE ENTRY */
/**********************/
public class SYMBOL_TABLE_ENTRY
{
	/*********/
	/* index */
	/*********/
	int index;
	
	/********/
	/* name */
	/********/
	public String name;

	/******************/
	/* TYPE value ... */
	/******************/
	public TYPE type;

	/*********************************************/
	/* prevtop and next symbol table entries ... */
	/*********************************************/
	public SYMBOL_TABLE_ENTRY prevtop;
	public SYMBOL_TABLE_ENTRY next;

	/****************************************************/
	/* The prevtop_index is just for debug purposes ... */
	/****************************************************/
	public int prevtop_index;
	
	public int scopeNum; //in which scope
	
	public Metadata metadata;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public SYMBOL_TABLE_ENTRY(
		String name,
		TYPE type,
		int index,
		SYMBOL_TABLE_ENTRY next,
		SYMBOL_TABLE_ENTRY prevtop,
		int prevtop_index,
		int scopeNum,
		Metadata metadata)
	{
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
		this.scopeNum=scopeNum;
		this.metadata=metadata;
	}
}
