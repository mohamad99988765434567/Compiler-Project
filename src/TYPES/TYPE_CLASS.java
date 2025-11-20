package TYPES;

import java.util.LinkedHashMap;
import java.util.List;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public LinkedHashMap<String, TYPE> data_members;
	public LinkedHashMap<String, TYPE> Allfields;
	public LinkedHashMap<String, String> Allmethods;
	public List<String> fieldIndex;
	public List<String> methodIndex;

	//countNumberofFields
	public int totalFields;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name, LinkedHashMap<String, TYPE> data_members) //or TYPE_LISt?
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.totalFields=0;
		this.Allfields=null;
		this.Allmethods=null;
		this.fieldIndex=null;
		this.methodIndex=null;
	}
	
	public void addToMap(String name, TYPE t) {
		this.data_members.put(name, t);
	}
	
	public int incField() {
		this.totalFields++;
		return this.totalFields;
	}
	
	
}
