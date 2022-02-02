package org.example.service;

import org.example.util.StringUtils;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Finder {
    private final Indexer indexer;

    public Finder(Indexer indexer) {
        this.indexer = indexer;
    }

    public Map<Phrase, Double> find(String line) {
        return StringUtils.wordStream(line)
                .map(StringUtils::normalize)
                .collect(Collectors.toList())
                .stream()
                .filter(indexer::contains)
                .flatMap(word -> indexer.find(word).stream())
                .collect(Collectors.groupingBy(p -> p, Collectors.collectingAndThen(Collectors.counting(), c -> (double) c)));
    }

}
