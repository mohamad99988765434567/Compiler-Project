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
import MIPS.*;

public class IRcommand_Func_Return extends IRcommand {
	
	TEMP returnValue;
	
	public IRcommand_Func_Return(TEMP returnValue) {
		this.returnValue=returnValue;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().returnValue(returnValue);
		
		
		
	}

}
