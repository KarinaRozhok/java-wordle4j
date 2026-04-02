package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {
    public List<String> loadDictionary(String filePath) throws Exception {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {

                line = line.toLowerCase().replace('ё', 'е');
                words.add(line);
            }
        }
        return words;
    }
}
