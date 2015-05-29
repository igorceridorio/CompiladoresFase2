package ast;

public class IntegerType extends Type{
	
	public IntegerType(){
		super("INTEGER");
	}
	
	public String getCName(){
		return "int";
	}
	
}