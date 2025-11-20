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

public class IRcommand_Store_Class_Field extends IRcommand {
	TEMP source;
	int fieldIndex;
	
	
	public IRcommand_Store_Class_Field(TEMP source, int fieldIndex) {
		this.source=source;
		this.fieldIndex=fieldIndex;
	}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeClassFieldInFunc(source,fieldIndex);
	}
	
	
}
