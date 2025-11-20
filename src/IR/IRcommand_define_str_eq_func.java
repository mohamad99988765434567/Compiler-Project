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

public class IRcommand_define_str_eq_func extends IRcommand {
	
	public IRcommand_define_str_eq_func() {
		
	}
	
	public void MIPSme()
	{
		
		TEMP str1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP str2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP result = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP char1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP char2 =  TEMP_FACTORY.getInstance().getFreshTEMP();
		
		String label_end        = getFreshLabel("str_eq_end");
		String label_AssignZero = getFreshLabel("AssignZero");
		String label_str_eq_loop = getFreshLabel("str_eq_loop");

		MIPSGenerator.getInstance().label("str_eq");
		MIPSGenerator.getInstance().prologue("str_eq", 0);
		MIPSGenerator.getInstance().loadParam(str1, 1);
		MIPSGenerator.getInstance().loadParam(str2, 2);
		MIPSGenerator.getInstance().li(result, 1);
		
		//loop
		MIPSGenerator.getInstance().label(label_str_eq_loop);
		MIPSGenerator.getInstance().lb(char1, str1);
		MIPSGenerator.getInstance().lb(char2, str2);
		MIPSGenerator.getInstance().bne(char1, char2, label_AssignZero);
		MIPSGenerator.getInstance().beqz(char1, label_end);
		MIPSGenerator.getInstance().addiu(str1,str1,1);
		MIPSGenerator.getInstance().addiu(str2,str2,1);
		MIPSGenerator.getInstance().jump(label_str_eq_loop);
		
		//assign zero
		MIPSGenerator.getInstance().label(label_AssignZero);
		MIPSGenerator.getInstance().li(result, 0);
		
		//end
		MIPSGenerator.getInstance().label(label_end);
		MIPSGenerator.getInstance().returnValue(result);
		MIPSGenerator.getInstance().epilogue("str_eq");
		




		




		

		
		

		
		
	}

}
