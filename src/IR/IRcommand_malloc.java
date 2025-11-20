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

public class IRcommand_malloc extends IRcommand {
	
	TEMP dst;
	TEMP size;
	
	public IRcommand_malloc(TEMP dst, TEMP size) {
		this.dst=dst;
		this.size=size;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().malloc(dst,size);
	}
	
}
