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


public class IRcommand_pop_stack extends IRcommand {
	
	int numberOfItems;
	
	public IRcommand_pop_stack(int numberOfItems ) {
		this.numberOfItems=numberOfItems;
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().popStack(numberOfItems);
	}
	

}
