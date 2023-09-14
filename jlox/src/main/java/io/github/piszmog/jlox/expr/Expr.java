package io.github.piszmog.jlox.expr;

public interface Expr {
    <T> T accept(final Visitor<T> visitor);
}
