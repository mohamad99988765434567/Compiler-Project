package AST;

import java.util.Map;

import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_LIST;


public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	public int lineNum;
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
	
	
	
	public int compareParams(TYPE_LIST paramlist1, TYPE_LIST ParamList2) {
		for (TYPE_LIST ParamList1=paramlist1; ParamList1!=null; ParamList1=ParamList1.tail) {
			if (ParamList2==null) { //error: different number of parameters
				return 0;
			}
			if (!((ParamList2.head.name).equals(ParamList1.head.name))) {// error: different types of parameters
				return 0;
			}
		
			ParamList2=ParamList2.tail;
		}
		
		//out of loop - ParamList1 is null
		if (ParamList2!=null) { //error: different number of parameters
			return 0;
		}
		
		//success
		return 1; 
		
	}
	
	//look for name in class hierarchy
	
	public TYPE searchInClassHierarchy(String name,TYPE_CLASS startClass) {
		for (TYPE_CLASS c=startClass; c!=null; c=c.father) {
			TYPE t=c.data_members.get(name);
			if (t!=null) {
				return t;
			}
		}
	//name was not found	
		return null;
	}
	
	
	public AST_EXP_LIST reverse(AST_EXP_LIST l) {
		AST_EXP_LIST reversed_list=null;
		AST_EXP t;
		while (l!=null) {
			t=l.head;
			reversed_list=new AST_EXP_LIST(t,reversed_list,0);
			l=l.tail;
			
		}
		return reversed_list;
		
	}
}
