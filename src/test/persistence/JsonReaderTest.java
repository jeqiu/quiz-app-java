package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNoSuchFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        assertEquals("./data/noSuchFile.json", reader.getSource());
        try {
            QuizList quizList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyQuizList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyQuizList.json");
        try {
            QuizList quizList = reader.read();
            assertEquals("QuizApp", quizList.getName());
            assertEquals(0, quizList.getNumOfQuizzes());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderQuizList() {
        JsonReader reader = new JsonReader("./data/testReaderQuizList.json");
        try {
            QuizList quizList = reader.read();
            assertEquals("QuizApp", quizList.getName());
            List<Quiz> quizzes = quizList.getQuizzes();
            assertEquals(2, quizzes.size());
            checkQuiz(1, "Shakespeare's Plays", "William", quizzes.get(0));
            Question question = quizzes.get(0).getQuestions().get(0);
            checkQuestion("Is Yorick is a character from the play Macbeth?", "N", question);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
