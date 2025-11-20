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


public class IRcommand_callFunc extends IRcommand {
	TEMP dst; //will save the result
	String funcName; //method name
	List <TEMP> params; //params list (reversed order)
	
	public IRcommand_callFunc(TEMP dst, String funcName, List <TEMP> params){
		if(dst!=null) {
			this.dst=dst;
		}
		
		this.funcName=funcName;
		this.params=params;
	}
	
	public void MIPSme()
	{
		System.out.println("we are here (ircommand callFunc)");
		for (TEMP t : params) { //push args
			MIPSGenerator.getInstance().pushStack(t);
		}
		
		MIPSGenerator.getInstance().jal(funcName);
		
		int pop=this.params.size();
		if(pop>0) {
			MIPSGenerator.getInstance().popStack(pop);
		}
		if(dst!=null) {
			MIPSGenerator.getInstance().loadReturnedValue(dst);
		}
		
	}
}
