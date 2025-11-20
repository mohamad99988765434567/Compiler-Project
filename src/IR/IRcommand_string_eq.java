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

public class IRcommand_string_eq extends IRcommand {
	
	TEMP dst;
	TEMP str1;
	TEMP str2;
	
	
	public IRcommand_string_eq(TEMP dst, TEMP str1, TEMP str2){
		this.dst=dst;
		this.str1=str1;
		this.str2=str2;
		
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().pushStack(str2);
		MIPSGenerator.getInstance().pushStack(str1);
		MIPSGenerator.getInstance().jal("str_eq");
		MIPSGenerator.getInstance().popStack(2);
		MIPSGenerator.getInstance().loadReturnedValue(dst);


	}
	
	

}
