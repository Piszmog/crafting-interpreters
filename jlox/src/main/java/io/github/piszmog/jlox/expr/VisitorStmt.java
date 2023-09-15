package io.github.piszmog.jlox.expr;

public interface VisitorStmt<T> {
    T visitBlockStmt(final Block stmt);

    T visitExpressionStmt(final Expression stmt);

    T visitPrintStmt(final Print stmt);

    T visitVarStmt(final Var stmt);
}
