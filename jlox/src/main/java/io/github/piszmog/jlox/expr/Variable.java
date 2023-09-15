package io.github.piszmog.jlox.expr;

import io.github.piszmog.jlox.scanner.Token;

public record Variable(Token name) implements Expr {
    @Override
    public <T> T accept(final VisitorExpr<T> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
