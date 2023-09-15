package io.github.piszmog.jlox.expr;

public interface Expr {
    <T> T accept(final VisitorExpr<T> visitor);
}
