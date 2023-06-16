package edu.ufsj.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox{
	
	private static final Interpreter interpreter = new Interpreter();
	
	static boolean hadError = false;
	static boolean hadRuntimeError = false;
			
	public static void main(String[] args)
	throws IOException{ 
		//Testa quantos argumentos o usu�rio passou
		if(args.length > 1) { 
			//Se for maior que 1 argumento retorna erro
			System.out.println("Usage: jlox [script]");
			System.exit(64);
		}else if(args.length == 1) {
			runFile(args[0]);
			  
		}else { 
			//Faz o REPL
			runPrompt();
		}
	}
	
	private static void runFile(String path) throws IOException { 
		//Metodo que vai para o arquivo
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));
		if(hadError) System.exit(65);
		if(hadRuntimeError) System.exit(70);
	}
	
	private static void runPrompt() throws IOException{ 
		//Executa o interpretador em REPL
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		for(;;) {
			System.out.print(">");
			String line = reader.readLine();
			if(line == null){
				break;
			}
			//Se tiver comentario de multiplas linhas:
			else if(line.contains("/*")) {
				while(!line.contains("*/")) {
					System.out.print(">");
					line = reader.readLine();
				}
				continue;
			}//Terminou o comentario de multiplas linhas
			
			run(line);
			hadError = false;
		}
	}

	
	private static void run(String source) { 
		//Interpretar ou um arquivo, ou uma linha so
		Scanner scanner = new Scanner(source); //Processa o source e quebra em uma lista de tokens
		List<Token> tokens = scanner.scanTokens();
		//por enquanto, somente mostra os tokens
		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();
		
		//em caso de erro, encerra a analise
		if(hadError) return;
		interpreter.interpret(expression);
		
		//System.out.println(new AstPrinter().print(expression));
//		for(Token token : tokens)
//			System.out.println(token);
	}
	
	static void error(int line, String message) {
		report(line, "", message);
	}
	
	static void error(Token token, String message) {
		if(token.type == TokenType.EOF) {
			report(token.line, " at end", message);
		} else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
	
	private static void report(int line, String where, String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
	}
	
	static void runtimeError(RuntimeError error) {
		System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
		hadRuntimeError = true;
	}
	
	
}
