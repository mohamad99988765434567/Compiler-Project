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


public class IRcommand_global_var extends IRcommand {
	
	String label;
	int value;
	
	public IRcommand_global_var(String label, int value) {
		this.label=label;
		this.value=value;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().addGlobalInt(label, value);
		
		
		
	}
}
