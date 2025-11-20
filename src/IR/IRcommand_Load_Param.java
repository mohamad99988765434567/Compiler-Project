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


public class IRcommand_Load_Param extends IRcommand {
	
	TEMP dst;
	int offset;
	
	public IRcommand_Load_Param(TEMP dst, int offset) {
		this.dst=dst;
		this.offset=offset;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadParam(dst,offset);
	}
}
