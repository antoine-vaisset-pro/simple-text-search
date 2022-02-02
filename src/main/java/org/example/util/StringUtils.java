package org.example.util;

import org.example.service.Phrase;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;

public interface StringUtils {

    String WORD_REGEXP = "\\W+";
    String PHRASE_REGEX = "[.!?]";
    int MAX_SIZE = 100;

    static Stream<String> phraseStream(String text) {
        return Arrays.stream(text.split(PHRASE_REGEX));
    }

    static Stream<String> wordStream(String line) {
        return Arrays.stream(line.split(WORD_REGEXP));
    }

    static String normalize(String string) {
        return string.toLowerCase();
    }

    static String formatResults(Map<Phrase, Double> results) {
        return results.entrySet().stream()
                .filter(e -> e.getValue() != 0d)
                .limit(MAX_SIZE)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(e -> format("Score %5.2f - Line %3d - Content : %s", e.getValue(), e.getKey().position(), e.getKey().content().replace("\n", " ")))
                .reduce((s1, s2) -> String.join("\n", s1, s2))
                .orElse("no matches found");
    }

}