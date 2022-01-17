package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizTest {
    private Quiz quiz1;
    private Quiz quiz2;

    @BeforeEach
    void runBefore() {
        Quiz.setQuizCounter(0);
        quiz1 = new Quiz("Shakespeare", "Jane");
        quiz1.addQuestion("Yorick is a character from the play Macbeth", "N");
        quiz2 = new Quiz("Geography", "John");
    }

    @Test
    void testQuizId() {
        assertEquals(1, quiz1.getId());
        assertEquals(2, quiz2.getId());
        quiz2.setId(5);
        assertEquals(5, quiz2.getId());
    }

    @Test
    void testAddQuestion() {
        assertEquals(0, quiz2.numOfQuestions());
        Question question = new Question("Testing", "Tester");
        quiz2.addQuestion(question);
        assertEquals(1, quiz2.numOfQuestions());
    }

    @Test
    void testQuizTitle() {
        assertEquals("Shakespeare", quiz1.getTitle());
    }

    @Test
    void testQuizCreator() {
        assertEquals("Jane", quiz1.getCreator());
    }

    @Test
    void testQuizQuestions() {
        assertEquals(1, quiz1.numOfQuestions());
        assertEquals(0, quiz2.numOfQuestions());
        ArrayList<Question> questions = quiz1.getQuestions();
        assertEquals(1, questions.size());
    }

    @Test
    void testQuizCounter() {
        Quiz.setQuizCounter(-1);
        assertEquals(0, Quiz.getQuizCounter());
        Quiz newQuiz = new Quiz("Oceanography", "Bruce");
        assertEquals(1, Quiz.getQuizCounter());
        assertEquals(1, newQuiz.getId());
    }
}
