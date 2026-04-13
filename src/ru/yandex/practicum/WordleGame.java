package ru.yandex.practicum;
import java.util.*;
import java.util.ArrayList;
import java.io.PrintWriter;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;
    private String answer;
    private WordleDictionary dictionary;
    private final PrintWriter log;
    private static final String RUSSIAN_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private Set<Character> lettersAbsent = new HashSet<>();
    private Set<Character> lettersPresent = new HashSet<>();
    private int correctPositionsCount = 0;


    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> analyzeWord(String guess) {
        List<String> feedback = new ArrayList<>();
        printHints();
        String answer = this.answer;
        correctPositionsCount = 0;

        if (guess.length() != answer.length()) {
            throw new IllegalArgumentException("Длина предполагаемого слова должна совпадать с длиной загаданного слова");
        }

        int[] answerCharCount = new int[33];
        for (char c : answer.toCharArray()) {
            int index = RUSSIAN_ALPHABET.indexOf(c);
            if (index >= 0) {
                answerCharCount[index]++;
            }
        }

        for (int i = 0; i < answerCharCount.length; i++) {
            if (answerCharCount[i] > 0) {
                char c = RUSSIAN_ALPHABET.charAt(i);
                System.out.println("  '" + c + "': " + answerCharCount[i]);
            }
        }

        boolean[] usedPositions = new boolean[answer.length()];

        for (int i = 0; i < answer.length(); i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);

            if (guessChar == answerChar) {
                feedback.add("🟢");
                usedPositions[i] = true;
                int charIndex = RUSSIAN_ALPHABET.indexOf(guessChar);
                if (charIndex >= 0) answerCharCount[charIndex]--;
                correctPositionsCount++;
            } else {
                feedback.add(null);
            }
        }

        for (int i = 0; i < guess.length(); i++) {
            if (feedback.get(i) == null) {
                char guessChar = guess.charAt(i);
                int charIndex = RUSSIAN_ALPHABET.indexOf(guessChar);

                if (charIndex >= 0 && answerCharCount[charIndex] > 0) {
                    feedback.set(i, "🟡");
                    answerCharCount[charIndex]--;
                } else {
                    feedback.set(i, "⚪️");
                }
            }
        }

        for (int i = 0; i < feedback.size(); i++) {
            String marker = feedback.get(i);
            char guessedChar = guess.charAt(i);

            switch (marker) {
                case "⚪️":
                    lettersAbsent.add(guessedChar);
                    break;
                case "🟡":
                    lettersPresent.add(guessedChar);
                    break;
                case "🟢":
                    break;
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
        String input;
        do {
            System.out.println("Введите ваше слово:");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Слово не может быть пустым! Попробуйте ещё раз.");
            }
        } while (input.isEmpty());
        return input;
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
