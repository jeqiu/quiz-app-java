package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {
    private Question question1;

    @Test
    void testQuestion() {
        question1 = new Question("Is today a weekday?", "Y");
        assertEquals("Is today a weekday?", question1.getQuestion());
        assertEquals("Y", question1.getAnswer());
        assertTrue(question1.checkAnswer("Y"));
        assertFalse(question1.checkAnswer("N"));
    }
}
