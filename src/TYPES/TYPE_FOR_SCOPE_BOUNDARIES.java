package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	/****************/
	/* CTROR(S) ... */
	/****************/
	
	public TYPE type;
	public int scopeNum;
	
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, TYPE type, int scopeNum)
	{
		this.name = name;
		this.type = type;
		this.scopeNum=scopeNum;
	}
}
