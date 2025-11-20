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

import java.util.List;

import MIPS.*;

public class IRcommand_virtual_call extends IRcommand {
	
	TEMP dst; //will save the result
	TEMP address; //address of class object
	int methodOffset; //method name
	List <TEMP> params; //params list (reversed order)
	
	public IRcommand_virtual_call(TEMP dst, TEMP address,  int methodOffset, List <TEMP> params) {
		if (dst!=null) { //only if called from AST_EXP_VAR_DOT_METHOD
			this.dst=dst;
			
		}
		this.address=address;
		this.methodOffset=methodOffset;
		this.params=params;
		
		
	}
	
	public void MIPSme()
	{
		for (TEMP t : params) { //push args
			MIPSGenerator.getInstance().pushStack(t);
		}
		MIPSGenerator.getInstance().beqz(address, "exit_invalid_ptr_dref");
		MIPSGenerator.getInstance().pushStack(this.address);//push address
		MIPSGenerator.getInstance().callVirtualMethod(address,methodOffset);
		int pop=this.params.size();
		if(pop>0) {
			MIPSGenerator.getInstance().popStack(pop);
		}
		if(dst!=null) {
			MIPSGenerator.getInstance().loadReturnedValue(dst);
		}

	}

}
