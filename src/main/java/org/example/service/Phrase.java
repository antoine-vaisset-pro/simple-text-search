package org.example.service;

import java.util.Objects;

public record Phrase(Integer position, String content) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return Objects.equals(position, phrase.position) && Objects.equals(content, phrase.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, content);
    }
}
