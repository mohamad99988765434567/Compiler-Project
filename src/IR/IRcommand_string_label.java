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

public class IRcommand_string_label extends IRcommand {
	
	String label1;
	String label2;
	
	public IRcommand_string_label(String label1, String label2) {
		this.label1=label1;
		this.label2=label2;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().addStringLabel(this.label1, this.label2);
	}
	
	
}
