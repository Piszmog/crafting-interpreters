package io.github.piszmog.jlox.expr;

public interface Visitor<T> {
    T visitBinaryExpr(final Binary expr);

    T visitGroupingExpr(final Grouping expr);

    T visitLiteralExpr(final Literal expr);

    T visitUnaryExpr(final Unary expr);
}
