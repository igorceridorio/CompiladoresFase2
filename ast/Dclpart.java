package ast;

import java.util.ArrayList;

public class Dclpart{

	public Dclpart(ArrayList<Dcl> dcls){
		this.dcls = dcls;
	}
	
	public void genC(PW pw){
		for(Dcl d: dcls)
			d.genC(pw);
	}
	
	private ArrayList<Dcl> dcls;
	
}
