# CompiladoresFase2

Projeto de Implementação de um Compilador - Segunda Fase. Disciplina de Compiladores.

### Desenvolvedores: 

408611 - Igor Felipe Ferreira Ceridorio.

438340 - Daniel Ramos Miola.

### Gramática implementada:
```
	- prog ::= PROGRAM pid ’;’ body ’.’
  - body ::= [dclpart] compstmt
  - dclpart ::= VAR dcls
  - dcls ::= dcl {dcl}
  - dcl ::= idlist ’:’ type ’;’
  - idlist ::= id {’,’ id}
  - type ::= stdtype | arraytype
  - stdtype ::= INTEGER | REAL | CHAR | STRING
  - arraytype ::= ARRAY ’[’ intnum ’..’ intnum ’]’ OF stdtype
  - compstmt ::= BEGIN stmts END
  - stmts ::= stmt {’;’ stmt} ’;’
  - stmt ::= ifstmt | whilestmt | assignstmt | compstmt | readstmt | writestmt | writelnstmt
  - ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
  - whilestmt ::= WHILE expr DO stmts ENDWHILE
  - assignstmt ::= vbl ’:=’ expr
  - readstmt ::= READ ’(’ vblist ’)’
  - writestmt ::= WRITE ’(’ exprlist ’)’
  - writelnstmt ::= WRITELN ’(’ [exprlist] ’)’
  - vblist ::= vbl {’,’ vbl}
  - vbl ::= id [’[’ expr ’]’]
  - exprlist ::= expr {’,’ expr}
  - expr ::= simexp [relop expr]
  - simexp ::= [unary] term {addop term}
  - term ::= factor {mulop factor}
  - factor ::= vbl | num | ’(’ expr ’)’ | ”’.”’
  - id ::= letter {letter | digit}
  - pid ::= letter {letter | digit}
  - num ::= [’+’ | ’-’] digit [’.’]
  - intnum ::= digit {digit}
  - relop ::= ’=’ | ’<’ | ’>’ | ’<=’ | ’>=’ | ’<>’
  - addop ::= ’+’ | ’-’ | OR
  - mulop ::= ’*’ | ’/’ | AND | MOD | DIV
  - unary ::= ’+’ | ’-’ | NOT
```
