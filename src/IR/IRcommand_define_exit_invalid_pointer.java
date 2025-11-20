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

public class IRcommand_define_exit_invalid_pointer extends IRcommand {
	
	public IRcommand_define_exit_invalid_pointer() {
		
	}
	
	public void MIPSme(){
		
		MIPSGenerator.getInstance().label("exit_invalid_ptr_dref");
		MIPSGenerator.getInstance().label("exit_invalid_ptr_dref_Epilogue");
		MIPSGenerator.getInstance().print_string("string_invalid_ptr_dref");
		MIPSGenerator.getInstance().exit();


		
	}

}
