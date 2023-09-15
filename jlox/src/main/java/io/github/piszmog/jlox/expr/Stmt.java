package io.github.piszmog.jlox.expr;

public interface Stmt {
    <T> T accept(final VisitorStmt<T> visitor);
}
