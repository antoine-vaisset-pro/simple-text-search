package org.example.service;

import org.example.util.StringUtils;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleLevenshteinFinder implements IFinder {
    private final Indexer indexer;

    public SimpleLevenshteinFinder(Indexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public Map<Phrase, Double> find(String search) {
        return StringUtils.wordStream(search)
                .map(StringUtils::normalize)
                .flatMap(this::scoreByPhrase)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
    }

    private Stream<Map.Entry<Phrase, Double>> scoreByPhrase(String search) {
        return indexer.getIndex().entrySet().stream()
                .flatMap(entry -> {
                    int distance = calculateLevenshteinDistance(entry.getKey(), search, search.length() / 3);
                    double score = distance >= 0 ? 1.0 / (distance + 1) : 0.0;
                    return entry.getValue().stream().map(phrase -> new AbstractMap.SimpleEntry<>(phrase, score));
                });
    }

    private int calculateLevenshteinDistance(String x, String y, int maxDistance) {
        if (x.equals(y)) {
            return 0;
        }
        if (Math.abs(x.length() - y.length()) > maxDistance) {
            return -1;
        }

        return calculate(x, y);
    }

    private int calculate(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        int substitution = calculate(x.substring(1), y.substring(1))
                + costOfSubstitution(x.charAt(0), y.charAt(0));
        int insertion = calculate(x, y.substring(1)) + 1;
        int deletion = calculate(x.substring(1), y) + 1;

        return min(substitution, insertion, deletion);
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

}
