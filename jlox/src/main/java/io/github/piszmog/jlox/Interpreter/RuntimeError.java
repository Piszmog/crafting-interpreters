package io.github.piszmog.jlox.Interpreter;

import io.github.piszmog.jlox.scanner.Token;
import lombok.Getter;

@Getter
public class RuntimeError extends RuntimeException {
    private final Token token;

    public RuntimeError(final Token token, final String message) {
        super(message);
        this.token = token;
    }
}
