package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a quiz question with a yes/no answer
public class Question implements Writable {
    private String question;
    private String answer;

    // REQUIRES: question has a non-zero length; answer must be "Y" or "N"
    // MODIFIES: this
    // EFFECTS: constructs new quiz question; question is set to given string with "Y" or "N" answer
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    // REQUIRES: ans has a non-zero length
    // EFFECTS: returns true if ans is the same sequence of characters as quiz answer, false otherwise
    public boolean checkAnswer(String ans) {
        return answer.equals(ans);
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject quizQuestion = new JSONObject();
        quizQuestion.put("question", question);
        quizQuestion.put("answer", answer);
        return quizQuestion;
    }
}
