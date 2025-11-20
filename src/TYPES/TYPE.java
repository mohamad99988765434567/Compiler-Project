package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	
	public boolean isVar(){ return false;}


	public static boolean check_unequal_cases(TYPE t1, TYPE t2) {
		if (case1(t1, t2) || case2(t1, t2) || case3(t1, t2)){
			return true;
		}
		return false;
	}
	private static boolean case1(TYPE t1, TYPE t2) {
		if(t1 instanceof TYPE_ARR && t2 instanceof TYPE_NIL){
			return true;
		}
		return false;
	}
	private static boolean case2(TYPE t1, TYPE t2) {
		if(t1 instanceof TYPE_CLASS && t2 instanceof TYPE_NIL){
			return true;
		}
		return false;
	}
	private static boolean case3(TYPE t1, TYPE t2) {
		if(t1 instanceof TYPE_CLASS && t2 instanceof TYPE_CLASS){
			TYPE_CLASS c1 = (TYPE_CLASS)t1;
			TYPE_CLASS c2 = (TYPE_CLASS)t2;
			while(c2 != null){
				if (c2.name.equals(c1.name)) return true;
				c2 = c2.father;
			}
		}
		return false;

	}

}
