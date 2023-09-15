package io.github.piszmog.jlox;

import io.github.piszmog.jlox.error.Lox;
import io.github.piszmog.jlox.expr.AstPrinter;
import io.github.piszmog.jlox.expr.Expr;
import io.github.piszmog.jlox.parser.Parser;
import io.github.piszmog.jlox.scanner.Scanner;
import io.github.piszmog.jlox.scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(final String path) throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (Lox.hadError) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        final InputStreamReader input = new InputStreamReader(System.in);
        final BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.println("> ");
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            Lox.hadError = false;
        }
    }

    private static void run(final String source) {
        final Scanner scanner = new Scanner(source);
        final List<Token> tokens = scanner.scanTokens();
        final Parser parser = new Parser(tokens);
        final Expr expr = parser.parse();
        if (Lox.hadError) {
            return;
        }
        System.out.println(new AstPrinter().print(expr));
    }
}
