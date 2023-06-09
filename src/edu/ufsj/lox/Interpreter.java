package edu.ufsj.lox;

public class Interpreter implements Expr.Visitor<Object> {
	
	@Override
	public Object visitLiteralExpr(Expr.Literal expr) {
		return expr.value;
	}
	
	@Override
	public Object visitGroupingExpr(Expr.Grouping expr) {
		return evaluate(expr.expression);
	}
	
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	@Override
		public Object visitUnaryExpr(Expr.Unary expr) {
			Object right = evaluate(expr.right);
			
			switch(expr.operator.type) {
				case MINUS:
					return -(double)right;
				case BANG:
					return !isTruthy(right);
			}
			
			return null;
		}
	
	private boolean isTruthy (Object object) {
		if(object == null)
			return false;
		
		if(object instanceof Boolean)
			return (boolean)object;
		
		return true;
	}
	
	@Override
	public Object visitBinaryExpr(Expr.Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch (expr.operator.type) {
		case MINUS:
			checkNumberOperand(expr.operator, right);
			return (double)left - (double)right;
		case GREATER:
			checkNumberOperands(expr.operator, left, right);
			return (double)left > (double)right;
		case GREATER_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (double)left >= (double)right;
		case LESS:
			checkNumberOperands(expr.operator, left, right);
			return (double)left < (double)right;
		case LESS_EQUAL:
			return (double)left <= (double)right;
		case BANG_EQUAL:
			return !isEqual(left, right);
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case PLUS:
			if (left instanceof Double && right instanceof Double) {
				return (double)left + (double)right;
			}
			
			if (left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}
		throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings. ");
		case SLASH:
			return (double)left / (double)right;
		case STAR:
			return (double)left * (double)right;
		}
		
		return null;
	}
	
	private boolean isEqual(Object a, Object b) {
		if(a==null && b == null) return true;
		if (a==null) return false;
		
		return a.equals(b);
	}
	
	private void checkNumberOperand(Token operator, Object operand) {
		
		if(operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must a number. ");
	}
	
	private void checkNumberOperands (Token operator, Object left, Object right) {
		if (left instanceof Double && right instanceof Double) return;
		
		throw new RuntimeError(operator, "Operands must be numbers. ");
	}
	
	void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		} catch (RuntimeError error) {
			Lox.runtimeError(error);
		}
	}
	
	private String stringify(Object object) {
		if (object == null) return "nil";
		
		if (object instanceof Double) {
			String text = object.toString();
			if(text.endsWith(".0")) {
				text = text.substring(0, text.length() - 2);
			}
			return text;
		}
		
		return object.toString();
	}
	
	@Override
	public Object visitTernaryExpr(Expr.Ternary expr) {
	    Object condition = evaluate(expr.condition); //Condicao do Operador Ternario
	    Object trueExpr = evaluate(expr.trueExpr); //Retorno se a condicao for verdade
	    Object falseExpr = evaluate(expr.falseExpr); //Retorno se a condicao for falsa
	    
	    // Avalia se a condicao passada no operador ternario eh satisfeita
	    if (isTruthy(condition)) return trueExpr; //Se for, retorna a expressao verdadeira
	    else return falseExpr; //Senao, retorna a expressao falsa
	}

}
