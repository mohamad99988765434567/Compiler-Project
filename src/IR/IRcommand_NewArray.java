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

public class IRcommand_NewArray extends IRcommand{

    public TEMP t1;  /* hold the address of new array */
    public TEMP t2; /* number of elements */

    public IRcommand_NewArray(TEMP t1,TEMP t2){
        this.t1 = t1;
        this.t2 = t2;
    }

    public void MIPSme(){
        MIPSGenerator.getInstance().newArray(t1, t2);
    }
}
