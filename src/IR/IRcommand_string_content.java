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

public class IRcommand_string_content extends IRcommand {
	
	String label;
	String content;
	
	public IRcommand_string_content(String label, String content) {
		this.label=label;
		this.content=content;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().addStringContent(this.label, this.content);
	}
	
	

}
