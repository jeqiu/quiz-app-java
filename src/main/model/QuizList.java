package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a list of quizzes
public class QuizList implements Writable {
    private String name;
    private ArrayList<Quiz> quizzes;

    // MODIFIES: this
    // EFFECTS: initializes an empty list of quizzes with given name
    public QuizList(String name) {
        this.name = name;
        quizzes = new ArrayList<Quiz>();
    }

    // EFFECTS: returns name of list of quizzes
    public String getName() {
        return name;
    }

    // EFFECTS: returns number of quizzes
    public int getNumOfQuizzes() {
        return quizzes.size();
    }

    // EFFECTS: returns the list of quizzes
    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    // REQUIRES: id number of an existing quiz
    // EFFECTS: returns quiz with given id
    public Quiz getQuiz(int id) {
        return quizzes.get((id - 1));
    }

    // MODIFIES: this
    // EFFECTS: adds a new quiz to the list
    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        EventLog.getInstance().logEvent(new Event("Quiz added to list of quizzes."));
    }

    // REQUIRES: index >= 0
    // MODIFIES: this
    // EFFECTS: removes quiz with given index from the list
    public void removeQuiz(int index) {
        quizzes.remove(index);
        EventLog.getInstance().logEvent(new Event("Quiz removed."));
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("quizzes", quizzesToJson());
        json.put("name", name);
        return json;
    }

    // EFFECTS: returns quizzes as a JSON array of JSON objects
    private JSONArray quizzesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Quiz quiz : quizzes) {
            jsonArray.put(quiz.toJson());
        }
        return jsonArray;
    }

}
