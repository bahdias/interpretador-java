package edu.ufsj.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox{
	
	static boolean hadError = false;
			
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
		//M�todo que vai para o arquivo
				byte[] bytes = Files.readAllBytes(Paths.get(path));
				run(new String(bytes, Charset.defaultCharset()));
				if(hadError) System.exit(65);
	}
	
	private static void runPrompt() throws IOException{ 
		//Executa o interpretador em REPL
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		for(;;) {
			System.out.print("> ");
			String line = reader.readLine();
			if(line == null) break;
			run(line);
			hadError = false;
		}
	}

	

	private static void run(String source) { 
		//Interpretar ou um arquivo, ou uma linha s�
		Scanner scanner = new Scanner(source); //Processa o source e quebra em uma lista de tokens
		List<Token> tokens = scanner.scanTokens();
		//por enquanto, somente mostra os tokens
		for(Token token : tokens)
			println(token);
	}
	
	static void error(int line, String message) {
		report(line, "", message);
	}
	
	private static void report(int line, String where, String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
	}
}