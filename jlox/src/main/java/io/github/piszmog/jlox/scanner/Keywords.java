package io.github.piszmog.jlox.scanner;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class Keywords {
    private final Map<String, TokenType> keywords = Map.ofEntries(
            Map.entry("and", TokenType.AND),
            Map.entry("class", TokenType.CLASS),
            Map.entry("else", TokenType.ELSE),
            Map.entry("false", TokenType.FALSE),
            Map.entry("for", TokenType.FOR),
            Map.entry("fun", TokenType.FUN),
            Map.entry("if", TokenType.IF),
            Map.entry("nil", TokenType.NIL),
            Map.entry("or", TokenType.OR),
            Map.entry("print", TokenType.PRINT),
            Map.entry("return", TokenType.RETURN),
            Map.entry("super", TokenType.SUPER),
            Map.entry("this", TokenType.THIS),
            Map.entry("true", TokenType.TRUE),
            Map.entry("var", TokenType.VAR),
            Map.entry("while", TokenType.WHILE)
    );

    public TokenType get(final String value) {
        return keywords.get(value);
    }
}
