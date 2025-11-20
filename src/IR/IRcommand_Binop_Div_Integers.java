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

public class IRcommand_Binop_Div_Integers extends IRcommand{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;
	
	public IRcommand_Binop_Div_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		
//		String label_div_by_zero = IRcommand.getFreshLabel("div_by_zero");
//		String label_end = IRcommand.getFreshLabel("end");
//		
//		MIPSGenerator.getInstance().beqz(t2, label_div_by_zero); //if t2==0, there is an illegal div by zero
//		
//		MIPSGenerator.getInstance().div(dst,t1,t2); //divide
//		MIPSGenerator.getInstance().jump(label_end); //jump to end
//		
//		MIPSGenerator.getInstance().label(label_div_by_zero); //division by zero - print error and exit
//		MIPSGenerator.getInstance().print_string("string_illegal_div_by_0");
//		MIPSGenerator.getInstance().exit();
//		
//		MIPSGenerator.getInstance().label(label_end); //this is the end
		
		
		
		MIPSGenerator.getInstance().beqz(t2, "exit_div_by_zero"); //if t2==0, there is an illegal div by zero
		
		MIPSGenerator.getInstance().div(dst,t1,t2); //divide
		
		
		
		

		
	}
}
