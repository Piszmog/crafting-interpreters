package io.github.piszmog.jlox.expr;

import io.github.piszmog.jlox.scanner.Token;

public record Var(Token name, Expr initializer) implements Stmt {
    @Override
    public <T> T accept(final VisitorStmt<T> visitor) {
        return visitor.visitVarStmt(this);
    }
}
