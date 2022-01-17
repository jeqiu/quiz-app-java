package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


// This JsonTest class checks the Question and Quiz models
public class JsonTest {
    void checkQuestion(String question, String answer, Question q) {
        assertEquals(question, q.getQuestion());
        assertEquals(answer, q.getAnswer());
    }

    void checkQuiz(int id, String title, String creator, Quiz quiz) {
        assertEquals(title, quiz.getTitle());
        assertEquals(creator, quiz.getCreator());
        assertEquals(id, quiz.getId());
    }
}
