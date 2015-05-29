package ast;

import java.io.IOException;

public class Program {
	
	public Program(String pid, Body body){
		this.pid = pid;
		this.body = body;
	}
	
	public void genC(PW pw) throws IOException{
		
		pw.out.println("#include <stdio.h>");
		pw.println("");
		pw.println("void main() {");
		
		//adding indentation to the code
		pw.add();
		
		//generating C code for the body part
		body.genC(pw);
		
		pw.sub();
		pw.println("}");
		
	}
	
	String pid;
	Body body;
	
}
