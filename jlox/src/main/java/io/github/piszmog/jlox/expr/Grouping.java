package io.github.piszmog.jlox.expr;

public record Grouping(Expr expression) implements Expr {
    @Override
    public <T> T accept(final VisitorExpr<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }
}
