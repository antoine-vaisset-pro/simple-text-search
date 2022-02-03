package org.example.util;

import org.example.service.Phrase;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.format;

public interface StringUtils {

    Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    String WORD_REGEXP = "[.!;:'’\"»«\\s]+";
    String PHRASE_REGEX = "[.!?\n]";
    int MAX_SIZE = 10;

    static Stream<String> phraseStream(String text) {
        return Arrays.stream(text.split(PHRASE_REGEX));
    }

    static Stream<String> wordStream(String line) {
        return Arrays.stream(line.split(WORD_REGEXP)).map(StringUtils::normalize);
    }

    static String normalize(String input) {
        StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
        return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll("").toLowerCase();
    }

    static String formatResults(Map<Phrase, Double> results) {
        return
                String.format("%d phrases trouvées.\n", results.size()) +
                results.entrySet().stream()
                .filter(e -> e.getValue() != 0d)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(MAX_SIZE)
                .map(e -> format("Score %5.2f - Line %3d - Content : %s", e.getValue(), e.getKey().position(), e.getKey().content().replace("\n", " ")))
                .reduce((s1, s2) -> String.join("\n", s1, s2))
                .orElse("no matches found");
    }

}