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

public class IRcommand_concat_string_loop extends IRcommand {
	
	String startLoopLabel;
	String endLoopLabel;
	
	
	public IRcommand_concat_string_loop(String startLoopLabel, String endLoopLabel){
		this.startLoopLabel=startLoopLabel;
		this.endLoopLabel=endLoopLabel;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().concat_string_loop(startLoopLabel, endLoopLabel);
	}
}
