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

public class IRcommand_Binop_limit extends IRcommand {
	
	TEMP dst;
	
	public IRcommand_Binop_limit(TEMP dst) {
		this.dst=dst;
	}
	
	
	public void MIPSme()
	{
		int min=-(int)Math.pow(2, 15);
		int max=(int)Math.pow(2, 15)-1;
		
		String label_check_overflow = IRcommand.getFreshLabel("check_overflow");
		String label_end =IRcommand.getFreshLabel("end");
		TEMP limit=TEMP_FACTORY.getInstance().getFreshTEMP();
		
		
		//load -2^15 to limit
		MIPSGenerator.getInstance().li(limit, min);
		
		//if dst>=-2^15 - there is no underflow, check overflow
		MIPSGenerator.getInstance().bge(dst, limit, label_check_overflow);
		MIPSGenerator.getInstance().li(dst, min); //there is an underflow, assign min value
		MIPSGenerator.getInstance().jump(label_end); //jump to end
		
		//check overflow
		MIPSGenerator.getInstance().label(label_check_overflow);
		
		//load 2^15-1 to limit
		MIPSGenerator.getInstance().li(limit, max);
		
		//if dst<=2^15-1 there is no overflow, end
		MIPSGenerator.getInstance().ble(dst, limit, label_end); 
		MIPSGenerator.getInstance().li(dst, max); //there is an overflow, assign max value
		
		MIPSGenerator.getInstance().label(label_end);

		
	}
	
	

}
