package io.github.piszmog.jlox.Interpreter;

import io.github.piszmog.jlox.error.Lox;
import io.github.piszmog.jlox.expr.*;
import io.github.piszmog.jlox.scanner.Token;

import java.util.List;

public class Interpreter implements VisitorExpr<Object>, VisitorStmt<Void> {
    public void interpret(final List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError e) {
            Lox.runtimeError(e);
        }
    }

    private void execute(final Stmt statement) {
        statement.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        final Object left = evaluate(expr.left());
        final Object right = evaluate(expr.right());

        switch (expr.operator().type()) {
            case MINUS -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left - (double) right;
            }
            case PLUS -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                } else if (left instanceof String && right instanceof String) {
                    return left + (String) right;
                } else if (left instanceof String && right instanceof Double) {
                    return left + String.valueOf(right);
                } else if (left instanceof Double && right instanceof String) {
                    return String.valueOf(left) + right;
                }
            }
            case SLASH -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left / (double) right;
            }
            case STAR -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left * (double) right;
            }
            case GREATER -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left >= (double) right;
            }
            case LESS -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left < (double) right;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.operator(), left, right);
                return (double) left <= (double) right;
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right);
            }
            case EQUAL_EQUAL -> {
                return isEqual(left, right);
            }
        }
        return null;
    }

    private void checkNumberOperands(final Token operator, final Object... operands) {
        for (Object operand : operands) {
            checkNumberOperand(operator, operand);
        }
    }

    private boolean isEqual(final Object left, final Object right) {
        if (left == null && right == null) {
            return true;
        } else if (left == null) {
            return false;
        } else if (right == null) {
            return false;
        } else {
            return left.equals(right);
        }
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression());
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value();
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        final Object right = evaluate(expr.right());
        switch (expr.operator().type()) {
            case MINUS -> {
                checkNumberOperand(expr.operator(), right);
                return -(double) right;
            }
            case BANG -> {
                return !isTruthy(right);
            }
        }
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression());
        return null;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        final Object val = evaluate(stmt.expression());
        System.out.println(stringify(val));
        return null;
    }

    private Object evaluate(final Expr expr) {
        return expr.accept(this);
    }

    private void checkNumberOperand(final Token operator, final Object operand) {
        if (!(operand instanceof Double)) {
            throw new RuntimeError(operator, "Operand must be a number.");
        }
    }

    private boolean isTruthy(final Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Boolean) {
            return (boolean) obj;
        } else {
            return true;
        }
    }

    private String stringify(Object object) {
        if (object == null) {
            return "nil";
        }

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}
