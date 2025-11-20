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

public class IRcommand_move_to_Sregister extends IRcommand {
	
	
	int SregisterIndex;
	TEMP t;
	
	public IRcommand_move_to_Sregister(int SregisterIndex, TEMP t) {
		this.SregisterIndex=SregisterIndex;
		this.t=t;
		
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().move_to_Sregister(SregisterIndex,t);
	}

}
