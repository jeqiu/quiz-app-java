package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a quiz having an id, title, creator, and a list of questions
public class Quiz implements Writable {
    private static int quizCounter = 0;
    private int id;
    private String title;
    private String creator;
    private ArrayList<Question> questions;

    // REQUIRES: title and creator have non-zero length
    // MODIFIES: this
    // EFFECTS: initializes quiz with positive integer id, title and creator name, and an empty list of questions
    public Quiz(String title, String creator) {
        id = ++quizCounter;
        this.title = title;
        this.creator = creator;
        questions = new ArrayList<Question>();
    }

    // EFFECTS: returns value of static field quizCounter
    public static int getQuizCounter() {
        return quizCounter;
    }

    // MODIFIES: this
    // EFFECTS: sets quizCounter to num if num >= 0; else sets quizCounter to 0
    public static void setQuizCounter(int num) {
        if (num < 0) {
            quizCounter = 0;
        } else {
            quizCounter = num;
        }
    }

    // EFFECTS: returns id number of quiz
    public int getId() {
        return id;
    }

    // REQUIRES: positive integer id
    // MODIFIES: this
    // EFFECTS: sets id number of quiz
    public void setId(int id) {
        this.id = id;
    }

    // EFFECTS: returns title of the quiz
    public String getTitle() {
        return title;
    }

    // EFFECTS: returns the quiz creator name
    public String getCreator() {
        return creator;
    }

    // EFFECTS: returns number of questions currently in the quiz
    public int numOfQuestions() {
        return questions.size();
    }

    // EFFECTS: returns list of questions in the quiz
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    // REQUIRES: question has a non-zero length; answer must be "Y" or "N"
    // MODIFIES: this
    // EFFECTS: adds a new question to the list of questions in the quiz
    public void addQuestion(String question, String ans) {
        Question newQuestion = new Question(question, ans);
        questions.add(newQuestion);
        EventLog.getInstance().logEvent(new Event("New question added to quiz."));
    }

    // MODIFIES: this
    // EFFECTS: adds a new question to the list of questions in the quiz
    public void addQuestion(Question question) {
        questions.add(question);
        EventLog.getInstance().logEvent(new Event("New question added to quiz."));
    }

    // EFFECTS: returns this as JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject quiz = new JSONObject();
        quiz.put("id", id);
        quiz.put("title", title);
        quiz.put("creator", creator);
        quiz.put("questions", questionsToJson());
        return quiz;
    }

    // EFFECTS: returns questions in this quiz as a JSON array of JSON objects
    private JSONArray questionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Question question : questions) {
            jsonArray.put(question.toJson());
        }
        return jsonArray;
    }

}
