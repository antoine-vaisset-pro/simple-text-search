package org.example.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Finder {
    private final Indexer indexer;
    private final Map<Integer, IFinder> finders = new HashMap<>();

    public Finder(Indexer indexer) {
        this.indexer = indexer;
        this.finders.put(0, new SimpleFinder(indexer));
        this.finders.put(1, new RegExpFinder(indexer));
        this.finders.put(2, new IndexedFinder(indexer));
        this.finders.put(3, new SimpleLevenshteinFinder(indexer));
    }

    public Map<Phrase, Double> find(String search) {
        int splitPosition = search.indexOf('|');
        int mode = 0;
        if (splitPosition != -1) {
            mode = Integer.parseInt(search.split("\\|")[0].trim());
            search = search.substring(splitPosition + 1).trim();
        }

        var start = Instant.now();
        Map<Phrase, Double> result = finders.get(mode).find(search);
        var stop = Instant.now();

        System.out.printf("Search done in %d ms%n", Duration.between(start, stop).toMillis());
        return result;
    }

}
