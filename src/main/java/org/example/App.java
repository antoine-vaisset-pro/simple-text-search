package org.example;

import org.example.controller.ConsoleReader;
import org.example.service.Finder;
import org.example.service.Indexer;

public class App {

    private static Indexer indexer = new Indexer();
    private static Finder finder = new Finder(indexer);

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No directory given to index.");
        }

        indexer.index(args[0]);
        new ConsoleReader(finder).start();
    }
}