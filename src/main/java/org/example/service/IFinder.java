package org.example.service;

import java.util.Map;

public interface IFinder {
    Map<Phrase, Double> find(String search);
}