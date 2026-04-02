package ru.yandex.practicum;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;
    private String answer;
    private WordleDictionary dictionary;
    private final PrintWriter log;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> analyzeWord(String userWord) {
        userWord = normalizeWord(userWord);
        String normalizedAnswer = normalizeWord(answer);

        if (userWord.length() != normalizedAnswer.length()) {
            System.out.println("Ошибка: длина вашего слова не совпадает с длиной загаданного слова.");
            System.out.printf("Подсказка: загаданное слово состоит из %d букв.%n", normalizedAnswer.length());
            return new ArrayList<>();
        }

        List<String> feedback = new ArrayList<>();
        for (int i = 0; i < userWord.length(); i++) {
            feedback.add("⚪️");
        }

        Map<Character, Integer> answerLetterCount = new HashMap<>();
        for (char c : normalizedAnswer.toCharArray()) {
            answerLetterCount.put(c, answerLetterCount.getOrDefault(c, 0) + 1);
        }

        System.out.println("answerLetterCount: " + answerLetterCount); 

        Map<Character, Integer> totalAnswerCount = new HashMap<>(answerLetterCount);
        System.out.println("totalAnswerCount: " + totalAnswerCount);

        for (int i = 0; i < userWord.length(); i++) {
            char userChar = userWord.charAt(i);
            char answerChar = normalizedAnswer.charAt(i);

            if (userChar == answerChar) {
                feedback.set(i, "🟢");
                totalAnswerCount.put(userChar, totalAnswerCount.get(userChar) - 1);
                System.out.printf("Позиция %d: '%c' == '%c', ставим '🟢', обновляем totalAnswerCount: %s%n", i, userChar, answerChar, totalAnswerCount);
            }
        }

        Map<Character, Integer> remainingLetters = new HashMap<>(totalAnswerCount);
        System.out.println("remainingLetters (после точных совпадений): " + remainingLetters);

        for (int i = 0; i < userWord.length(); i++) {
            if ("🟢".equals(feedback.get(i))) continue;

            char userChar = userWord.charAt(i);

            System.out.printf("Проверяем позицию %d, буква '%c'%n", i, userChar);

            if (remainingLetters.containsKey(userChar) && remainingLetters.get(userChar) > 0) {
                feedback.set(i, "🟡");
                remainingLetters.put(userChar, remainingLetters.get(userChar) - 1);
                System.out.printf("  → Буква найдена в оставшихся, ставим '🟡', обновляем remainingLetters: %s%n", remainingLetters);
            } else {
                feedback.set(i, "⚪️");
                System.out.printf("  → Буквы '%c' нет в оставшихся, ставим '⚪️'%n", userChar);
            }
        }

        System.out.println("Итоговый feedback: " + feedback);
        return feedback;
    }


    public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ваше слово:");
        return scanner.nextLine();
    }

    public void printFeedback(List<String> feedback) {
        System.out.println("Результат проверки:");
        for (String mark : feedback) {
            System.out.print(mark + " ");
        }
        System.out.println();
    }

    public boolean isWordGuessed(String userWord) {
        return normalizeWord(userWord).equals(normalizeWord(answer));
    }


    public void startGame() {
        log.println("Игра начата");
        answer = selectRandomWordFromDictionary();
        log.printf("Загадано слово: '%s'%n", answer);

        int attemptsLeft = MAX_ATTEMPTS;
        while (attemptsLeft > 0) {
            String userWord = getUserInput();
            log.printf("Ход игрока: '%s' (осталось попыток: %d)%n", userWord, attemptsLeft);
            List<String> feedback = analyzeWord(userWord);
            log.println("Результат анализа: " + feedback);
            printFeedback(feedback);

            if (isWordGuessed(userWord)) {
                System.out.println("Поздравляем, вы угадали слово!");
                break;
            }

            attemptsLeft--;
        }

        if (attemptsLeft == 0) {
            System.out.printf("К сожалению, вы не угадали слово. Правильное слово было '%s'.%n", answer);
        }
    }


    private String normalizeWord(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }

    public String selectRandomWordFromDictionary() {
        List<String> fiveLetterNouns = dictionary.getWords();

        if (fiveLetterNouns.isEmpty()) {
            throw new RuntimeException("Словарь не содержит подходящих 5-буквенных существительных!");
        }

        Random random = new Random();
        int wordIndex = random.nextInt(fiveLetterNouns.size());
        return fiveLetterNouns.get(wordIndex);
    }
}
