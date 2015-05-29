package ast;

import java.util.ArrayList;

public class StmtList {

	public StmtList(ArrayList<Stmt> stmt) {
		this.stmt = stmt;
	}

	public void genC(PW pw) {
		for (Stmt s : stmt)
			s.genC(pw);
	}

	public boolean isEmpty(){
		boolean r = false;
		
		if( this.stmt.isEmpty() )
			r = true;
		
		return r;
	}
	
	private ArrayList<Stmt> stmt;

}