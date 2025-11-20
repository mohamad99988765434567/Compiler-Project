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

public class IRcommand_Binop_EQ_Strings extends IRcommand {
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;
	
	
	public IRcommand_Binop_EQ_Strings(TEMP dst,TEMP t1,TEMP t2)
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
		/*******************************/
		/* [1] Allocate 3 fresh labels */
		/*******************************/
		String label_end        = getFreshLabel("end");
		//String label_AssignOne  = getFreshLabel("AssignOne");
		String label_AssignZero = getFreshLabel("AssignZero");
		String label_str_eq_loop = getFreshLabel("str_eq_loop");
		
		
		MIPSGenerator.getInstance().li(dst,1);
		MIPSGenerator.getInstance().move_to_Sregister(0, t1);
		MIPSGenerator.getInstance().move_to_Sregister(1, t2);
		MIPSGenerator.getInstance().label(label_str_eq_loop);
		MIPSGenerator.getInstance().string_eq_loop(label_str_eq_loop, label_end, label_AssignZero);
		
		MIPSGenerator.getInstance().label(label_AssignZero);
		MIPSGenerator.getInstance().li(dst,0);
		
		//this is the end
		MIPSGenerator.getInstance().label(label_end);

		
		
		
		
		
		
	}
}
