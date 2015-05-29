package ast;

import lexer.Symbol;

public class UnaryExpr extends Expr {

	public UnaryExpr(Expr expr, Symbol op) {
		this.expr = expr;
		this.op = op;
	}

	public void genC(PW pw) {
		switch (op) {
		case PLUS:
			pw.out.print("+");
			break;
		case MINUS:
			pw.out.print("-");
			break;
		case NOT:
			pw.out.print("!");
			break;
		default:
			break;
		}
		expr.genC(pw);
	}

	public Type getType() {
		return expr.getType();
	}

	private Expr expr;
	private Symbol op;
}
