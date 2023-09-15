package io.github.piszmog.jlox.environment;

import io.github.piszmog.jlox.Interpreter.RuntimeError;
import io.github.piszmog.jlox.scanner.Token;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;

    public Environment() {
        enclosing = null;
    }

    public void define(final String name, final Object val) {
        values.put(name, val);
    }

    public Object get(final Token name) {
        if (values.containsKey(name.lexeme())) {
            return values.get(name.lexeme());
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme() + "'.");
    }

    public void assign(final Token name, final Object val) {
        if (values.containsKey(name.lexeme())) {
            values.put(name.lexeme(), val);
        }
        if (enclosing != null) {
            enclosing.assign(name, val);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme() + "'.");
    }
}
