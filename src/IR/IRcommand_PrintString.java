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

public class IRcommand_PrintString extends IRcommand {
	String label=null;
	TEMP t=null;
	
	public IRcommand_PrintString(String label) {
		this.label=label;
	}
	
	public IRcommand_PrintString(TEMP t) {
		this.t=t;;
	}
	
	
	
	public void MIPSme()
	{
		if(t==null) {
			MIPSGenerator.getInstance().print_string(label);
		}
		
		else {
			MIPSGenerator.getInstance().print_string(t);
		}
		
	}

}
