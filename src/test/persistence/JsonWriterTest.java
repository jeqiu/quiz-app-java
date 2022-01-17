package persistence;

import model.Question;
import model.Quiz;
import model.QuizList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


// To test JsonWriter:
// write data to a file, then use reader to check it is a copy of what was written
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            QuizList quizList = new QuizList("My quiz list");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyQuizList() {
        try {
            QuizList quizList = new QuizList("My quiz list");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyQuizList.json");
            writer.open();
            writer.write(quizList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyQuizList.json");
            quizList = reader.read();
            assertEquals("My quiz list", quizList.getName());
            assertEquals(0, quizList.getNumOfQuizzes());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterQuizList() {
        try {
            Quiz.setQuizCounter(0);
            QuizList quizList = new QuizList("My quiz list");
            Quiz quiz1 = new Quiz("Shakespeare's Plays", "William");
            quiz1.addQuestion("Is Yorick is a character from the play Macbeth?", "N");
            quiz1.addQuestion("Hamlet is a comedy with a happy ending.", "N");
            Quiz quiz2 = new Quiz("Canadian Trivia", "Tom & Jerry");
            quiz2.addQuestion("Canada has 11 provinces and territories.", "N");
            quiz2.addQuestion("UBC is Canada's oldest university.", "N");
            quizList.addQuiz(quiz1);
            quizList.addQuiz(quiz2);

            JsonWriter writer = new JsonWriter("./data/testWriterQuizList.json");
            writer.open();
            writer.write(quizList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterQuizList.json");
            quizList = reader.read();
            assertEquals("My quiz list", quizList.getName());
            List<Quiz> quizzes = quizList.getQuizzes();
            assertEquals(2, quizzes.size());
            checkQuiz(1, "Shakespeare's Plays", "William", quizzes.get(0));
            Question question = quizzes.get(0).getQuestions().get(0);
            checkQuestion("Is Yorick is a character from the play Macbeth?", "N", question);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}