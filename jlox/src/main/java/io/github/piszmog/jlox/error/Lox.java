package io.github.piszmog.jlox.error;

import io.github.piszmog.jlox.scanner.Token;
import io.github.piszmog.jlox.scanner.TokenType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Lox {
    public boolean hadError;

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

    private void report(final int line, final String where, final String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
