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

public class IRcommand_space extends IRcommand {
	
	String label;
	int size;
	
	public IRcommand_space(String label, int size) {
		this.label=label;
		this.size=size;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().space(label,size);
		
	}

}
