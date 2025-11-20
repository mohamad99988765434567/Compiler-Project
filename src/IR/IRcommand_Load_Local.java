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


public class IRcommand_Load_Local extends IRcommand {
	
	TEMP dst;
	int offset;
	
	public IRcommand_Load_Local(TEMP dst, int offset) {
		this.dst=dst;
		this.offset=offset;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadLocal(dst,offset);
	}
	
}
