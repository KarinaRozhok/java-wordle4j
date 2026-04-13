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

    private Set<Character> lettersAbsent = new HashSet<>();  // буквы, которых нет в загаданном слове
    private Set<Character> lettersPresent = new HashSet<>(); // буквы, которые есть в загаданном слове, но не на своих местах
    private int correctPositionsCount = 0; // Вместо List<Character>


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


        if (!dictionary.contains(userWord)) {
            throw new WordleInputException("Слово '" + userWord + "' отсутствует в словаре!");
        } else if (userWord.length() != normalizedAnswer.length()) {
            throw new WordleInputException("Ошибка: длина вашего слова не совпадает с длиной загаданного слова.");
        }

        List<String> feedback = new ArrayList<>(Collections.nCopies(userWord.length(), "⚪️")); // Инициализируем один раз
        Map<Character, Integer> remainingLetters = new HashMap<>();

        for (char c : normalizedAnswer.toCharArray()) {
            remainingLetters.put(c, remainingLetters.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> availableLetters = new HashMap<>(remainingLetters);

        for (int i = 0; i < userWord.length(); i++) {
            char userChar = userWord.charAt(i);
            if ("🟢".equals(feedback.get(i))) continue; // Пропускаем уже угаданные

            if (availableLetters.containsKey(userChar) && availableLetters.get(userChar) > 0) {
                feedback.set(i, "🟡");
                availableLetters.put(userChar, availableLetters.get(userChar) - 1);
                lettersPresent.add(userChar);
            } else {
                feedback.set(i, "⚪️");
                lettersAbsent.add(userChar);
            }
        }

        for (int i = 0; i < userWord.length(); i++) {
            char userChar = userWord.charAt(i);
            char answerChar = normalizedAnswer.charAt(i);

            if (userChar == answerChar) {
                feedback.set(i, "🟢");
                remainingLetters.put(userChar, remainingLetters.get(userChar) - 1);
                correctPositionsCount++;
            }
        }

        for (int i = 0; i < userWord.length(); i++) {
            char userChar = userWord.charAt(i);
            if ("🟢".equals(feedback.get(i))) continue; // Пропускаем уже угаданные

            if (remainingLetters.containsKey(userChar) && remainingLetters.get(userChar) > 0) {
                feedback.set(i, "🟡");
                remainingLetters.put(userChar, remainingLetters.get(userChar) - 1);
                lettersPresent.add(userChar);
            } else {
                feedback.set(i, "⚪️");
                lettersAbsent.add(userChar);
            }
        }
        return feedback;
    }
    public void printHints() {
        System.out.println("Подсказки:");

        if (!lettersAbsent.isEmpty()) {
            System.out.print("Буквы, которых нет в загаданном слове: ");
            for (char letter : lettersAbsent) {
                System.out.print(letter + " ");
            }
            System.out.println();
        } else {
            System.out.println("Все введенные вами буквы есть в загаданном слове.");
        }

        int incorrectPositionsCount = lettersPresent.size();
        if (incorrectPositionsCount > 0) {
            System.out.printf("Количество букв, которые есть в слове, но на неверных позициях: %d%n", incorrectPositionsCount);
        } else {
            System.out.println("Нет букв, которые были бы в слове, но стояли бы на неверных позициях.");
        }

        if (correctPositionsCount > 0) {
            System.out.printf("Количество букв на правильных позициях: %d%n", correctPositionsCount);
        } else {
            System.out.println("Пока нет букв на правильных позициях.");
        }

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
        correctPositionsCount = 0;

        log.println("Игра начата");
        answer = selectRandomWordFromDictionary();
        log.printf("Загадано слово: '%s'%n", answer);

        lettersAbsent.clear();
        lettersPresent.clear();

        int attemptsLeft = MAX_ATTEMPTS;

        while (attemptsLeft > 0) {
            String userWord = getUserInput();
            log.printf("Ход игрока: '%s' (осталось попыток: %d)%n", userWord, attemptsLeft);
            List<String> feedback = analyzeWord(userWord);
            log.println("Результат анализа: " + feedback);
            printFeedback(feedback);

            printHints();

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
