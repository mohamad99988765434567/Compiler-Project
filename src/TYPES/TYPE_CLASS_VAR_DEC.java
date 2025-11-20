package TYPES;
import TEMP.*;
import AST.*;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public int offset;
	public AST_EXP initialValue;
	
	public TYPE_CLASS_VAR_DEC(TYPE t,String name, int offset)
	{
		this.t = t;
		this.name = name;
		this.initialValue=null;
	}
}
