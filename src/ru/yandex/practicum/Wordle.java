package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;

public class Wordle {
    public static void main(String[] args) throws Exception {
        try (PrintWriter log = new PrintWriter("log.txt")) {
            String dictionaryPath = "words_ru.txt";
            WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader();
            List<String> allWords = dictionaryLoader.loadDictionary(dictionaryPath);

            WordleDictionary dictionary = new WordleDictionary(allWords, log);
            List<String> fiveLetterNouns = dictionary.filterFiveLetterNouns(allWords);
            dictionary = new WordleDictionary(fiveLetterNouns, log);

            WordleGame game = new WordleGame(dictionary, log);
            game.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

