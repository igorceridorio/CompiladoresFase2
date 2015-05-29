package ast;

public class WhileStmt extends Stmt {

	public WhileStmt(Expr expr, StmtList whilePart) {
		this.expr = expr;
		this.whilePart = whilePart;
	}

	public void genC(PW pw) {
		pw.print("while (");
		expr.genC(pw);
		pw.out.println(") {");
		pw.add();
		whilePart.genC(pw);
		pw.sub();
		pw.println("}");
	}

	private Expr expr;
	private StmtList whilePart;

}