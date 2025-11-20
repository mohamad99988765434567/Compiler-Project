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
public class IRcommand_define_exit_div_by_zero extends IRcommand {
	
	public IRcommand_define_exit_div_by_zero() {
	}
	
	
	
	public void MIPSme(){
		
		MIPSGenerator.getInstance().label("exit_div_by_zero");
		MIPSGenerator.getInstance().label("exit_div_by_zero_Epilogue");
		MIPSGenerator.getInstance().print_string("string_illegal_div_by_0");
		MIPSGenerator.getInstance().exit();



		
	}
	
}
