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

public class IRcommand_define_exit_out_of_bound extends IRcommand {

	public IRcommand_define_exit_out_of_bound() {
		
	}
	
	
	public void MIPSme(){
		
		MIPSGenerator.getInstance().label("exit_out_of_bound");
		MIPSGenerator.getInstance().label("exit_out_of_bound_Epilogue");
		MIPSGenerator.getInstance().print_string("string_access_violation");
		MIPSGenerator.getInstance().exit();


		
	}
}
