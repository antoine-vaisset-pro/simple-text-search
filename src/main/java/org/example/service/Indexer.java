package org.example.service;

import org.example.util.StringUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

public class Indexer {
    private final Map<String, List<Phrase>> index = new HashMap<>();
    private final Set<Phrase> indexed = new HashSet<>();

    public void index(String filePath) {
        try {
            String fileContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            System.out.printf("Indexing file '%s'%n", filePath);
            var start = Instant.now();
            AtomicInteger phraseNumber = new AtomicInteger();
            StringUtils.phraseStream(fileContent).forEach(phrase -> {
                indexPhrase(new Phrase(phraseNumber.getAndIncrement(), phrase));
            });
            var finish = Instant.now();
            System.out.printf("%d phrases indexed from file '%s' in %d ms%n", indexed.size(), filePath, Duration.between(start, finish).toMillis());
        } catch (IOException e) {
            System.err.println(format("Unable to read file '%s'", filePath));
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void indexPhrase(Phrase phrase) {
        StringUtils.wordStream(phrase.content())
                .map(StringUtils::normalize)
                .forEach(word -> {
                    if (!index.containsKey(word)) {
                        index.put(word, new ArrayList<>());
                    }
                    index.get(word).add(phrase);
                });
        indexed.add(phrase);
    }

    public List<Phrase> find(String word) {
        return index.get(word);
    }

    public boolean contains(String word) {
        return index.containsKey(word);
    }

    public Set<Phrase> getIndexed() {
        return indexed;
    }
}
