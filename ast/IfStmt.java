package ast;

public class IfStmt extends Stmt {

	public IfStmt(Expr expr, StmtList thenPart, StmtList elsePart) {
		this.expr = expr;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}

	public void genC(PW pw) {
		pw.print("if (");
		expr.genC(pw);
		pw.out.println(") {");
		pw.add();
		thenPart.genC(pw);
		pw.sub();
		pw.println("} ");
		if (!elsePart.isEmpty()) {
			pw.println("else { ");
			pw.add();
			elsePart.genC(pw);
			pw.sub();
			pw.println("} ");
		}
	}

	private Expr expr;
	private StmtList thenPart, elsePart;

}