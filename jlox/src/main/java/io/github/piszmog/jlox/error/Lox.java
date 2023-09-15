package io.github.piszmog.jlox.error;

import io.github.piszmog.jlox.Interpreter.RuntimeError;
import io.github.piszmog.jlox.scanner.Token;
import io.github.piszmog.jlox.scanner.TokenType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Lox {
    @Getter
    @Setter
    private boolean hadError;
    @Getter
    private boolean hadRuntimeError;

    public void error(final int line, final String message) {
        report(line, "", message);
    }

    public void error(final Token token, final String message) {
        if (token.type() == TokenType.EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '" + token.lexeme() + "'", message);
        }
    }

    public void runtimeError(RuntimeError e) {
        System.out.println(e.getMessage() + "\n[line " + e.getToken().line() + "]");
        hadRuntimeError = true;
    }

    private void report(final int line, final String where, final String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
