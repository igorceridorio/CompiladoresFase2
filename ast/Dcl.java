package ast;

import java.util.ArrayList;

public class Dcl {

	public Dcl(ArrayList<Variable> idlist, Type type) {
		this.idlist = idlist;
		this.type = type;
	}

	public Dcl(ArrayList<Variable> idlist, ArrayType arrayType) {
		this.idlist = idlist;
		this.arrayType = arrayType;
	}

	public void genC(PW pw) {
		boolean isFirst = true;
		boolean isString = false;

		if (arrayType == null) {

			// if arrayType is null then we are dealing with a stdtype

			// prints the type of the variables
			if (type == Type.integerType)
				pw.print("int ");
			else if (type == Type.realType)
				pw.print("float ");
			else if (type == Type.charType)
				pw.print("char ");
			else {
				pw.print("char ");
				isString = true;
			}

			// prints the list of variables
			for (Variable v : idlist) {
				if (isFirst) {
					if (isString)
						pw.out.print(v.getName() + "[50]");
					else
						pw.out.print(v.getName());
					isFirst = false;
				} else {
					if (isString)
						pw.out.print(", " + v.getName() + "[50]");
					else
						pw.out.print(", " + v.getName());
				}
			}

		} else {

			// if we are dealing with an arrayType
			String range = Integer.toString(arrayType.getIntnum2() - arrayType.getIntnum());

			// prints the type of the variables
			if (arrayType.getType() == Type.integerType)
				pw.print("int ");
			else if (arrayType.getType() == Type.realType)
				pw.print("float ");
			else if (arrayType.getType() == Type.charType)
				pw.print("char ");
			else {
				pw.print("char ");
				isString = true;
			}
			
			// prints the list of variables
			for(Variable v : idlist){
				if (isFirst) {
					if(isString)
						pw.out.print(v.getName() + "[" + range + "]" + "[50]");
					else
						pw.out.print(v.getName() + "[" + range + "]");
					isFirst = false;
				} else {
					if(isString)
						pw.out.print(", " + v.getName() + "[" + range + "]" + "[50]");
					else
						pw.out.print(", " + v.getName() + "[" + range + "]");
				}
			}

		}
		
		pw.out.println(";");

	}

	private ArrayList<Variable> idlist;
	private Type type;

	private ArrayType arrayType;

}
