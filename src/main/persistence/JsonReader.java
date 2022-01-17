package persistence;


import model.Question;
import model.Quiz;
import model.QuizList;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads a list of quizzes from stored JSON file data
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: returns source file name
    public String getSource() {
        return source;
    }

    // EFFECTS: reads quiz list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public QuizList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseQuizList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // REQUIRES: jsonObject to have QuizList fields
    // EFFECTS: parses a list of quizzes from jsonObject and returns it
    private QuizList parseQuizList(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        QuizList quizList = new QuizList(name);
        addQuizzesToQuizList(quizList, jsonObject);
        return quizList;
    }

    // MODIFIES: quizList
    // EFFECTS: parses quizzes from JSON object and adds them to quizList
    private void addQuizzesToQuizList(QuizList quizList, JSONObject jsonObject) {
        JSONArray quizzes = jsonObject.getJSONArray("quizzes");
        for (Object quiz : quizzes) {
            JSONObject nextQuiz = (JSONObject) quiz;
            addQuiz(quizList, nextQuiz); // Passing on quizList and JSON quiz
        }
    }

    // MODIFIES: quizList
    // EFFECTS: parses quiz from JSON object and adds it to quizList
    private void addQuiz(QuizList quizList, JSONObject nextQuiz) {
        int id = nextQuiz.getInt("id");
        String title = nextQuiz.getString("title");
        String creator = nextQuiz.getString("creator");  // get values from JSON quiz

        Quiz quiz = new Quiz(title, creator);
        quiz.setId(id);

        JSONArray questions = nextQuiz.getJSONArray("questions"); // array of questions in a JSON quiz
        for (Object question : questions) {
            JSONObject nextQuestion = (JSONObject) question;
            addQuestionToQuiz(quiz, nextQuestion);
        }
        quizList.addQuiz(quiz);
    }

    // MODIFIES: quiz
    // EFFECTS: parses question from JSON object and adds it to quiz
    private void addQuestionToQuiz(Quiz quiz, JSONObject nextQuestion) {
        String question = nextQuestion.getString("question");
        String answer = nextQuestion.getString("answer");
        Question newQuestion = new Question(question, answer);
        quiz.addQuestion(newQuestion);
    }

}
