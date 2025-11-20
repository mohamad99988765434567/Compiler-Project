/***********/
/* PACKAGE */
/***********/
package MIPS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.Scanner;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;
	private PrintWriter dataWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		label("main");
		jal("user_main");
		exit();
		fileWriter.close();
		dataWriter.close();
		createFile();
	}
	
	
	public void createFile() {
		String dirname="./output/";
		String filename=String.format("MIPS_DRAFT.txt");
		String dataname=String.format("MIPS_DATA.txt");
		String textname=String.format("MIPS_TEXT.txt");
		String line;
		FileReader file_reader;
		try {
			file_reader= new FileReader(dirname+dataname); 
			Scanner s1= new Scanner(file_reader);
			//File f=new File(dirname+filename);
			PrintWriter file_writer=new PrintWriter(dirname+filename);
			while (s1.hasNextLine()) {
				line=s1.nextLine();
				file_writer.println(line);
				file_writer.flush();
			}
			s1.close();
			file_reader= new FileReader(dirname+textname); 
			Scanner s2= new Scanner(file_reader);
			while (s2.hasNextLine()) {
				line=s2.nextLine();
				file_writer.println(line);
				file_writer.flush();
			}
			s2.close();
			file_writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	public void allocate(String var_name)
	{
		//fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d, %s\n",idxdst,var_name); //global_ prefix ? i don't think that we need it
	}
	
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		//fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);	
		fileWriter.format("\tsw Temp_%d,%s\n",idxsrc,var_name);		

	}
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d, %d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void label(String inlabel)
	{
//		if (inlabel.equals("main"))
//		{
//			fileWriter.format(".text\n");
//			fileWriter.format("%s:\n",inlabel);
//		}
//		else
		//{
			fileWriter.format("%s:\n",inlabel);
		//}
	}	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	
	
	
	//Additions methods
	
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbgt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	
	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tble Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	
	public void prologue(String funcName,int locals) {
		fileWriter.format("%s:\n", funcName+"_Prologue");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $ra, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $fp, 0($sp)\n");
		fileWriter.format("\tmove $fp, $sp\n");
		//backup_registers/////////////////////////////
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t0, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t1, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t2, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t3, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t4, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t5, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t6, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t7, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t8, 0($sp)\n");
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw $t9, 0($sp)\n");
		///////////////////////////////////////////////
		if (locals>0) {
			fileWriter.format("\taddi $sp, $sp, %d\n", -4*locals);
		}
	}
	
	
	public void epilogue(String funcName) {
		fileWriter.format("%s:\n", funcName+"_Epilogue");
		fileWriter.format("\tmove $sp, $fp\n");
		//backup_registers/////////////////////////////
		fileWriter.format("\tlw $t0, -4($sp)\n");
		fileWriter.format("\tlw $t1, -8($sp)\n");
		fileWriter.format("\tlw $t2, -12($sp)\n");
		fileWriter.format("\tlw $t3, -16($sp)\n");
		fileWriter.format("\tlw $t4, -20($sp)\n");
		fileWriter.format("\tlw $t5, -24($sp)\n");
		fileWriter.format("\tlw $t6, -28($sp)\n");
		fileWriter.format("\tlw $t7, -32($sp)\n");
		fileWriter.format("\tlw $t8, -36($sp)\n");
		fileWriter.format("\tlw $t9, -40($sp)\n");
		///////////////////////////////////////////////
		fileWriter.format("\tlw $fp, 0($sp)\n");
		fileWriter.format("\tlw $ra, 4($sp)\n");
		fileWriter.format("\taddi $sp, $sp, 8\n");
		fileWriter.format("\tjr $ra\n");
		
	}
	
	public void returnValue(TEMP returnValue) {
		int i1 =returnValue.getSerialNumber();
		fileWriter.format("\tmove $v0, Temp_%d\n", i1);
		
	}
	
	public void dataLabel(String label_name) {
		dataWriter.format(".data\n");
		dataWriter.format("\t%s:\n", label_name);
	}
	
	public void addToVtable(String methodName) {
		dataWriter.format("\t.word %s\n", methodName);
	}
	
	public void storeLocal(int offset ,TEMP src) {
		//System.out.println("SERIAL NUMBER:" +src.getSerialNumber());
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d, %d($fp)\n",idxsrc,-4*(offset+10));
	}
	
	public void addStringContent(String label, String content) {
		dataWriter.format(".data\n");
		dataWriter.format("\t%s: .asciiz \"%s\"\n", label, content);
	}
	
	public void addStringLabel(String label1 ,String label2) {
		dataWriter.format(".data\n");
		dataWriter.format("\tglobal_%s: .word %s\n", label1, label2);
	}
	
	public void loadAddress(TEMP dst, String label) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tla Temp_%d, %s\n",idxdst,label);
		
	}
	public void addGlobalInt(String label, int value) {
		dataWriter.format(".data\n");
		dataWriter.format("\tglobal_%s: .word %d\n", label, value);
	}
	
	public void storeWord(TEMP source, TEMP dst, int offset) {
		int idxsoucre=source.getSerialNumber();
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tsw Temp_%d, %d(Temp_%d)\n",idxsoucre,offset, idxdst);
	}
	
	public void malloc(TEMP dst, TEMP size) {
		int idxdst=dst.getSerialNumber();
		int idxsize=size.getSerialNumber();
		fileWriter.format("\tmove $a0, Temp_%d\n", idxsize);
		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove Temp_%d, $v0\n", idxdst);
	}
	
	public void addi(TEMP dst,TEMP oprnd1,int num)
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\taddi Temp_%d, Temp_%d, %d\n",dstidx,i1,num);
	}
	
	
	public void loadLocal(TEMP dst, int offset) {
		int dstidx=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d, %d($fp)\n",dstidx, -4*(offset+10));
	}
	
	public void loadWord(TEMP dst,TEMP source, int offset) {
		int idxdst=dst.getSerialNumber();
		int idxsoucre=source.getSerialNumber();
		fileWriter.format("\tlw Temp_%d, %d(Temp_%d)\n",idxdst,offset,idxsoucre );
	}
	
	public void print_string(String datalabel) {
		fileWriter.format("\tla $a0, %s\n", datalabel);
		fileWriter.format("\tli $v0, 4\n");
		fileWriter.format("\tsyscall\n");
		
	}
	
	public void print_string(TEMP t) {
		int idxt=t.getSerialNumber();
		fileWriter.format("\tmove $a0, Temp_%d\n", idxt);
		fileWriter.format("\tli $v0, 4\n");
		fileWriter.format("\tsyscall\n");
		
	}
	
	public void exit() {
		fileWriter.format("\tli $v0, 10\n");
		fileWriter.format("\tsyscall\n");
	}
	
	
	
	public void jal(String funcLabel) {
		fileWriter.format("\tjal %s\n", funcLabel);
	}
	
	public void popStack(int numOfItems) {
		fileWriter.format("\taddi $sp, $sp, %d\n", 4*numOfItems);
	}
	
	
	public void pushStack(TEMP source) {
		int idxsoucre=source.getSerialNumber();
		fileWriter.format("\taddi $sp, $sp, -4\n");
		fileWriter.format("\tsw Temp_%d, 0($sp)\n", idxsoucre);
	}
	
	public void loadReturnedValue(TEMP dst) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tmove Temp_%d, $v0\n", idxdst);
	}
	
	
	public void loadParam(TEMP dst, int offset) {
		int dstidx=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d, %d($fp)\n",dstidx, 4*(1+offset));
	}
	
	
	public void space(String label, int size) {
		dataWriter.format("\t%s: .space %d\n",label,size);
	}
	
	public void concat_string_loop(String startLoopLabel, String endLoopLabel) {
		fileWriter.format("\tlb $s2, 0($s1)\n");
		fileWriter.format("\tbeq $s2, $zero ,%s\n", endLoopLabel);
		fileWriter.format("\tsb $s2, 0($s0)\n");
		fileWriter.format("\taddiu $s0, $s0, 1\n");
		fileWriter.format("\taddiu $s1, $s1, 1\n");
		fileWriter.format("\tj %s\n", startLoopLabel);
		

	}
	
	public void move_to_Sregister(int SregisterIndex, TEMP t) {
		int idxsoucre=t.getSerialNumber();
		fileWriter.format("\tmove $s%d, Temp_%d\n", SregisterIndex,idxsoucre);
		
	}
	
	public void string_eq_loop(String startLoopLabel, String endLoopLabel, String assignZeroLabel) {
		fileWriter.format("\tlb $s2, 0($s0)\n");
		fileWriter.format("\tlb $s3, 0($s1)\n");
		fileWriter.format("\tbne $s2, $s3, %s\n",assignZeroLabel);
		fileWriter.format("\tbeq $s2, $zero, %s\n", endLoopLabel);
		fileWriter.format("\taddiu $s0, $s0, 1\n");
		fileWriter.format("\taddiu $s1, $s1, 1\n");
		fileWriter.format("\tj %s\n", startLoopLabel);


	}
	
	public void lb(TEMP dst, TEMP source) {
		
		int idxdst=dst.getSerialNumber();
		int idxsoucre=source.getSerialNumber();

		fileWriter.format("\tlb Temp_%d, 0(Temp_%d)\n", idxdst, idxsoucre);
	}
	
	public void sb(TEMP source, TEMP dst) {
		
		int idxsoucre=source.getSerialNumber();
		int idxdst=dst.getSerialNumber();

		fileWriter.format("\tsb Temp_%d, 0(Temp_%d)\n", idxsoucre,idxdst);
	}
	
	public void addiu(TEMP dst, TEMP op, int i) {
		int idxdst=dst.getSerialNumber();
		int idxop=op.getSerialNumber();
		fileWriter.format("\taddiu Temp_%d, Temp_%d, %d\n", idxdst, idxop, i);
		


	}
	
	public void move(TEMP dst, TEMP source) {
		int idxdst=dst.getSerialNumber();
		int idxsoucre=source.getSerialNumber();

		fileWriter.format("\tmove Temp_%d, Temp_%d\n", idxdst, idxsoucre);
	}
	
	////DAN PART/////
	/**************************************/
	/* =========== START_addition ==========  */
	/**************************************/
	public void loadimmedate(TEMP address, int value) {
		int dst=address.getSerialNumber();
		fileWriter.format("\tli Temp_%d, %d\n",dst,value);
	}
	public void newArray(TEMP oprnd1,TEMP oprnd2)
	{
		int i1=oprnd1.getSerialNumber(); /* will hold address */
		int i2=oprnd2.getSerialNumber(); /* number of elements */

		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tmove $a0,Temp_%d\n",i2);
		/* add 1 to size because arr[0]=size */
		fileWriter.format("\taddi $a0,$a0,1\n");
		fileWriter.format("\tmul $a0,$a0,4\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove Temp_%d, $v0\n",i1);
		/* save size of array at index 0 */
		storeWord(oprnd2, oprnd1, 0);
	}

	
	
	public void arrayAccess(TEMP dst, TEMP oprnd1,TEMP oprnd2)
	{
		int address=oprnd1.getSerialNumber(); /* address of array */
		int offset=oprnd2.getSerialNumber(); /* offset */
		int dstidx=dst.getSerialNumber(); /* will hold the item from array */

		/* check if offset is less then 0 */
		fileWriter.format("\tblt Temp_%d,$zero,%s\n",offset, "exit_out_of_bound");
		/* load size of array into $s0 */
		fileWriter.format("\tlw $s0,%d(Temp_%d)\n", 0, address);

		/* check if offset is equal or greater then size of array */
		fileWriter.format("\tbge Temp_%d,$s0,%s\n", offset, "exit_out_of_bound");
		fileWriter.format("\tmove $s0,Temp_%d\n",offset);

		/* add 1 to offset because arr[0]=size */
		fileWriter.format("\taddi $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");

		/* addu = unsighn add, thats what they used */
		fileWriter.format("\taddu $s0,Temp_%d,$s0\n", address);
		fileWriter.format("\tlw Temp_%d,%d($s0)\n", dstidx, 0);
		
	}
	
	


	
	
	public void arraySet(TEMP val, TEMP oprnd1,TEMP oprnd2) //value, address, offset
	{
		int address=oprnd1.getSerialNumber(); /* v of array */
		int offset=oprnd2.getSerialNumber(); /* offset */
		int value= val.getSerialNumber(); /* value to write into array */

		/* check if offset is less then 0 */
		fileWriter.format("\tblt Temp_%d,$zero,%s\n",offset,"exit_out_of_bound");
		/* load size of array into $s0 */
		fileWriter.format("\tlw $s0,%d(Temp_%d)\n", 0, address);

		/* check if offset is equal or greater then size of array */
		fileWriter.format("\tbge Temp_%d,$s0,%s\n", offset, "exit_out_of_bound");
		fileWriter.format("\tmove $s0,Temp_%d\n",offset);

		/* add 1 to offset because arr[0]=size */
		fileWriter.format("\taddi $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");

		/* unsighn ? thats what they used */
		fileWriter.format("\taddu $s0,Temp_%d,$s0\n", address);
		/* save value  */
		fileWriter.format("\tsw Temp_%d, %d($s0)\n", value, 0);
		
	}
	
	
	
	////////////
	
	public void newClass(TEMP dst, String className, int sizeInBytes, int vtable) {
		int idxdst=dst.getSerialNumber();
		
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tli $a0,%d\n", sizeInBytes);
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove Temp_%d, $v0\n", idxdst);
		if(vtable==1) {
			fileWriter.format("\tla $s0, %s\n", "vt_"+className);
			fileWriter.format("\tsw $s0, 0(Temp_%d)\n", idxdst);
		}
		

	}
	
	public void initialize_IntField(TEMP dst, int value, int index) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tli $s0,%d\n", value);
		fileWriter.format("\tsw $s0,%d(Temp_%d)\n", 4*index, idxdst);
	}
	
	public void initialize_StringField(TEMP dst, String value, int index, String stringLabel) {
		int idxdst=dst.getSerialNumber();
		dataWriter.format("\t%s: .asciiz \"%s\"\n", stringLabel, value);
		fileWriter.format("\tla $s0,%s\n", stringLabel);
		fileWriter.format("\tsw $s0,%d(Temp_%d)\n", 4*index, idxdst);
		
	}
	
	public void initialize_NILField(TEMP dst, int index) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tli $s0,0\n");
		fileWriter.format("\tsw $s0,%d(Temp_%d)\n", 4*index, idxdst);
	}
	
	public void storeParam(TEMP source, int offset) {
		int srcidx=source.getSerialNumber();
		fileWriter.format("\tsw Temp_%d, %d($fp)\n",srcidx, 4*(1+offset));
	}
	
	
	
	public void callVirtualMethod(TEMP address, int methodOffset) {
		int i=address.getSerialNumber();
		fileWriter.format("\tlw $s0, 0(Temp_%d)\n", i); //load vt to s0
		fileWriter.format("\tlw $s1, %d($s0)\n", 4*(methodOffset));
		fileWriter.format("\tjalr $s1\n");

	}
	
	public void loadClassFieldInFunc(TEMP dst, int offset) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw $s0 8($fp)\n");
		fileWriter.format("\tlw Temp_%d, %d($s0)\n",idxdst, 4*offset);
	}
	
	public void storeClassFieldInFunc(TEMP source, int offset) {
		int idxsrc=source.getSerialNumber();
		fileWriter.format("\tlw $s0 8($fp)\n"); //load the object
		fileWriter.format("\tsw Temp_%d, %d($s0)\n",idxsrc, 4*offset);
	}
	
//	public void print_char(TEMP t) {
//		int idxt=t.getSerialNumber();
//		fileWriter.format("\tmove $a0, Temp_%d\n", idxt);
//		fileWriter.format("\tli $v0, 11\n");
//		fileWriter.format("\tsyscall\n");
//	}
	
	
	/////////////////////////////////////////////////////////////////////////
	
	
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS_TEXT.txt");
				String dataname=String.format("MIPS_DATA.txt");
				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
				instance.dataWriter = new PrintWriter(dirname+dataname);
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.dataWriter.print(".data\n");
			instance.dataWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.dataWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.dataWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			
			instance.fileWriter.print(".text\n");
			
			
		}
		return instance;
	}
}
