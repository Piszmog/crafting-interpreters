package io.github.piszmog.jlox.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        defineAst(
                args[0],
                "Expr",
                Arrays.asList(
                        "Binary   : Expr left, Token operator, Expr right",
                        "Grouping : Expr expression",
                        "Literal  : Object value",
                        "Unary    : Token operator, Expr right"
                )
        );
        defineAst(
                args[0],
                "Stmt",
                Arrays.asList(
                        "Expression : Expr expression",
                        "Print      : Expr expression"
                )
        );
    }

    private static void defineAst(final String outputDir, final String baseName, final List<String> types) throws IOException {
        final String visitorName = "Visitor" + baseName;
        writeVisitorInterface(outputDir, baseName, visitorName, types);
        writeBaseClass(outputDir, baseName, visitorName);

        for (String type : types) {
            writeClass(outputDir, baseName, visitorName, type);
        }
    }

    private static void writeVisitorInterface(
            final String outputDir,
            final String baseName,
            final String name,
            final List<String> types
    ) throws IOException {
        final String path = getPath(outputDir, name);
        final PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package io.github.piszmog.jlox.expr;");
        writer.println();
        writer.printf("public interface %s<T> {\n", name);
        types.forEach(type -> {
            final String typeName = type.split(":")[0].trim();
            writer.printf("    T visit%s%s(final %s %s);\n", typeName, baseName, typeName, baseName.toLowerCase());
        });
        writer.println("}");
        writer.close();
    }

    private static void writeBaseClass(final String outputDir, final String name, final String visitorName) throws IOException {
        final String path = getPath(outputDir, name);
        final PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package io.github.piszmog.jlox.expr;");
        writer.println();
        writer.printf("public interface %s {\n", name);
        writer.printf("    <T> T accept(final %s<T> visitor);\n", visitorName);
        writer.println("}");
        writer.close();
    }

    private static void writeClass(final String outputDir, final String baseClass, final String visitorName, final String type) throws IOException {
        String name = type.split(":")[0].trim();
        String fields = type.split(":")[1].trim();

        final String path = getPath(outputDir, name);
        final PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package io.github.piszmog.jlox.expr;");
        writer.println();
        if (fields.contains("Token")) {
            writer.println("import io.github.piszmog.jlox.scanner.Token;");
            writer.println();
        }
        writer.printf("public record %s(%s) implements %s {\n", name, fields, baseClass);
        writer.println("    @Override");
        writer.printf("    public <T> T accept(final %s<T> visitor) {\n", visitorName);
        writer.printf("        return visitor.visit%s%s(this);\n", name, baseClass);
        writer.println("    }");
        writer.println("}");
        writer.close();
    }

    private static String getPath(final String outputDir, final String name) {
        return outputDir + "/" + name + ".java";
    }
}
