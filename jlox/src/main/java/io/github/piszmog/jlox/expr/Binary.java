package io.github.piszmog.jlox.expr;

import io.github.piszmog.jlox.scanner.Token;

public record Binary(Expr left, Token operator, Expr right) implements Expr {
    @Override
    public <T> T accept(final VisitorExpr<T> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}
