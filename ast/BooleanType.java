package ast;

public class BooleanType extends Type{
	
	public BooleanType(){
		super("BOOLEAN");
	}
	
	public String getCName(){
		return "int";
	}
	
}