package io.github.piszmog.jlox.expr;

import java.util.List;

public record Block(List<Stmt> statements) implements Stmt {
    @Override
    public <T> T accept(final VisitorStmt<T> visitor) {
        return visitor.visitBlockStmt(this);
    }
}
