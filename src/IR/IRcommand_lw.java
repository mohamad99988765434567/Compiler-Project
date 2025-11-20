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

public class IRcommand_lw extends IRcommand{
	TEMP dst;
	TEMP source;
	int offset;
	
	public IRcommand_lw(TEMP dst, TEMP source, int offset) {
		this.dst=dst;
		this.source=source;
		this.offset=offset;
	}
	
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadWord(dst,source,offset);
	} 
}
