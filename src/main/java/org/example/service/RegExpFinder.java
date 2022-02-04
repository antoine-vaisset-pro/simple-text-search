package org.example.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegExpFinder implements IFinder {
    private final Indexer indexer;

    public RegExpFinder(Indexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public Map<Phrase, Double> find(String search) {
        Map<Phrase, Double> result = new HashMap<>();

        indexer.getIndexed().forEach(phrase -> {
            if (Pattern.compile(search).matcher(phrase.content()).matches()) {
                result.put(phrase, 1d);
            }
        });

        return result;
    }
}
