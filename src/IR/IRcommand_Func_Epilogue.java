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

public class IRcommand_Func_Epilogue extends IRcommand {

	String funcName;
	
	public IRcommand_Func_Epilogue(String funcName) {this.funcName=funcName;}
	
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().epilogue(funcName);
		
	}
}
