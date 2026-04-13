package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    public List<String> loadDictionary(String path) throws WordleDictionaryException {
        List<String> words = new ArrayList<>();
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            throw new WordleDictionaryException("Файл словаря не найден: " + path);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                line = line.toLowerCase().replace('ё', 'е');
                words.add(line);
            }
        } catch (IOException e) {
            throw new WordleDictionaryException("Ошибка при загрузке словаря: " + e.getMessage());
        }

        if (words.isEmpty()) {
            throw new WordleDictionaryException("Словарь пуст или не найден!");
        }
        return words;
    }
}