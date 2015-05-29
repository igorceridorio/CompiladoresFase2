package ast;

import java.io.PrintWriter;

import lexer.*;

public class CompilerError {
	
	public CompilerError(PrintWriter out){
		this.out = out;
	}
	
	public void setLexer(Lexer lexer){
		this.lexer = lexer;
	}
	
	public void signal(String strMessage){
		System.out.println("Error at line " + lexer.getLineNumber() + ": ");
		System.out.println(lexer.getCurrentLine());
		System.out.println("Compilation error: " + strMessage);
		throw new RuntimeException();
	}
	
	private Lexer lexer;
	PrintWriter out;
	
}