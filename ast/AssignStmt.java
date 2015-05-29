package ast;

public class AssignStmt extends Stmt {

	public AssignStmt(VblExpr vbl, Expr expr) {
		this.vbl = vbl;
		this.expr = expr;
	}

	public void genC(PW pw) {
		pw.print("");
		vbl.genC(pw);
		pw.out.print(" = ");
		expr.genC(pw);
		pw.out.println(";");
	}

	private VblExpr vbl;
	private Expr expr;

}