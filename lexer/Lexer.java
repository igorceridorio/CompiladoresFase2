package lexer;

import ast.*;
import java.util.Hashtable;

public class Lexer {

	public Lexer(char[] input, CompilerError error) {
		this.input = input;
		this.error = error;
		input[input.length - 1] = '\0';
		lineNumber = 1;
		tokenPos = 0;
	}

	// static part of code that will be executed just once and contains the
	// keywords table
	static private Hashtable<String, Symbol> keywordsTable;

	static {
		keywordsTable = new Hashtable<String, Symbol>();
		keywordsTable.put("PROGRAM", Symbol.PROGRAM);
		keywordsTable.put("VAR", Symbol.VAR);
		keywordsTable.put("INTEGER", Symbol.INTEGER);
		keywordsTable.put("REAL", Symbol.REAL);
		keywordsTable.put("CHAR", Symbol.CHAR);
		keywordsTable.put("STRING", Symbol.STRING);
		keywordsTable.put("ARRAY", Symbol.ARRAY);
		keywordsTable.put("OF", Symbol.OF);
		keywordsTable.put("BEGIN", Symbol.BEGIN);
		keywordsTable.put("END", Symbol.END);
		keywordsTable.put("IF", Symbol.IF);
		keywordsTable.put("THEN", Symbol.THEN);
		keywordsTable.put("ELSE", Symbol.ELSE);
		keywordsTable.put("ENDIF", Symbol.ENDIF);
		keywordsTable.put("WHILE", Symbol.WHILE);
		keywordsTable.put("DO", Symbol.DO);
		keywordsTable.put("ENDWHILE", Symbol.ENDWHILE);
		keywordsTable.put("READ", Symbol.READ);
		keywordsTable.put("WRITE", Symbol.WRITE);
		keywordsTable.put("WRITELN", Symbol.WRITELN);
		keywordsTable.put("OR", Symbol.OR);
		keywordsTable.put("END", Symbol.END);
		keywordsTable.put("MOD", Symbol.MOD);
		keywordsTable.put("DIV", Symbol.DIV);
		keywordsTable.put("NOT", Symbol.NOT);
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getCurrentLine() {
		int i = lastTokenPos;

		if (i == 0)
			i = 1;
		else if (i >= input.length)
			i = input.length;

		StringBuffer line = new StringBuffer();

		while (i >= 1 && input[i] != '\n')
			i--;
		if (input[i] == '\n')
			i++;

		while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
			line.append(input[i]);
			i++;
		}

		return line.toString();
	}

	public String getStringValue() {
		return stringValue;
	}

	public int getNumberValue() {
		return numberValue;
	}

	public float getFloatNumberValue() {
		return floatNumberValue;
	}

	public char getCharValue() {
		return charValue;
	}

	public void nextToken() {
		char ch;

		// consuming invalid characters
		while ((ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t'
				|| ch == '\n') {
			// count the number of lines
			if (ch == '\n')
				lineNumber++;
			tokenPos++;
		}

		// if reaches the end of the input
		if (ch == '\0') {
			token = Symbol.EOF;
			// if founds a '{' means that a comment will begin
		} else if (input[tokenPos] == '{') {
			while (input[tokenPos] != '\0' && input[tokenPos] != '}'){
				tokenPos++;
			}
			tokenPos++;
			nextToken();
		} else {
			if (Character.isLetter(ch)) {
				// get an identifier or keyword
				StringBuffer idpid = new StringBuffer();
				// after the first token being a letter, 'id' and 'pid'
				// production rules allow digits
				while (Character.isLetter(input[tokenPos])
						|| Character.isDigit(input[tokenPos])) {
					idpid.append(input[tokenPos]);
					tokenPos++;
				}
				stringValue = idpid.toString();

				// checks if stringValue is a keyword
				Symbol value = keywordsTable.get(stringValue);
				if (value == null)
					token = Symbol.IDPID;
				else
					token = value;
			} else if (Character.isDigit(input[tokenPos])) {
				// get a number that may be composed by more than one digit
				StringBuffer intnum = new StringBuffer();
				while (Character.isDigit(input[tokenPos])) {
					intnum.append(input[tokenPos]);
					tokenPos++;
				}
				token = Symbol.INTNUM;
				// verifies if the number is within the defined range of values
				try {
					numberValue = Integer.valueOf(intnum.toString()).intValue();
				} catch (NumberFormatException e) {
					error.signal("Number out of limits");
				}
				if (numberValue >= MaxValueInteger)
					error.signal("Number out of limits");
			} else {

				// if it is neither a letter nor a digit, then it is some other
				// symbol
				tokenPos++;
				switch (ch) {
				case '+':
					token = Symbol.PLUS;
					break;
				case '-':
					token = Symbol.MINUS;
					break;
				case '*':
					token = Symbol.MULTIPLICATION;
					break;
				case '/':
					token = Symbol.DIVISION;
					break;
				case '<':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.LE;
					} else if (input[tokenPos] == '>') {
						tokenPos++;
						token = Symbol.DIFF;
					} else
						token = Symbol.LT;
					break;
				case '>':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.GE;
					} else
						token = Symbol.GT;
					break;
				case ':':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.EQUAL;
					} else
						token = Symbol.COLON;
					break;
				case ',':
					token = Symbol.COMMA;
					break;
				case ';':
					token = Symbol.SEMICOLON;
					break;
				case '.':
					token = Symbol.POINT;
					break;
				case '[':
					token = Symbol.LEFTBRA;
					break;
				case ']':
					token = Symbol.RIGHTBRA;
					break;
				case '(':
					token = Symbol.LEFTPAR;
					break;
				case ')':
					token = Symbol.RIGHTPAR;
					break;
				case '=':
					token = Symbol.ASSIGN;
					break;
				case '\'':
					token = Symbol.APOSTROPHE;
					StringBuffer phrase = new StringBuffer();
					while (input[tokenPos] != '\'') {
						phrase.append(input[tokenPos]);
						tokenPos++;
					}
					stringValue = phrase.toString();
					tokenPos++;
					break;
				case '}':
					break;
				default:
					error.signal("Invalid character: '" + ch + "'");
				}
			}
		}
		lastTokenPos = tokenPos - 1;
	}

	public Symbol token;
	private char[] input;
	private int tokenPos;
	private int lastTokenPos;

	private String stringValue;
	private char charValue;
	private int numberValue;
	private int lineNumber;

	private float floatNumberValue;

	private CompilerError error;
	private static final int MaxValueInteger = 32768;

}