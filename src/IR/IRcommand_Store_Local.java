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

public class IRcommand_Store_Local extends IRcommand {
	int offset;
	TEMP src;
	
	public IRcommand_Store_Local(int offset,TEMP src)
	{
		this.offset      = offset;
		this.src = src;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeLocal(offset,src);
	}

}
