package ast;

public class ParenthesisExpr extends Expr {
    
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public void genC( PW pw ) {
    	pw.out.print("(");
        expr.genC(pw);
        pw.out.print(")");
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
}