package io.github.piszmog.jlox.environment;

import io.github.piszmog.jlox.Interpreter.RuntimeError;
import io.github.piszmog.jlox.scanner.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    public void define(final String name, final Object val) {
        values.put(name, val);
    }

    public Object get(final Token token) {
        if (!values.containsKey(token.lexeme())) {
            throw new RuntimeError(token, "Undefined variable '" + token.lexeme() + "'.");
        }
        return values.get(token.lexeme());
    }

    public void assign(final Token name, final Object val) {
        if (!values.containsKey(name.lexeme())) {
            throw new RuntimeError(name, "Undefined variable '" + name.lexeme() + "'.");
        }
        values.put(name.lexeme(), val);
    }
}
