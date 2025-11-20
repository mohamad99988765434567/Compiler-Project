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

public class IRcommand_Add_Immediate extends IRcommand {
	
	public TEMP dst;
	public TEMP t1;
	public int num;
	
	public IRcommand_Add_Immediate(TEMP dst,TEMP t1, int num)
	{
		this.dst = dst;
		this.t1 = t1;
		this.num = num;
	}
	
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().addi(dst,t1,num);
		
	}
}
