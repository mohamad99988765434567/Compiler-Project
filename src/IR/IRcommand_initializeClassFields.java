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
import TYPES.TYPE;
import TYPES.TYPE_CLASS_VAR_DEC;

import java.util.LinkedHashMap;
import java.util.Set;

import MIPS.*;
import AST.*;

public class IRcommand_initializeClassFields extends IRcommand{
	
	TEMP dst;
	LinkedHashMap<String, TYPE> fields;
	
	public IRcommand_initializeClassFields(TEMP dst, LinkedHashMap<String, TYPE> fields) {
		this.dst=dst;
		this.fields=fields;
		
		
	}
	
	
	
	public void MIPSme(){
		//LinkedHashMap<String, TYPE> fields//=tclass.Allfields;
		 //LinkedHashMap<TEMP, Integer> initialized =new LinkedHashMap<>();
		Set<String> keys=fields.keySet();
		 //TEMP t;
		int i=0;
		for(String key : keys) {
			i++;
			TYPE_CLASS_VAR_DEC ctype= (TYPE_CLASS_VAR_DEC)fields.get(key);
			if (ctype.initialValue!=null) {
				if (ctype.initialValue instanceof AST_EXP_INT) {
					MIPSGenerator.getInstance().initialize_IntField(dst, ((AST_EXP_INT)ctype.initialValue).value, i);
				}
			
				if (ctype.initialValue instanceof AST_EXP_STRING) {
					String str_const = IRcommand.getFreshLabel("str_const");
					MIPSGenerator.getInstance().
					initialize_StringField(dst, ((AST_EXP_STRING)ctype.initialValue).value, i, str_const);

				}
			
				if (ctype.initialValue instanceof AST_EXP_NIL) {
					MIPSGenerator.getInstance().initialize_NILField(dst, i);
					
				}
				
			
			}

		}
	}
	
	
	
	

}
