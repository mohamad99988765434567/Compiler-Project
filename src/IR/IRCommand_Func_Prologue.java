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

public class IRCommand_Func_Prologue extends IRcommand {
	
	String funcName;
	public int locals;
	
	public IRCommand_Func_Prologue(String funcName,int locals)
	{
		this.funcName=funcName;
		this.locals=locals;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().prologue(funcName,locals);
		
		
	}
}
