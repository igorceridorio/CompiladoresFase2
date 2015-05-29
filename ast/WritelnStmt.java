package ast;

import java.util.ArrayList;

public class WritelnStmt extends Stmt {

	public WritelnStmt(ArrayList<Expr> exprList) {
		this.exprList = exprList;
	}

	public WritelnStmt() {

	}

	public void genC(PW pw) {
		
		// if there is any exprList prints its elements
		if (exprList != null) {
			
			pw.print("printf(\"");

			// prints the C arguments according to each expr type of the
			// exprlist
			int i = 0;
			for (i = 0; i < (exprList.size() - 1); i++) {
				if (exprList.get(i).getType() == Type.integerType)
					pw.out.print("%d ");
				else if (exprList.get(i).getType() == Type.realType)
					pw.out.print("%f ");
				else if (exprList.get(i).getType() == Type.charType)
					pw.out.print("%c ");
				else
					pw.out.print("%s ");
			}

			// prints the last C argument, which doesn't need the last space
			if (exprList.get(i).getType() == Type.integerType)
				pw.out.print("%d");
			else if (exprList.get(i).getType() == Type.realType)
				pw.out.print("%f");
			else if (exprList.get(i).getType() == Type.charType)
				pw.out.print("%c");
			else
				pw.out.print("%s");
			pw.out.print("\\n\", ");

			// prints the names of the expressions in the order of the exprList
			for (i = 0; i < (exprList.size() - 1); i++) {
				if (exprList.get(i).getType() == Type.stringType) {
					exprList.get(i).genC(pw);
				} else
					exprList.get(i).genC(pw);
				pw.out.print(", ");
			}

			// prints the name of the last expression present in exprList
			if (exprList.get(i).getType() == Type.stringType) {
				exprList.get(i).genC(pw);
			} else
				exprList.get(i).genC(pw);
			pw.out.println(");");

		} else {
			pw.println("printf(\"\\n\");");
		}
	}

	private ArrayList<Expr> exprList;

}