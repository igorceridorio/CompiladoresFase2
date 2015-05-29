package ast;

public class StringExpr extends Expr {
    
    public StringExpr( String value ) {
        this.value = value; 
    }
    
    public void genC( PW pw ) {
        pw.out.print("\"" + value + "\"");
    }
    
    public String getValue() {
        return value;
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String value;
}