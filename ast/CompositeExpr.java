package ast;

import lexer.Symbol;

public class CompositeExpr extends Expr {

	public CompositeExpr(Expr left, Symbol oper, Expr right) {
		this.left = left;
		this.oper = oper;
		this.right = right;
	}

	public void genC(PW pw) {
		
		String op = oper.toString();
		switch (op) {
		case "<>":
			op = "!="; break;
		case "OR":
			op = "||"; break;
		case "AND":
			op = "&&"; break;
		case "MOD":
			op = "%"; break;
		case "DIV":
			op = "/"; break;
		case "NOT":
			op = "!"; break;
		}
		
		left.genC(pw);		
		pw.out.print(" " + oper.toString() + " ");
		right.genC(pw);
	}

	public Type getType() {
		if (oper == Symbol.DIFF || oper == Symbol.LE || oper == Symbol.LT
				|| oper == Symbol.GE || oper == Symbol.GT || oper == Symbol.AND
				|| oper == Symbol.OR)
			return Type.booleanType;
		else
			return Type.integerType;
	}

	private Expr left, right;
	private Symbol oper;

}