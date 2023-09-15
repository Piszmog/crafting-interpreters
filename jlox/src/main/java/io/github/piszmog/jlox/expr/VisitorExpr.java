package io.github.piszmog.jlox.expr;

public interface VisitorExpr<T> {
    T visitAssignExpr(final Assign expr);

    T visitBinaryExpr(final Binary expr);

    T visitGroupingExpr(final Grouping expr);

    T visitLiteralExpr(final Literal expr);

    T visitUnaryExpr(final Unary expr);

    T visitVariableExpr(final Variable expr);
}
