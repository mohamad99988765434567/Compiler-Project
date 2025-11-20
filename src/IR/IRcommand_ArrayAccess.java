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

public class IRcommand_ArrayAccess extends IRcommand{

        public TEMP t0;  /* hold the element from the array */
        public TEMP t1; /* address of the array  */
        public TEMP t2; /* offset by number of elements */

        public IRcommand_ArrayAccess(TEMP t0,TEMP t1, TEMP t2){
            this.t0 = t0;
            this.t1 = t1;
            this.t2 = t2;
        }


//        public void MIPSme(){
//        	
//        	String label_abort        = getFreshLabel("abort");
//        	String label_done		= getFreshLabel("done");
//            MIPSGenerator.getInstance().arrayAccess(t0, t1, t2, label_abort, label_done);
//
//        }
        
        public void MIPSme(){
        	
            MIPSGenerator.getInstance().arrayAccess(t0, t1, t2);

        }
        
}
