package io.github.piszmog.jlox.error;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Lox {
    public boolean hadError;

    public void error(final int line, final String message) {
        report(line, "", message);
    }

    private static void report(final int line, final String where, final String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
