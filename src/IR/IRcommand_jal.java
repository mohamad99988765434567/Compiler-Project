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

public class IRcommand_jal extends IRcommand {
	
	String funcLabel;
	
	public IRcommand_jal(String funcLabel) {
		this.funcLabel=funcLabel;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jal(funcLabel);
	}
	
}
