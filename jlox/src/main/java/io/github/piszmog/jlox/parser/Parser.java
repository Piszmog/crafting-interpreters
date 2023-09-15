package io.github.piszmog.jlox.parser;

import io.github.piszmog.jlox.error.Lox;
import io.github.piszmog.jlox.expr.*;
import io.github.piszmog.jlox.scanner.Token;
import io.github.piszmog.jlox.scanner.TokenType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public List<Stmt> parse() {
        final ArrayList<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.VAR)) {
                return varDeclaration();
            }
            return statement();
        } catch (ParserError e) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() throws ParserError {
        final Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        Expr initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new Var(name, initializer);
    }

    private Stmt statement() throws ParserError {
        if (match(TokenType.PRINT)) {
            return printStatement();
        } else if (match(TokenType.LEFT_BRACE)) {
            return new Block(block());
        }
        return expressionStatement();
    }

    private Stmt printStatement() throws ParserError {
        final Expr value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Print(value);
    }

    private List<Stmt> block() throws ParserError {
        final ArrayList<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Stmt expressionStatement() throws ParserError {
        final Expr value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new Expression(value);
    }

    private Expr expression() throws ParserError {
        // expression → assignment ;
        return assignment();
    }

    private Expr assignment() throws ParserError {
        // assignment → IDENTIFIER "=" assignment | equality ;
        final Expr expr = equality();
        if (match(TokenType.EQUAL)) {
            final Token equals = previous();
            final Expr val = assignment();
            if (expr instanceof Variable) {
                return new Assign(((Variable) expr).name(), val);
            }
            error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    private Expr equality() throws ParserError {
        // equality → comparison ( ( "!=" | "==" ) comparison )* ;
        Expr expr = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            final Token operator = previous();
            final Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() throws ParserError {
        // comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
        Expr expr = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            final Token operator = previous();
            final Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() throws ParserError {
        // term → factor ( ( "-" | "+" ) factor )* ;
        Expr expr = factor();
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            final Token operator = previous();
            final Expr right = factor();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() throws ParserError {
        // factor → unary ( ( "/" | "*" ) unary )* ;
        Expr expr = unary();
        while (match(TokenType.SLASH, TokenType.STAR)) {
            final Token operator = previous();
            final Expr right = unary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() throws ParserError {
        // unary → ( "!" | "-" ) unary  | primary ;
        if (match(TokenType.BANG, TokenType.MINUS)) {
            final Token operator = previous();
            final Expr right = unary();
            return new Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() throws ParserError {
        // primary → NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Literal(previous().literal());
        } else if (match(TokenType.TRUE)) {
            return new Literal(true);
        } else if (match(TokenType.FALSE)) {
            return new Literal(false);
        } else if (match(TokenType.NIL)) {
            return new Literal(null);
        } else if (match(TokenType.LEFT_PAREN)) {
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expression());
        } else if (match(TokenType.IDENTIFIER)) {
            return new Variable(previous());
        }
        throw error(peek(), "Expected expression.");
    }

    private boolean match(final TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(final TokenType type, final String message) throws ParserError {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private boolean check(final TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type() == type;
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type() == TokenType.SEMICOLON) {
                return;
            }
            switch (peek().type()) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> {
                    return;
                }
            }
            advance();
        }
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParserError error(final Token token, final String message) {
        Lox.error(token, message);
        return new ParserError();
    }
}
