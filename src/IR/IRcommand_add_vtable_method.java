/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

import java.util.LinkedHashMap;

import MIPS.*;

public class IRcommand_add_vtable_method extends IRcommand {
	
	public String methodName;
	
	public IRcommand_add_vtable_method(String methodName) {
		this.methodName=methodName;
		
	}
	
	public void MIPSme()
	{
		//fix - add a lablel with the class name, and then add  labels for the methods.
		
		MIPSGenerator.getInstance().addToVtable(methodName);
		
		
	}
	
	
	
	
	
}
