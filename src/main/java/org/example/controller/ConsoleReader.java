package org.example.controller;

import org.example.service.Finder;
import org.example.service.Phrase;
import org.example.util.StringUtils;
import java.util.Map;
import java.util.Scanner;

public class ConsoleReader {
    private final Finder finder;

    public ConsoleReader(Finder finder) {
        this.finder = finder;
    }

    public void start() {
        var keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();
            if (line.equals(":quit")) {
                break;
            }

            Map<Phrase, Double> result = finder.find(line);

            System.out.println(format(result));
        }
    }

    private String format(Map<Phrase, Double> result) {
        return StringUtils.formatResults(result);
    }
}
