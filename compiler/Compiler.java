package compiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import lexer.*;
import ast.*;

public class Compiler {

	public Program compile(char[] input, PrintWriter outError) {
		Program p = null;
		symbolTable = new Hashtable<String, Variable>();
		error = new CompilerError(outError);
		lexer = new Lexer(input, error);
		error.setLexer(lexer);
		lexer.nextToken();

		try {
			p = prog();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return p;
	}

	// prog ::= PROGRAM pid ';' body '.'
	private Program prog() {
		Body body = null;
		String pid = "";

		if (lexer.token == Symbol.PROGRAM) {
			lexer.nextToken();
			pid = pid();
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				body = body();
				if (lexer.token != Symbol.POINT)
					error.signal("Expecting '.'");
				lexer.nextToken();
			} else
				error.signal("Expecting ';'");
		} else
			error.signal("Expecting 'PROGRAM'");

		return new Program(pid, body);
	}

	// body::= [dclpart] compstmt
	private Body body() {
		Dclpart dclpart = null;
		CompStmt compstmt = null;

		if (lexer.token == Symbol.VAR)
			dclpart = dclpart();
		compstmt = compstmt();

		if (dclpart != null)
			return new Body(dclpart, compstmt);

		return new Body(compstmt);
	}

	// dclpart ::= VAR dcls
	private Dclpart dclpart() {
		ArrayList<Dcl> dcls = new ArrayList<Dcl>();

		lexer.nextToken();
		dcls = dcls();

		return new Dclpart(dcls);
	}

	// dcls ::= dcl {dcl}
	private ArrayList<Dcl> dcls() {
		ArrayList<Dcl> dcls = new ArrayList<Dcl>();

		dcls.add(dcl());
		while (lexer.token != Symbol.BEGIN)
			dcls.add(dcl());

		return dcls;
	}

	// dcl ::= idlist ':' type ';'
	private Dcl dcl() {
		ArrayList<Variable> idlist = new ArrayList<Variable>();
		Type type = null;
		ArrayType arraytype = null;

		idlist = idlist();
		if (lexer.token == Symbol.COLON) {

			lexer.nextToken();
			if (lexer.token == Symbol.ARRAY) {
				arraytype = arraytype();

			} else {
				type = stdtype();
			}

			if (lexer.token != Symbol.SEMICOLON)
				error.signal("Expecting ';'");
			lexer.nextToken();

		} else {
			error.signal("Expecting ':'");
		}

		if (arraytype != null) {

			for (Variable v : idlist)
				v.setType(arraytype.getType());

			return new Dcl(idlist, arraytype);
		}

		for (Variable v : idlist)
			v.setType(type);

		return new Dcl(idlist, type);
	}

	// idlist ::= id {',' id}
	private ArrayList<Variable> idlist() {
		ArrayList<Variable> idlist = new ArrayList<Variable>();

		if (lexer.token != Symbol.IDPID)
			error.signal("Expecting identifier1");

		String name = id();
		if (symbolTable.get(name) != null)
			error.signal("Variable " + name + " has already been declared");

		Variable v = new Variable(name);
		symbolTable.put(name, v);
		idlist.add(v);

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();

			if (lexer.token != Symbol.IDPID)
				error.signal("Expecting identifier2");

			name = id();
			if (symbolTable.get(name) != null)
				error.signal("Variable " + name + " has already been declared");

			v = new Variable(name);
			symbolTable.put(name, v);
			idlist.add(v);
		}

		return idlist;
	}

	// stdtype ::= INTEGER | REAL | CHAR | STRING
	private Type stdtype() {
		if (lexer.token == Symbol.INTEGER) {
			lexer.nextToken();
			return Type.integerType;
		} else if (lexer.token == Symbol.REAL) {
			lexer.nextToken();
			return Type.realType;
		} else if (lexer.token == Symbol.CHAR) {
			lexer.nextToken();
			return Type.charType;
		} else if (lexer.token == Symbol.STRING) {
			lexer.nextToken();
			return Type.stringType;
		} else
			error.signal("Unknown variable type");
		return null;
	}

	// arraytype ::= ARRAY '[' intnum '..' intnum ']' OF stdtype
	private ArrayType arraytype() {
		int intnum = 0, intnum2 = 0;
		Type stdtype = null;

		lexer.nextToken();
		if (lexer.token == Symbol.LEFTBRA) {
			lexer.nextToken();
			if (lexer.token == Symbol.INTNUM) {
				intnum = lexer.getNumberValue();
				lexer.nextToken();
				if (lexer.token == Symbol.POINT) {
					lexer.nextToken();
					if (lexer.token == Symbol.POINT) {
						lexer.nextToken();
						if (lexer.token == Symbol.INTNUM) {
							intnum2 = lexer.getNumberValue();
							lexer.nextToken();
							if (lexer.token == Symbol.RIGHTBRA) {
								lexer.nextToken();
								if (lexer.token == Symbol.OF) {
									lexer.nextToken();
									stdtype = stdtype();
								} else
									error.signal("Expecting 'OF'");
							} else
								error.signal("Expecting ']'");
						} else
							error.signal("Expecting final index of array");
					} else
						error.signal("Expecting '.'");
				} else
					error.signal("Expecting '.'");
			} else
				error.signal("Expecting initial index of array");
		} else
			error.signal("Expecting '['");

		return new ArrayType(intnum, intnum2, stdtype);
	}

	// compstmt ::= BEGIN stmts END
	private CompStmt compstmt() {
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();

		if (lexer.token == Symbol.BEGIN) {
			lexer.nextToken();
			stmts = stmts();
			if (lexer.token == Symbol.END)
				lexer.nextToken();
			else
				error.signal("Expecting 'END'");
		} else
			error.signal("Expecting 'BEGIN'");

		return new CompStmt(stmts);
	}

	// stmts ::= stmt {';' stmt} ';'
	private ArrayList<Stmt> stmts() {
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();

		stmts.add(stmt());

		if (lexer.token == Symbol.SEMICOLON) {
			lexer.nextToken();
			while (lexer.token != Symbol.END && lexer.token != Symbol.ENDIF
					&& lexer.token != Symbol.ENDWHILE
					&& lexer.token != Symbol.ELSE) {
				stmts.add(stmt());
				if (lexer.token != Symbol.SEMICOLON){
					error.signal("Expecting ';'");
				}else if (lexer.token == Symbol.SEMICOLON)
					lexer.nextToken();
			}
		} else
			error.signal("Expecting ';4'");

		return stmts;
	}

	// stmt ::= ifstmt | whilestmt | assignstmt | compstmt | readstmt |
	// writestmt | writelnstmt
	private Stmt stmt() {
		switch (lexer.token) {
		case IF:
			return ifstmt();
		case WHILE:
			return whilestmt();
		case IDPID:
			return assignstmt();
		case BEGIN:
			return compstmt();
		case READ:
			return readstmt();
		case WRITE:
			return writestmt();
		case WRITELN:
			return writelnstmt();
		default:
			error.signal("Statement expected");
		}
		return null;
	}

	// ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
	private IfStmt ifstmt() {
		Expr expr;
		ArrayList<Stmt> thenPart = new ArrayList<Stmt>();
		ArrayList<Stmt> elsePart = new ArrayList<Stmt>();
		StmtList thenPartList, elsePartList;

		lexer.nextToken();
		expr = expr();

		// verifies if the expression is of boolean type
		if (expr.getType() != Type.booleanType)
			error.signal("Boolean type expected in if expression");

		// iterates the token to the THEN part
		if (lexer.token != Symbol.THEN)
			error.signal("Expecting 'THEN'");
		lexer.nextToken();

		thenPart = stmts();

		// iterates the token to the ELSE part (if exists)
		if (lexer.token == Symbol.ELSE) {
			lexer.nextToken();
			elsePart = stmts();
		}

		// iterates to the ENDIF part
		if (lexer.token != Symbol.ENDIF)
			error.signal("Expecting 'ENDIF'");
		lexer.nextToken();

		thenPartList = new StmtList(thenPart);
		elsePartList = new StmtList(elsePart);

		return new IfStmt(expr, thenPartList, elsePartList);
	}

	// whilestmt ::= WHILE expr DO stmts ENDWHILE
	private WhileStmt whilestmt() {
		Expr expr;
		ArrayList<Stmt> whilePart = new ArrayList<Stmt>();
		StmtList whilePartList;

		lexer.nextToken();
		expr = expr();

		// verifies if the expression is of boolean type
		if (expr.getType() != Type.booleanType)
			error.signal("Boolean type expected in if expression");

		// iterates the token to the DO part
		if (lexer.token != Symbol.DO)
			error.signal("Expecting 'DO'");
		lexer.nextToken();
		whilePart = stmts();

		// iterates to the ENDWHILE part
		if (lexer.token != Symbol.ENDWHILE)
			error.signal("Expecting 'ENDWHILE'");
		lexer.nextToken();

		whilePartList = new StmtList(whilePart);

		return new WhileStmt(expr, whilePartList);
	}

	// assignstmt ::= vbl ':=' expr
	private AssignStmt assignstmt() {
		VblExpr vbl;
		Expr expr;

		vbl = vbl();

		// iterates to the EQUAL part
		if (lexer.token != Symbol.EQUAL)
			error.signal("Expecting ':='");
		lexer.nextToken();

		expr = expr();

		// verifies if the types match
		if (vbl.getType() == Type.charType && expr.getType() != Type.charType)
			error.signal("Type error in assignment");
		else if (vbl.getType() == Type.stringType && ( expr.getType() != Type.charType && expr.getType() != Type.stringType ))
			error.signal("Type error in assignment");
		if ( (vbl.getType() != Type.charType && vbl.getType() != Type.stringType) && vbl.getType() != expr.getType())
			error.signal("Type error in assignment");

		return new AssignStmt(vbl, expr);
	}

	// readstmt ::= READ '(' vblist ')'
	private ReadStmt readstmt() {
		ArrayList<VblExpr> vblist = new ArrayList<VblExpr>();

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			error.signal("Expecting '('");
		lexer.nextToken();
		vblist = vblist();

		if (lexer.token != Symbol.RIGHTPAR)
			error.signal("Expecting ')'");
		lexer.nextToken();

		return new ReadStmt(vblist);
	}

	// writestmt ::= WRITE '(' exprlist ')'
	private WriteStmt writestmt() {
		ArrayList<Expr> exprlist = new ArrayList<Expr>();

		lexer.nextToken();

		if (lexer.token != Symbol.LEFTPAR)
			error.signal("Expecting '('");

		lexer.nextToken();
		exprlist = exprlist();

		if (lexer.token != Symbol.RIGHTPAR)
			error.signal("Expecting ')'");
		lexer.nextToken();

		return new WriteStmt(exprlist);
	}

	// writelnstmt ::= WRITELN '(' [exprlist] ')'
	private WritelnStmt writelnstmt() {
		ArrayList<Expr> exprlist = new ArrayList<Expr>();

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			error.signal("Expecting '('");

		lexer.nextToken();
		if (lexer.token != Symbol.RIGHTPAR){
			exprlist = exprlist();
			lexer.nextToken();
			return new WritelnStmt(exprlist);
		}

		if (lexer.token != Symbol.RIGHTPAR)
			error.signal("Expecting ')'");
		
		lexer.nextToken();

		return new WritelnStmt();
	}

	// vblist ::= vbl {',' vbl}
	private ArrayList<VblExpr> vblist() {
		ArrayList<VblExpr> vblist = new ArrayList<VblExpr>();

		vblist.add(vbl());

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			vblist.add(vbl());
		}

		return vblist;
	}

	// vbl ::= id ['[' expr ']']
	private VblExpr vbl() {
		Variable id = null;
		Expr expr = null;

		// verifies if the variable has been declared
		String name = lexer.getStringValue();
		id = (Variable) symbolTable.get(name);
		if (id == null)
			error.signal("Variable " + name + " was not declared");
		lexer.nextToken();

		// verifies if the vbl has a expr part
		if (lexer.token == Symbol.LEFTBRA) {
			lexer.nextToken();
			expr = expr();

			// semantic analysis of the type of the expression
			if (expr.getType() != Type.integerType)
				error.signal("Expecting an integer type expression");

			if (lexer.token != Symbol.RIGHTBRA)
				error.signal("Expecting ']'");
			lexer.nextToken();
			return new VblExpr(id, expr);
		}

		return new VblExpr(id);
	}

	// exprlist ::= expr {',' expr}
	private ArrayList<Expr> exprlist() {
		ArrayList<Expr> exprlist = new ArrayList<Expr>();

		exprlist.add(expr());

		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			exprlist.add(expr());
		}

		return exprlist;
	}

	// expr ::= simexp [relop expr]
	private Expr expr() {
		Expr left = null, right = null;
		Symbol relop = null;

		left = simexp();
		relop = relop();
		if (relop != null) {
			right = expr();

			left = new CompositeExpr(left, relop, right);
		}

		return left;
	}

	// simexp ::= [unary] term {addop term}
	private Expr simexp() {
		Expr left = null, right = null;
		Symbol unary = null, addop = null;

		unary = unary();
		left = term();
		if (unary != null)
			left = new UnaryExpr(left, unary);
		else {
			addop = addop();
			while (addop != null) {
				right = term();
				left = new CompositeExpr(left, addop, right);
				addop = addop();
			}
		}

		return left;
	}

	// term ::= factor {mulop factor}
	private Expr term() {
		Expr left = null, right = null;
		Symbol mulop = null;

		left = factor();
		mulop = mulop();
		

		while (mulop != null) {
			right = factor();
			left = new CompositeExpr(left, mulop, right);
			mulop = mulop();
		}

		return left;
	}

	// factor ::= vbl | num | '(' expr ')' | '''.'''
	private Expr factor() {

		Expr factor = null;

		if (lexer.token == Symbol.IDPID)
			factor = vbl();
		else if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS
				|| lexer.token == Symbol.INTNUM)
			factor = num();
		else if (lexer.token == Symbol.LEFTPAR) {
			lexer.nextToken();
			factor = new ParenthesisExpr(expr());
			if (lexer.token != Symbol.RIGHTPAR)
				error.signal("Expecting ')'");
			lexer.nextToken();
		} else if (lexer.token == Symbol.APOSTROPHE) {
			lexer.nextToken();
			if (lexer.getStringValue().length() == 1)
				factor = new CharExpr(lexer.getStringValue());
			else
				factor = new StringExpr(lexer.getStringValue());
		} else {
			error.signal("Expecting expression");
		}
			
		return factor;
	}

	// id ::= letter {letter | digit}
	private String id() {
		String s = lexer.getStringValue();
		lexer.nextToken();
		return s;
	}

	// pid ::= letter {letter | digit}
	private String pid() {
		String s = lexer.getStringValue();
		lexer.nextToken();
		return s;
	}

	// num ::= ['+' | '-'] digit ['.'] {digit}
	private Expr num() {
		StringBuffer s = new StringBuffer();
		Expr num = null;

		if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS) {
			if (lexer.token == Symbol.MINUS)
				s.append("-");
			lexer.nextToken();
		}

		if (lexer.token == Symbol.INTNUM) {
			s.append(Integer.toString(lexer.getNumberValue()));
			lexer.nextToken();
		} else
			error.signal("Expecting a digit");

		if (lexer.token == Symbol.POINT) {
			s.append(".");
			lexer.nextToken();
			if (lexer.token == Symbol.INTNUM) {
				s.append(Integer.toString(lexer.getNumberValue()));
				lexer.nextToken();
				num = new NumberFloatExpr(Float.valueOf(s.toString())
						.floatValue());
			}
		} else
			return new NumberIntExpr(Integer.valueOf(s.toString()).intValue());

		return num;
	}

	// relop ::= '=' | '<' | '>' | '<=' | '>=' | '<>'
	private Symbol relop() {
		Symbol oper = null;

		if (lexer.token == Symbol.ASSIGN) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.LT) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.GT) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.LE) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.GE) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.DIFF) {
			oper = lexer.token;
			lexer.nextToken();
		}

		return oper;
	}

	// addop ::= '+' | '-' | OR
	private Symbol addop() {
		Symbol oper = null;

		if (lexer.token == Symbol.PLUS) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.MINUS) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.OR) {
			oper = lexer.token;
			lexer.nextToken();
		}

		return oper;
	}

	// mulop ::= '*' | '/' | AND | MOD | DIV
	private Symbol mulop() {
		Symbol oper = null;

		if (lexer.token == Symbol.MULTIPLICATION) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.DIVISION) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.AND) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.MOD) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.DIV) {
			oper = lexer.token;
			lexer.nextToken();
		}

		return oper;
	}

	// unary ::= '+' | '-' | NOT
	private Symbol unary() {
		Symbol oper = null;

		if (lexer.token == Symbol.PLUS) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.MINUS) {
			oper = lexer.token;
			lexer.nextToken();
		} else if (lexer.token == Symbol.NOT) {
			oper = lexer.token;
			lexer.nextToken();
		}

		return oper;
	}

	private Hashtable<String, Variable> symbolTable;
	private Lexer lexer;
	private CompilerError error;

}
