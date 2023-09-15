package io.github.piszmog.jlox.expr;

import io.github.piszmog.jlox.scanner.Token;

public record Assign(Token name, Expr value) implements Expr {
    @Override
    public <T> T accept(final VisitorExpr<T> visitor) {
        return visitor.visitAssignExpr(this);
    }
}
