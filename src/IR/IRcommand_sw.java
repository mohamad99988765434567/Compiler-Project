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

public class IRcommand_sw extends IRcommand {
	TEMP source;
	TEMP dst;
	int offset;
	
	public IRcommand_sw(TEMP source, TEMP dst, int offset) {
		this.source=source;
		this.dst=dst;
		this.offset=offset;
	}
	
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeWord(source,dst,offset);
	} 

}
