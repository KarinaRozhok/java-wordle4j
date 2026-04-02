package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */

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

