package ast;

public class RealType extends Type {

	public RealType() {
		super("REAL");
	}

	public String getCName() {
		return "float";
	}

}