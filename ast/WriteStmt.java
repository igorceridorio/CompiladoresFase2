package ast;

import java.util.ArrayList;

public class WriteStmt extends Stmt {

	public WriteStmt(ArrayList<Expr> exprList) {
		this.exprList = exprList;
	}

	public void genC(PW pw) {
		pw.print("printf(\"");

		// prints the C arguments according to each expr type of the exprlist
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
		pw.out.print("\", ");

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
	}

	private ArrayList<Expr> exprList;

}
