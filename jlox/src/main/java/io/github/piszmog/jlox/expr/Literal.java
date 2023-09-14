package io.github.piszmog.jlox.expr;

public record Literal(Object value) implements Expr {
    @Override
    public <T> T accept(final Visitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}
