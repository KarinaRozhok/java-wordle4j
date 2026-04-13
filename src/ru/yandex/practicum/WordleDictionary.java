package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class WordleDictionary {
    public static final int WORD_LENGTH = 5;
    private List<String> words;
    private PrintWriter log;

    public boolean contains(String word) {
        return words.contains(word);
    }

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = new ArrayList<>();
        for (String word : words) {
            this.words.add(normalizeWord(word));
        }
        this.log = log;
    }

    private String normalizeWord(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> filterFiveLetterNouns(List<String> allWords) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : allWords) {
            if (word.length() == WORD_LENGTH) {

                filteredWords.add(word);
            }
        }
        return filteredWords;
    }
}
