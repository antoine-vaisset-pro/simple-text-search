package org.example.service;

import java.util.HashMap;
import java.util.Map;

public class SimpleFinder implements IFinder {
    private final Indexer indexer;

    public SimpleFinder(Indexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public Map<Phrase, Double> find(String search) {
        Map<Phrase, Double> result = new HashMap<>();

        indexer.getIndexed().forEach(phrase -> {
            int patternSize = search.length();
            int textSize = phrase.content().length();

            int i = 0;

            while ((i + patternSize) <= textSize) {
                int j = 0;
                while (phrase.content().charAt(i + j) == search.charAt(j)) {
                    j += 1;
                    if (j >= patternSize) {
                        result.put(phrase, 1d);
                        return;
                    }
                }
                i += 1;
            }
        });

        return result;
    }
}
