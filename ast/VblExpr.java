package ast;

public class VblExpr extends Expr{

	public VblExpr(Variable id, Expr expr){
		this.id = id;
		this.expr = expr;
	}
	
	public VblExpr(Variable id){
		this.id = id;
	}
	
	public Type getType(){
		return id.getType();
	}
	
	public void setType(Type type){
		this.id.setType(type);
	}
	
	public String getName(){
		return id.getName();
	}
	
	public void setName(String name){
		this.id.setName(name);
	}

	public void genC(PW pw){
			pw.out.print(id.getName());
		if(expr != null){
			pw.out.print("[");
			expr.genC(pw);
			pw.out.print("]");
		}
	}
	
	private Variable id;
	private Expr expr;
	
}