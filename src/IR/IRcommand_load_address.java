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


public class IRcommand_load_address extends IRcommand {
	
	TEMP dst;
	String label;
	
	public IRcommand_load_address(TEMP dst, String label) {
		this.dst=dst;
		this.label=label;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadAddress(dst,label);
	}
	

}
