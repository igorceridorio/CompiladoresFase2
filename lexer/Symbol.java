package lexer;

public enum Symbol {
	
	EOF("EOF"),
	IDPID("idpid"),
	FLOATNUM("floatnum"),
	INTNUM("intnum"),
	PROGRAM("PROGRAM"),
	VAR("VAR"),
	INTEGER("INTEGER"),
	REAL("REAL"),
	CHAR("CHAR"),
	STRING("STRING"),
	BOOLEAN("BOOLEAN"),
	ARRAY("ARRAY"),
	OF("OF"),
	BEGIN("BEGIN"),
	END("END"),
	IF("IF"),
	THEN("THEN"),
	ELSE("ELSE"),
	ENDIF("ENDIF"),
	WHILE("WHILE"),
	DO("DO"),
	ENDWHILE("ENDWHILE"),
	READ("READ"),
	WRITE("WRITE"),
	WRITELN("WRITELN"),
	PHRASE("PHRASE"),
	AND("AND"),
	OR("OR"),
	MOD("MOD"),
	DIV("DIV"),
	NOT("NOT"),
	LEFTKEY("{"),
	RIGHT("}"),
	LEFTBRA("["),
	RIGHTBRA("]"),
	LEFTPAR("("),
	RIGHTPAR(")"),
	ASSIGN("="),
	LT("<"),
	GT(">"),
	LE("<="),
	GE(">="),
	DIFF("<>"),
	PLUS("+"),
	MINUS("-"),
	MULTIPLICATION("*"),
	DIVISION("/"),
	EQUAL(":="),
	COMMA(","),
	SEMICOLON(";"),
	COLON(":"),
	POINT("."),
	APOSTROPHE("'");
	
	Symbol(String name){
		this.name = name;
	}
    
	public String toString(){
		return name;
	}
	
	private String name;
	
}
