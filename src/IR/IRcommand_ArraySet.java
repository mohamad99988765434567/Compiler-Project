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


public class IRcommand_ArraySet extends IRcommand{
    /* write to an array */
    public TEMP value; /* value we wants to write to array */
    public TEMP address;   /* address of the array */
    public TEMP offset; /* offset by number of elements  */

    public IRcommand_ArraySet(TEMP value,TEMP address, TEMP offset){
        this.value = value;
        this.address = address;
        this.offset = offset;
    }

//    public void MIPSme(){
//    	String label_abort        = getFreshLabel("abort");
//    	String label_done		= getFreshLabel("done");
//
//        MIPSGenerator.getInstance().arraySet(value, address, offset, label_abort, label_done); //value, address, offset
//    }
    
    public void MIPSme(){

        MIPSGenerator.getInstance().arraySet(value, address, offset); //value, address, offset
    }
    
}
