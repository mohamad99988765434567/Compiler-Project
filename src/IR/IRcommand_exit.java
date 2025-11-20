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


public class IRcommand_exit extends IRcommand {
	
	public IRcommand_exit() {}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().exit();
		
	}

}
