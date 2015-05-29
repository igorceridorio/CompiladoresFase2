package ast;

public class CharType extends Type{
	
	public CharType(){
		super("CHAR");
	}
	
	public String getCName(){
		return "char";
	}
	
}