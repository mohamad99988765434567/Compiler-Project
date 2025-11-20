package TYPES;

public class TYPE_ARR extends TYPE
{
	public TYPE typeArray;
	public TYPE_ARR(String name,  TYPE myType)
	{
		this.name = name;
		this.typeArray = myType;
	}
	public boolean isArray(){
		return true;
	}
}
