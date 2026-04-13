package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;

public class Wordle {
    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("log.txt", "UTF-8")) {
            String dictionaryPath = "words_ru.txt";
            WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader();
            List<String> allWords = dictionaryLoader.loadDictionary(dictionaryPath);

            WordleDictionary dictionary = new WordleDictionary(allWords, log);
            List<String> fiveLetterNouns = dictionary.filterFiveLetterNouns(allWords);
            dictionary = new WordleDictionary(fiveLetterNouns, log);

            WordleGame game = new WordleGame(dictionary, log);
            game.startGame();
        } catch (WordleDictionaryException e) {
            System.out.println("Ошибка словаря: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

