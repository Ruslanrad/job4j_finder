package ru.job4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class Search {
    public static void main(String[] args) throws IOException {
        argsValidate(args);
        Path start = Paths.get(args[0]);
        search(start, path -> path.toFile().getName().endsWith(args[1])).forEach(System.out::println);
    }

    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    public static void argsValidate(String[] arguments) {
        if (arguments.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        if (!".".equals(arguments[0])) {
            throw new IllegalArgumentException("Wrong is directory.");
        }
        if (!".js".equals(arguments[1])) {
            throw new IllegalArgumentException("Wrong file extension.");
        }
    }
}