package org.example.service;

import org.example.util.StringUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Finder {
    private final Indexer indexer;

    public Finder(Indexer indexer) {
        this.indexer = indexer;
    }

    public Map<Phrase, Double> find(String search) {
        int splitPosition = search.indexOf('|');
        int mode = 0;
        if(splitPosition != -1){
            mode = Integer.parseInt(search.split("\\|")[0].trim());
            search = search.substring(splitPosition+1).trim();
        }

        var start = Instant.now();
        Map<Phrase, Double> result = switch (mode) {
            case 0 -> simpleFind(search);
            case 1 -> regexFind(search);
            default -> indexedFind(search);
        };
        var stop = Instant.now();

        System.out.printf("Search done in %d ms%n", Duration.between(start, stop).toMillis());
        return result;
    }

    public Map<Phrase, Double> simpleFind(String search) {
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

    public Map<Phrase, Double> regexFind(String search) {
        Map<Phrase, Double> result = new HashMap<>();

        indexer.getIndexed().forEach(phrase -> {
            if(Pattern.compile(search).matcher(phrase.content()).matches()){
                result.put(phrase, 1d);
            }
        });

        return result;
    }

    public Map<Phrase, Double> indexedFind(String search) {
        return StringUtils.wordStream(search)
                .map(StringUtils::normalize)
                .collect(Collectors.toList())
                .stream()
                .filter(indexer::contains)
                .flatMap(word -> indexer.find(word).stream())
                .collect(Collectors.groupingBy(p -> p, Collectors.collectingAndThen(Collectors.counting(), c -> (double) c)));
    }

}
