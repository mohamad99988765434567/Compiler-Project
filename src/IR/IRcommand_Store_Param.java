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

public class IRcommand_Store_Param extends IRcommand {
	TEMP source;
	int offset;
	
	public IRcommand_Store_Param(TEMP source, int offset) {
		this.source=source;
		this.offset=offset;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeParam(source,offset);
	}
}
