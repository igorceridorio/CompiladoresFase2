package ast;

abstract public class Type {

	public Type() {
	}

	public Type(String name) {
		this.name = name;
	}

	public static Type integerType = new IntegerType();
	public static Type realType = new RealType();
	public static Type charType = new CharType();
	public static Type stringType = new StringType();
	public static Type booleanType = new BooleanType();

	public String getName() {
		return name;
	}

	abstract public String getCName();

	private String name;

}