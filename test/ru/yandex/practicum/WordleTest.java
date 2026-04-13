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
        words.add("test");
        words.add("hello");
        words.add("world");
        PrintWriter log = new PrintWriter(System.out);
        WordleDictionary dictionary = new WordleDictionary(words, log);
        game = new WordleGame(dictionary, log);
    }

    @Test
    public void testRepeatedLetters() {
        game.setAnswer("мама");
        List<String> feedback = game.analyzeWord("рама");
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
        String correctAnswer = "робот";
        game.setAnswer(correctAnswer);

        List<String> feedback = game.analyzeWord("ерпнт");

        assertEquals(correctAnswer.length(), feedback.size());

        String[] expected = {"⚪️", "🟡", "⚪️", "⚪️", "🟢"};

        for (int i = 0; i < feedback.size(); i++) {
            assertEquals(expected[i], feedback.get(i), "Позиция " + i + " не совпала");
        }
    }

    @Test
    public void testAnalyzeWordNoMatch() {
        String correctAnswer = "world";
        game.setAnswer(correctAnswer);
        List<String> feedback = game.analyzeWord("tests");

        for (String mark : feedback) {
            assertEquals("⚪️", mark);
        }
    }
}
