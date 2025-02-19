package ru.job4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgsName {


    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '" + key + "' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        Pattern doesNotContainKey = Pattern.compile("^-=.+");
        Pattern doesNotContainValue = Pattern.compile("^-[^=]+=$");
        Pattern noHyphenPrefix = Pattern.compile("^[^-]+");
        Pattern thereNoEqualSign = Pattern.compile("^-[^=]+$");
        Pattern pattern = Pattern.compile("^-([^=]+)=(.+)");
        for (String arg : args) {
            if (doesNotContainKey.matcher(arg).matches()) {
                throw new IllegalArgumentException("Error: This argument '" + arg + "' does not contain a key");
            }
            if (doesNotContainValue.matcher(arg).matches()) {
                throw new IllegalArgumentException("Error: This argument '" + arg + "' does not contain a value");
            }
            if (thereNoEqualSign.matcher(arg).matches()) {
                throw new IllegalArgumentException("Error: This argument '" + arg + "' does not contain an equal sign");
            }
            if (noHyphenPrefix.matcher(arg).matches()) {
                throw new IllegalArgumentException("Error: This argument '" + arg + "' does not start with a '-' character");
            }
            Matcher matcher = pattern.matcher(arg);
            if (matcher.matches()) {
                values.put(matcher.group(1), matcher.group(2));
            }
        }
    }

    public static ArgsName of(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }

    public static void main(String[] args) {
        ArgsName jvm = ArgsName.of(new String[] {"-Xmx=512", "-encoding=UTF-8"});
        System.out.println(jvm.get("Xmx"));
        System.out.println(jvm.get("encoding"));

        ArgsName zip = ArgsName.of(new String[] {"-out=project.zip", "-encoding=UTF-8"});
        System.out.println(zip.get("out"));
    }
}
