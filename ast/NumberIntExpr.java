package ast;

public class NumberIntExpr extends Expr {

	public NumberIntExpr(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public void genC(PW pw){
		pw.out.print(String.valueOf(value));
	}
	
	public Type getType(){
		return Type.integerType;
	}
	
	private int value;
	
}
