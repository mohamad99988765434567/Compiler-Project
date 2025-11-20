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

public class IRcommand_fieldSet extends IRcommand {
	TEMP value; //value we want to store
	TEMP address;
	int index;
	
	public IRcommand_fieldSet(TEMP value,TEMP address,int index ) {
		this.value=value;
		this.address=address;
		this.index=index;
		
	}
	
//	public void MIPSme()
//	{
//		String abort        = getFreshLabel("abort");
//		String end			= getFreshLabel("end");
//
//		
//		MIPSGenerator.getInstance().beqz(address, abort);
//		MIPSGenerator.getInstance().storeWord(value, address, index*4);
//		MIPSGenerator.getInstance().jump(end);
//		MIPSGenerator.getInstance().label(abort);
//		MIPSGenerator.getInstance().print_string("string_invalid_ptr_dref");
//		MIPSGenerator.getInstance().exit();
//		MIPSGenerator.getInstance().label(end);
//	
//	}
	
	
	public void MIPSme()
	{

		
		MIPSGenerator.getInstance().beqz(address, "exit_invalid_ptr_dref");
		MIPSGenerator.getInstance().storeWord(value, address, index*4);
		
	
	}

}
