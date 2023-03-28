package edu.ufsj.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.ufsj.lox.TokenType.*;

class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<Token>();
	
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
	
}
