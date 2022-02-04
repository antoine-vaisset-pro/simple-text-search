package org.example.service;

import org.example.util.StringUtils;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexedFinder implements IFinder {
    private final Indexer indexer;

    public IndexedFinder(Indexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public Map<Phrase, Double> find(String search) {
        return StringUtils.wordStream(search)
                .map(StringUtils::normalize)
                .collect(Collectors.toList())
                .stream()
                .filter(indexer::contains)
                .flatMap(word -> indexer.find(word).stream())
                .collect(Collectors.groupingBy(p -> p, Collectors.collectingAndThen(Collectors.counting(), c -> (double) c)));
    }
}
