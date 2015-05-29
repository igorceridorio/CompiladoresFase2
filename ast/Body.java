package ast;

public class Body extends Prog{
	
	public Body(Dclpart dclpart, CompStmt compstmt){
		this.dclpart = dclpart;
		this.compstmt = compstmt;
	}
	
	public Body(CompStmt compstmt){
		this.compstmt = compstmt;
	}
	
	public void genC(PW pw){
		if(dclpart != null)
			dclpart.genC(pw);
		compstmt.genC(pw);
	}
	
	private Dclpart dclpart;
	private CompStmt compstmt;
	
}