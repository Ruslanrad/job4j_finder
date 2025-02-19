package ru.job4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        input = input.replaceAll("\\s+", " ");
        String[] parts = input.split(" ");
        List<Path> source = new ArrayList<>();

        ArgsName argsName = ArgsName.of(parts);
        String directory = argsName.get("d");
        String fileName = argsName.get("n");
        String tipeSearch = argsName.get("t");
        String pathOutput = argsName.get("o");
        Path start = Paths.get(directory);

        if ("name".equals(tipeSearch)) {
            source = Search.search(start, path -> path.toFile().getName().equals(fileName)).stream().toList();
        }
        if ("mask".equals(tipeSearch)) {
            StringBuilder maskRegular = new StringBuilder();
            char[] charsMask = fileName.toCharArray();
            for (char c : charsMask) {
                switch (c) {
                    case '.' -> maskRegular.append("\\.");
                    case '*' -> maskRegular.append(".*");
                    case '?' -> maskRegular.append(".{1}");
                    default -> maskRegular.append(c);
                }
            }
            Pattern pattern = Pattern.compile(maskRegular.toString());
            source = Search.search(start, path -> path.toFile().getName().matches(pattern.pattern())).stream().toList();
        }
        if ("regex".equals(tipeSearch)) {
            try {
                Pattern.compile(fileName);
                source = Search.search(start, path -> path.toFile().getName().matches(fileName)).stream().toList();
            } catch (PatternSyntaxException e) {
                e.printStackTrace();
            }
        }
        try (PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(pathOutput)))) {
            for (Path path : source) {
                output.println(path.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
