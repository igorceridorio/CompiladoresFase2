package ast;

public class StringType extends Type{
	
	public StringType(){
		super("STRING");
	}
	
	public String getCName(){
		return "char";
	}
	
}