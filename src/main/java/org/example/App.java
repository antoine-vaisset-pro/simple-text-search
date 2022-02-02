package org.example;

import org.example.controller.ConsoleReader;
import org.example.service.Finder;
import org.example.service.Indexer;

public class App {

    private static Indexer indexer = new Indexer();

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No file path given to index.");
        }
        Finder finder = new Finder(indexer);

        indexer.index(args[0]);
        new ConsoleReader(finder).start();
    }
}