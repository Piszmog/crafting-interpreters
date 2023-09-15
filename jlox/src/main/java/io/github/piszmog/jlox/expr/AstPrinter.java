package io.github.piszmog.jlox.expr;

public class AstPrinter implements VisitorExpr<String> {
    public String print(final Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator().lexeme(), expr.left(), expr.right());
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression());
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value() == null) {
            return "nil";
        }
        return expr.value().toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator().lexeme(), expr.right());
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return null;
    }

    private String parenthesize(final String name, final Expr... exprs) {
        final StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ")
                    .append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
