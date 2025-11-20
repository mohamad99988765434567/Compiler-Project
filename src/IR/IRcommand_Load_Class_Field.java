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


public class IRcommand_Load_Class_Field extends IRcommand {
	TEMP dst;
	int fieldIndex;
	
	
	public IRcommand_Load_Class_Field(TEMP dst, int fieldIndex) {
		this.dst=dst;
		this.fieldIndex=fieldIndex;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadClassFieldInFunc(dst,fieldIndex);
	}
}
