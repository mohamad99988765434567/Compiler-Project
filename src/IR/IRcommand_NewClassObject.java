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
import TYPES.*;

import java.util.LinkedHashMap;
import java.util.Set;

import MIPS.*;

public class IRcommand_NewClassObject extends IRcommand {
	
	TEMP dst;
	String className;
	int sizeInBytes;
	TYPE_CLASS tclass;
	
	public IRcommand_NewClassObject(TEMP dst, String className, int sizeInBytes ,TYPE_CLASS tclass){
		
		this.dst=dst;
		this.className=className;
		this.sizeInBytes=sizeInBytes;
		this.tclass=tclass;
		
		
	}
	 public void MIPSme(){
		 	int vtable=0;
		 	if(tclass.Allmethods.size()>0) {
		 		vtable=1;
		 	}
	        MIPSGenerator.getInstance().newClass(dst,className,sizeInBytes,vtable);
	        
	        
	    }
	
	
	
	
}
