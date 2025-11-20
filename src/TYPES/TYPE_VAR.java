package TYPES;

public class TYPE_VAR extends TYPE{
	public TYPE t;
	
	
	public TYPE_VAR(String name, TYPE t) {
		this.name=name;
		this.t=t;
	
	
	}

	public boolean isVar(){ return true;}

}
