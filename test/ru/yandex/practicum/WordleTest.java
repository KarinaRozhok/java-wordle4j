package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WordleTest {
    private WordleGame game;

    @BeforeEach
    public void setUp() {
        List<String> words = new ArrayList<>();
        PrintWriter log = new PrintWriter(System.out);
        WordleDictionary dictionary = new WordleDictionary(words, log);
        game = new WordleGame(dictionary, log);
    }


    @Test
    public void testAnalyzeWordExactMatch() {
        String correctAnswer = "test";
        game.setAnswer(correctAnswer);

        List<String> feedback = game.analyzeWord(correctAnswer);

        for (String mark : feedback) {
            assertEquals("🟢", mark);
        }
    }


    @Test
    public void testAnalyzeWordPartialMatch() {
        String correctAnswer = "hello";
        game.setAnswer(correctAnswer);

        List<String> feedback = game.analyzeWord("hillo");

        assertEquals("🟢", feedback.get(0)); // 'h' на месте
        assertEquals("⚪️", feedback.get(1)); // 'i' нет в ответе
        assertEquals("🟡", feedback.get(2));  // 'l' есть, но не на месте
        assertEquals("🟡", feedback.get(3)); // второй 'l' — аналогично
        assertEquals("🟢", feedback.get(4)); // 'o' на месте
    }

    @Test
    public void testAnalyzeWordNoMatch() {
        String correctAnswer = "world";
        game.setAnswer(correctAnswer);
        List<String> feedback = game.analyzeWord("test"); // теперь List<String>

        for (String mark : feedback) {
            assertEquals("⚪️", mark);
        }
    }
}
