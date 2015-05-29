package ast;

public class NumberFloatExpr extends Expr {

	public NumberFloatExpr(float value){
		this.value = value;
	}
	
	public float getValue(){
		return value;
	}
	
	public void genC(PW pw){
		pw.out.print(String.valueOf(value));
	}
	
	public Type getType(){
		return Type.realType;
	}
	
	private float value;
	
}