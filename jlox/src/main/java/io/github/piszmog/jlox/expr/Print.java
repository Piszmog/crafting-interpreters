package io.github.piszmog.jlox.expr;

public record Print(Expr expression) implements Stmt {
    @Override
    public <T> T accept(final VisitorStmt<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
