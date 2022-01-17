package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizListTest {
    private QuizList quizList;

    @BeforeEach
    void runBefore() {
        quizList = new QuizList("QuizApp");
    }

    @Test
    void testQuizName() {
        assertEquals("QuizApp", quizList.getName());
    }

    @Test
    void testNumOfQuizzes() {
        assertEquals(0, quizList.getNumOfQuizzes());
        quizList.addQuiz(new Quiz("Countries", "George"));
        assertEquals(1, quizList.getNumOfQuizzes());
    }

    @Test
    void testAddQuiz() {
        quizList.addQuiz(new Quiz("Countries", "George"));
        Quiz quiz1 = quizList.getQuiz(1);
        assertEquals("Countries", quiz1.getTitle());
        assertEquals("George", quiz1.getCreator());
    }

    @Test
    void testRemoveQuiz() {
        quizList.addQuiz(new Quiz("Countries", "George"));
        quizList.addQuiz(new Quiz("Food", "Food Critic"));
        assertEquals(2, quizList.getNumOfQuizzes());
        quizList.removeQuiz(0);
        assertEquals(1, quizList.getNumOfQuizzes());
    }

    @Test
    void testGetQuizAndQuizList() {
        assertEquals(0, quizList.getQuizzes().size());
        quizList.addQuiz(new Quiz("Weather", "Jim"));
        assertEquals(1, quizList.getQuizzes().size());
        Quiz quiz1 = quizList.getQuiz(1);
        assertEquals("Weather", quiz1.getTitle());
        assertEquals("Jim", quiz1.getCreator());
    }

}