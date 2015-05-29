package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genC( PW pw ) {
    	pw.out.print( v.getName() );
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;
}