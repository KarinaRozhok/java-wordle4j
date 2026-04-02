package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class WordleDictionary {
    private List<String> words;
    private PrintWriter log;

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = words;
        this.log = log;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> filterFiveLetterNouns(List<String> allWords) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : allWords) {
            if (word.length() == 5) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }
}
