package ast;

public class ArrayType extends Type {

	public ArrayType() {
		super("ARRAY");
	}

	public ArrayType(int intnum, int intnum2, Type stdtype) {
		this.intnum = intnum;
		this.intnum2 = intnum2;
		this.stdtype = stdtype;
	}

	public String getCName() {
		return "array";
	}

	public int getIntnum() {
		return intnum;
	}
	
	public int getIntnum2() {
		return intnum2;
	}
	
	public Type getType() {
		return stdtype;
	}

	private int intnum, intnum2;
	private Type stdtype;

}