package edu.ufsj.lox;


abstract class Expr {
   interface Visitor<R> {
      R visitBinaryExpr(Binary expr);
      R visitGroupingExpr(Grouping expr);
      R visitLiteralExpr(Literal expr);
      R visitUnaryExpr(Unary expr);
      R visitTernaryExpr(Ternary expr);
  }
   static class Binary extends Expr {
      Binary(Expr left, Token operator, Expr right) {
         this.left = left;
         this.operator = operator;
         this.right = right;
      }
      @Override
      <R> R accept (Visitor<R> visitor) {
         return visitor.visitBinaryExpr(this);
      }

      final Expr left;
      final Token operator;
      final Expr right;
   }
   static class Grouping extends Expr {
      Grouping(Expr expression) {
         this.expression = expression;
      }
      @Override
      <R> R accept (Visitor<R> visitor) {
         return visitor.visitGroupingExpr(this);
      }

      final Expr expression;
   }
   static class Literal extends Expr {
      Literal(Object value) {
         this.value = value;
      }
      @Override
      <R> R accept (Visitor<R> visitor) {
         return visitor.visitLiteralExpr(this);
      }

      final Object value;
   }
   static class Unary extends Expr {
      Unary(Token operator, Expr right) {
         this.operator = operator;
         this.right = right;
      }
      @Override
      <R> R accept (Visitor<R> visitor) {
         return visitor.visitUnaryExpr(this);
      }

      final Token operator;
      final Expr right;
   }

   static class Ternary extends Expr {
	    Ternary(Expr condition, Expr trueExpr, Expr falseExpr) {
	        this.condition = condition; //Armazena a condicao da expressao
	        this.trueExpr = trueExpr; //Armazena o retorno se a expressão for verdadeira
	        this.falseExpr = falseExpr; //Armazena o retorno se a expressão for falsa
	    }

	    @Override
	    <R> R accept(Visitor<R> visitor) {
	    	//Recebe o objeto do tipo "Visitor" e chama o metodo apropriado desse objeto para processar o operador ternario
	        return visitor.visitTernaryExpr(this);  
	    }

	    //Essas constantes sao inicializadas pelo construtor da classe "Ternary", 
	    //que recebe tres argumentos e atribui seus valores as respectivas variaveis.
	    final Expr condition;
	    final Expr trueExpr;
	    final Expr falseExpr;
	}

   
   abstract <R> R accept(Visitor<R> visitor);
}
