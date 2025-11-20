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


public class IRcommand_define_str_concationation_func extends IRcommand {
	
	public IRcommand_define_str_concationation_func() {
		
	}
	
	public void MIPSme()
	{
		String label_len_str1_loop= getFreshLabel("len_str1_loop");
		String label_len_str2_loop=getFreshLabel("len_str2_loop");
		String label_allocate_space=getFreshLabel("allocate_space");
		String label_write_str1_loop=getFreshLabel("write_str1_loop");
		String label_write_str2_loop=getFreshLabel("write_str2_loop");
		String label_end_concat=getFreshLabel("end_concat");
		
		TEMP str1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP str2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP length=TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP character=TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP concat=TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP result=TEMP_FACTORY.getInstance().getFreshTEMP();
		
		MIPSGenerator.getInstance().label("str_concatination");
		MIPSGenerator.getInstance().prologue("str_concatination", 0);
		MIPSGenerator.getInstance().loadParam(str1, 1);
		MIPSGenerator.getInstance().loadParam(str2, 2);
		MIPSGenerator.getInstance().li(length, 0);
		
		
		//add to str1.length() to length
		MIPSGenerator.getInstance().label(label_len_str1_loop);
		MIPSGenerator.getInstance().lb(character,str1);
		MIPSGenerator.getInstance().beqz(character,label_len_str2_loop);
		MIPSGenerator.getInstance().addiu(str1,str1,1);
		MIPSGenerator.getInstance().addiu(length,length,1);
		MIPSGenerator.getInstance().jump(label_len_str1_loop);
		
		
		
		//add to str2.length() to length
		MIPSGenerator.getInstance().label(label_len_str2_loop);
		MIPSGenerator.getInstance().lb(character,str2);
		MIPSGenerator.getInstance().beqz(character,label_allocate_space);
		MIPSGenerator.getInstance().addiu(str2,str2,1);
		MIPSGenerator.getInstance().addiu(length,length,1);
		MIPSGenerator.getInstance().jump(label_len_str2_loop);
		
		//allocate space on heap for the concatenated string
		MIPSGenerator.getInstance().label(label_allocate_space);
		MIPSGenerator.getInstance().addiu(length,length,1); //string should be null terminated
		MIPSGenerator.getInstance().malloc(concat, length);
		MIPSGenerator.getInstance().move(result, concat);
		
		MIPSGenerator.getInstance().loadParam(str1, 1); //now str1 points to the start of string1
		MIPSGenerator.getInstance().loadParam(str2, 2); //now str2 points to the start of string2

		
		
		//write str1 to the concatenated string
		MIPSGenerator.getInstance().label(label_write_str1_loop);
		MIPSGenerator.getInstance().lb(character,str1);
		MIPSGenerator.getInstance().sb(character,concat);
		MIPSGenerator.getInstance().beqz(character,label_write_str2_loop);
		MIPSGenerator.getInstance().addiu(str1,str1,1);
		MIPSGenerator.getInstance().addiu(concat,concat,1);
		MIPSGenerator.getInstance().jump(label_write_str1_loop);
		
		
		
		//write str2 to the concatenated string
		MIPSGenerator.getInstance().label(label_write_str2_loop);
		MIPSGenerator.getInstance().lb(character,str2);
		MIPSGenerator.getInstance().sb(character,concat);
		MIPSGenerator.getInstance().beqz(character,label_end_concat);
		MIPSGenerator.getInstance().addiu(str2,str2,1);
		MIPSGenerator.getInstance().addiu(concat,concat,1);
		MIPSGenerator.getInstance().jump(label_write_str2_loop);
		
		
	
		//end
		MIPSGenerator.getInstance().label(label_end_concat);
		MIPSGenerator.getInstance().returnValue(result);
		MIPSGenerator.getInstance().epilogue("str_concatination");


		





		
	
	}
	
	
	
}
