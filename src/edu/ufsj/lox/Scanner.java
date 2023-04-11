package edu.ufsj.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.ufsj.lox.TokenType.*;

class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<Token>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	
	Scanner(String source){
		this.source = source;
	}
	
	List <Token> scanTokens(){
		while (!isAtEnd()) {
			//inicio do proximo lexema
			start = current;
			scanToken();
		}
		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	private char advance() {
		current++;
		return source.charAt(current-1);
	}
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}
	
	private boolean match(char expected) {
		if (isAtEnd()) return false;
		if(source.charAt(current) != expected)
			return false;
		
		current++;
		return true;
	}
	
	private char peek() {
		if(isAtEnd()) return '\0';
		return source.charAt(current);
	}
	
	private boolean endOfComment(char expected1, char expected2) {
		if(isAtEnd()) return false;
		if(source.charAt(current) == expected1) {
			current++;
			if(source.charAt(current) == expected2){
				current++;
				return true;
			}
		}
		return false;
	}
	
	private void string() {
		while(peek()!= '"' && !isAtEnd()) {
			if(peek() == '\n') line++;
			advance();
		}
		//Abre " sem o retrospectivo fecha "
		if(isAtEnd()) {
			Lox.error(line, "Undeterminated string.");
			return;
		}
		//O fecha "
		advance();
		//remove os "s
		String value = source.substring(start + 1, current - 1);
		addToken(STRING, value);
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private void number() {
		while(isDigit(peek())) advance();
		
		//
		if(peek() == '.' && isDigit(peekNext())) {
			//Consome o .
			advance();
			
			while(isDigit(peek())) advance();
		}
		
		addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
	}
	
	private void identifier() {
		while(isAlphaNumeric(peek())) advance();
		
		addToken(IDENTIFIER);
	}
	
	private boolean isAlpha(char c) {
		return(c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <- 'Z') ||
				c == '_';
	}
	
	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}
	
	private char peekNext() {
		if(current + 1 >= source.length()) return '\0';
		return source.charAt(current + 1);
	}
	
	private void scanToken() {
		char c = advance();
		switch (c) {
		case '(': addToken(LEFT_PAREN); break;
		case ')' : addToken(RIGHT_PAREN); break;
		case '{': addToken(LEFT_BRACE); break;
		case '}': addToken(RIGHT_BRACE); break;
		case ',': addToken(COMMA); break;
		case '.': addToken(DOT); break;
		case '-': addToken(MINUS); break;
		case '+': addToken(PLUS); break;
		case ';': addToken(SEMICOLON); break;
		case '*': addToken(STAR); break;
		case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
		case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
		case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
		case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
		case '/': if(match('/')) {
			//comentarios de uma linha
			while(peek() != '\n' && !isAtEnd()) advance();
		} else if(match('*')){
			
			//---------- DESAFIO 1: ----------
			//comentarios de mais de uma linha
			while(!endOfComment('*', '/') && !isAtEnd()){
				advance();				
			}
		}else {
			addToken(SLASH);
		} break;
		case '"':
			string();
			break;
		case ' ':
		case '\r':
		case '\t':
			//Ignora espaaçoes em branco
			break;
		case '\n':
			line++;
			break;
		case 'o':
			if(peek() == 'r') {
				addToken(OR);
			}
			break;
		default:
			if (isDigit(c)) {
				number();
			}else if (isAlpha(c)){
				identifier();
			}else {
				Lox.error(line, "Unexpected character");
			}
			break;
		}
	}
}
