package ast;

import java.util.ArrayList;

public class ReadStmt extends Stmt {

	public ReadStmt(ArrayList<VblExpr> vblist) {
		this.vblist = vblist;
	}

	public void genC(PW pw) {
		pw.print("scanf(\"");

		// prints the C arguments according to each vbl type of the vblist
		int i = 0;
		for (i = 0; i < (vblist.size() - 1); i++) {
			if (vblist.get(i).getType() == Type.integerType)
				pw.out.print("%d ");
			else if (vblist.get(i).getType() == Type.realType)
				pw.out.print("%f ");
			else if (vblist.get(i).getType() == Type.charType)
				pw.out.print("%c ");
			else
				pw.out.print("%s ");
		}

		// prints the last C argument, which doesn't need the last space
		if (vblist.get(i).getType() == Type.integerType)
			pw.out.print("%d");
		else if (vblist.get(i).getType() == Type.realType)
			pw.out.print("%f");
		else if (vblist.get(i).getType() == Type.charType)
			pw.out.print("%c");
		else
			pw.out.print("%s");
		pw.out.print("\",");

		// prints the names of the variables in the order of the vblist
		for (i = 0; i < (vblist.size() - 1); i++) {
			if (vblist.get(i).getType() == Type.stringType)
				pw.out.print(" " + vblist.get(i).getName() + ",");
			else
				pw.out.print(" &" + vblist.get(i).getName() + ",");
		}

		// prints the name of the last variable present in vblist
		if (vblist.get(i).getType() == Type.stringType)
			pw.out.print(" " + vblist.get(i).getName());
		else
			pw.out.print(" &" + vblist.get(i).getName());
		pw.out.println(");");

	}

	private ArrayList<VblExpr> vblist;

}
