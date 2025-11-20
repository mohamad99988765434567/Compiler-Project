package AST;

import TYPES.*;
import IR.*;
import TEMP.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


import SYMBOL_TABLE.*;


public abstract class AST_CLASS_DEC extends AST_Node{
	String className;
	String fatherName;
	AST_CFIELD_LIST cFieldList;
	
	TYPE_CLASS classData;
	
	public TYPE SemantMe() throws SemantErrorException
	{	
		
		TYPE t=null;
		TYPE currClassType= null;
		TYPE fatherType = null;
		TYPE_CLASS fatherClassType=null;
		
		String errMessage;
		
		if (this.fatherName != null) {
			fatherType=SYMBOL_TABLE.getInstance().find(this.fatherName);
			if (fatherType==null) {
				errMessage=String.format("father class %s does not exist\n",this.fatherName);
				throw new SemantErrorException(errMessage,lineNum);
				
			}
			if (!(fatherType instanceof TYPE_CLASS)) {
				errMessage=String.format("father class %s does not exist\n",this.fatherName);
				throw new SemantErrorException(errMessage,lineNum);
			}
			
			fatherClassType=(TYPE_CLASS)fatherType;
			
		}
		
		
		currClassType = SYMBOL_TABLE.getInstance().find(this.className);
		if (currClassType!=null) {
			errMessage=String.format("%s name is already taken\n",this.className);
			throw new SemantErrorException(errMessage,lineNum);
		}
		
		LinkedHashMap<String, TYPE> dataMembers = new LinkedHashMap<>();
		TYPE_CLASS typeClass = new TYPE_CLASS(fatherClassType,this.className, dataMembers);
		
		
		SYMBOL_TABLE.getInstance().enter(className,typeClass);
		
		
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope("class", typeClass); //////Do we need a sign here for our class to avoid problems?!???????

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		
		/***************************/
		for (AST_CFIELD_LIST l = cFieldList; l  != null; l = l.tail)
		{
			t=l.head.SemantMe(typeClass); //here, in SemantMe, there will be some validations in the scope
			System.out.format("class %s, field %s\n", this.className, t.name);
			//System.out.format("%s\n", t);
			typeClass.data_members.put(t.name, t);
		}
					
		
		
		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		
		//We have already done it 

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		
		
		
		
		//we want to save a linked hashmap of fields of the class (and inherited fields)
		//and we want to save a linked hashMap with all
//		LinkedHashMap<String, TYPE> allFieldsReverse= new LinkedHashMap<>();
//		LinkedHashMap<String, String> vtableMethodsReverse = new LinkedHashMap<>();
//		for(TYPE_CLASS c=typeClass; c!=null; c=c.father) {
//			LinkedHashMap<String, TYPE> map=c.data_members;
//			List<String> keys= new ArrayList<String>(map.keySet());
//			Collections.reverse(keys);
//			for (String key: keys) {
//				TYPE type= map.get(key);
//				if (type instanceof TYPE_FUNCTION) {
//					TYPE_FUNCTION x=(TYPE_FUNCTION)type;
//					if (vtableMethodsReverse.get(x.name)==null) { //if function  wasn't overloaded by "younger" function 
//						vtableMethodsReverse.put(x.name, c.name);
//					}
//				}
//				else { //type instanceof TYPE_CLASS_VAR_DEC
//					if (allFieldsReverse.get(key)==null) {
//						allFieldsReverse.put(key, map.get(key));
//						
//					}
//				}
//			}
//		}
//		LinkedHashMap<String, TYPE> allFields= new LinkedHashMap<>();
//		LinkedHashMap<String, String> vtableMethods = new LinkedHashMap<>();
//		
//		List<String> keys= new ArrayList<String>(allFieldsReverse.keySet());
//		Collections.reverse(keys);
//		for (String key: keys) {
//			allFields.put(key, allFieldsReverse.get(key));
//		}
//		
//		List<String> keys2= new ArrayList<String>(vtableMethodsReverse.keySet());
//		Collections.reverse(keys2);
//		for (String key: keys2) {
//			vtableMethods.put(key, vtableMethodsReverse.get(key));
//		}
//		
//		typeClass.Allfields=allFields;
//		typeClass.Allmethods=vtableMethods;
		
//		List<String> fieldIndex=new ArrayList<String>(typeClass.Allfields.keySet());
//		List<String> methodIndex= new  ArrayList<String>(typeClass.Allmethods.keySet());
//		System.out.println("ALL METHODS " + this.className);
//		for (String s: methodIndex) {
//			System.out.println(s);
//		}
//		typeClass.fieldIndex=fieldIndex;
//		typeClass.methodIndex=methodIndex;
		
		
		//create/update index
		if (this.fatherName==null) {
			List<String> methodIndex = new ArrayList<String>();
			List<String> fieldIndex = new ArrayList<String>();
			LinkedHashMap<String, TYPE> map = typeClass.data_members;
			List<String> keys_list = new ArrayList<String>(map.keySet());
			for (String key: keys_list) {
				TYPE type = map.get(key);
				if (type instanceof TYPE_FUNCTION) {
					methodIndex.add(key);
				}
				else {
					fieldIndex.add(key);
				}
			}
			
			typeClass.methodIndex = methodIndex;
			typeClass.fieldIndex = fieldIndex;
			
		}
		
		else {
			TYPE_CLASS c = typeClass.father;
			List<String> methodIndex =c.methodIndex;
			List<String> fieldIndex =c.fieldIndex;
			
			LinkedHashMap<String, TYPE> map = typeClass.data_members;
			
			List<String> keys_list= new ArrayList<String>(map.keySet());
			for (String key: keys_list) {
				TYPE type = map.get(key);
				if (type instanceof TYPE_FUNCTION) {
					TYPE_FUNCTION x = (TYPE_FUNCTION)type;
					if (!methodIndex.contains(x.name)) { //if function  wasn't overloaded by "younger" function 
						methodIndex.add(x.name);
					}
				}
				
				else {
					if (!fieldIndex.contains(key)) {
						fieldIndex.add(key);
					}
				}
			
			}
			typeClass.methodIndex = methodIndex;
			typeClass.fieldIndex = fieldIndex;
		
		}
		
		
		
		LinkedHashMap<String, TYPE> allFields = new LinkedHashMap<>();
		LinkedHashMap<String, String> allMethods = new LinkedHashMap<>();
		
		List<String> methodIndex = typeClass.methodIndex;
		String method;
		for (int i = 0; i < methodIndex.size(); i++) {
			method = methodIndex.get(i);
			for(TYPE_CLASS c = typeClass; c != null; c = c.father) {
				LinkedHashMap<String, TYPE> map = c.data_members;
				if (map.containsKey(method) && map.get(method) instanceof TYPE_FUNCTION) {
					allMethods.put(method, c.name);
					break;
					
				}
			}
			
		}
		
		List<String> fieldIndex = typeClass.fieldIndex;
		String field;
		for (int i=0; i<fieldIndex.size(); i++) {
			field=fieldIndex.get(i);
			for(TYPE_CLASS c = typeClass; c != null; c = c.father) {
				LinkedHashMap<String, TYPE> map=c.data_members;
				if (map.containsKey(field) && map.get(field) instanceof TYPE_CLASS_VAR_DEC) {
					allFields.put(field, map.get(field));
					break;
					
				}
			}
			
		}
		typeClass.Allfields=allFields;
		typeClass.Allmethods=allMethods;

		
		//before end, save class data in the ast:
		this.classData=typeClass;
		return null;		
	}
	
	
	
	
	
	
	
	
	public TEMP IRme() {
		
		
//		//key is the function name, value is the "youngest" class which implements it ("overload").
//		LinkedHashMap<String, String> vtable_methods = new LinkedHashMap<>();
//		
//		//we will scan from the youngest class the the oldest class to find the methods that we will put their labels in the vtable;
//		for(TYPE_CLASS c=this.classData; c!=null; c=c.father) {
//			LinkedHashMap<String, TYPE> map=c.data_members;
//			//iterate over data_members keys
//			Set<String> keys=map.keySet();
//			for (String key: keys) {
//				TYPE t= map.get(key);
//				if (t instanceof TYPE_FUNCTION) {
//					TYPE_FUNCTION x=(TYPE_FUNCTION)t;
//					if (vtable_methods.get(x.name)==null) { //if function  wasn't overloaded by "younger" function 
//						vtable_methods.put(x.name, c.name);
//					}
//				}
//				
//			}
//		}
		
		
		
		
		if (this.classData.Allmethods!=null && this.classData.Allmethods.size()>0) {
			String vtable="vt_"+this.className;
			IR.getInstance().Add_IRcommand(new IRcommand_data_label(vtable));
			LinkedHashMap<String,String> map=this.classData.Allmethods;
			for(String key: map.keySet()) {
				String methodName=map.get(key)+"_"+key; //className_methodName
				IR.getInstance().
				Add_IRcommand(new IRcommand_add_vtable_method(methodName));
			}
		}
		
		
		
		for (AST_CFIELD_LIST l = cFieldList; l  != null; l = l.tail)
		{
			l.head.IRme(this.classData);
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
}
