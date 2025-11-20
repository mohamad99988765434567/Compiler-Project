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

public class IRcommand_fieldAccess extends IRcommand {
	TEMP dst;
	TEMP address;
	int index;
	
	public IRcommand_fieldAccess(TEMP dst,TEMP address,int index ) {
		this.dst=dst;
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
//		MIPSGenerator.getInstance().loadWord(dst, address, index*4);
//		MIPSGenerator.getInstance().jump(end);
//		MIPSGenerator.getInstance().label(abort);
//		MIPSGenerator.getInstance().print_string("string_invalid_ptr_dref");
//		MIPSGenerator.getInstance().exit();
//		MIPSGenerator.getInstance().label(end);
//		
//		
//		
//	}
	
	
	public void MIPSme()
	{

		
		MIPSGenerator.getInstance().beqz(address, "exit_invalid_ptr_dref");
		MIPSGenerator.getInstance().loadWord(dst, address, index*4);
		
		
		
		
	}
	
	

}
