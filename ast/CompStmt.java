package ast;

import java.util.ArrayList;

public class CompStmt extends Stmt {

	public CompStmt(ArrayList<Stmt> stmts) {
		this.stmts = stmts;
	}

	public void genC(PW pw) {
		for (Stmt s : stmts)
			s.genC(pw);
	}

	private ArrayList<Stmt> stmts;

}
