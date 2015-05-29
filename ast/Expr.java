package ast;

abstract public class Expr {

	abstract public void genC(PW pw);

	abstract public Type getType();

}