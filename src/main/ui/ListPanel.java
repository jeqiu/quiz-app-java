package ui;

import model.Question;
import model.Quiz;
import model.QuizList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Represents main panel of the quiz app GUI; contains list of quizzes and buttons for interaction
public class ListPanel extends JPanel implements ListSelectionListener {
    public static final String JSON_STORE = "./data/quizzes.json";
    public static final String JSON_DATABASE = "./data/database.json";
    protected QuizList quizList;
    protected JsonWriter jsonWriter;
    protected JsonReader jsonReader;
    private JList list;
    private DefaultListModel listModel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton selectButton;
    private JButton loadButton;
    private JButton saveButton;

    // MODIFIES: this
    // EFFECTS: constructs a panel that displays quizzes and buttons the user can interact with
    public ListPanel() {
        super(new BorderLayout());

        quizList = new QuizList("QuizApp");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        JsonReader databaseReader = new JsonReader(JSON_DATABASE);
        loadQuizList(databaseReader);

        createDisplay();
    }

    // MODIFIES: this
    // EFFECTS: initializes display of list of quizzes and buttons
    private void createDisplay() {
        listModel = new DefaultListModel();
        displayQuizzes();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane, BorderLayout.CENTER);

        createButtons();
        addButtons();
    }

    // MODIFIES: this
    // EFFECTS: adds quizzes to the display
    private void displayQuizzes() {
        ArrayList<Quiz> quizzes = quizList.getQuizzes();
        int quizNum = 1;
        for (Quiz q: quizzes) {
            String title = q.getTitle();
            listModel.addElement("\tQuiz " + quizNum + ": " + title);
            quizNum++;
        }
    }

    // EFFECTS: helper function for creating add, delete, select, load and save buttons for quizzes
    private void createButtons() {
        createSelectButton("Select Quiz");
        createAddButton("Create New Quiz");
        createDeleteButton("Delete Quiz");
        createLoadButton("Load");
        createSaveButton("Save");
    }

    // REQUIRES: non-zero length for button label
    // MODIFIES: this
    // EFFECTS: creates button for selecting a quiz and adds action listener to button
    private void createSelectButton(String label) {
        selectButton = new JButton(label);
        SelectListener selectListener = new SelectListener();
        selectButton.addActionListener(selectListener);
        selectButton.setEnabled(true);
    }

    // REQUIRES: non-zero length for button label
    // MODIFIES: this
    // EFFECTS: creates button for adding a new quiz and adds action listener to button
    private void createAddButton(String label) {
        addButton = new JButton(label);
        AddListener addListener = new AddListener();
        addButton.addActionListener(addListener);
        addButton.setEnabled(true);
    }

    // REQUIRES: non-zero length for button label
    // MODIFIES: this
    // EFFECTS: creates button for deleting a quiz and adds action listener to button
    private void createDeleteButton(String label) {
        deleteButton = new JButton(label);
        DeleteListener deleteListener = new DeleteListener();
        deleteButton.addActionListener(deleteListener);
        deleteButton.setEnabled(true);
    }

    // REQUIRES: non-zero length for button label
    // MODIFIES: this
    // EFFECTS: creates button for loading quizzes and adds action listener to button
    private void createLoadButton(String label) {
        loadButton = new JButton(label);
        LoadListener loadListener = new LoadListener();
        loadButton.addActionListener(loadListener);
        loadButton.setEnabled(true);
    }

    // REQUIRES: non-zero length for button label
    // MODIFIES: this
    // EFFECTS: creates button for saving quizzes and adds action listener to button
    private void createSaveButton(String label) {
        saveButton = new JButton(label);
        SaveListener saveListener = new SaveListener();
        saveButton.addActionListener(saveListener);
        saveButton.setEnabled(true);
    }

    // MODIFIES: this
    // EFFECTS: adds the buttons for interaction with quizzes to the bottom of display
    private void addButtons() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(addButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(deleteButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(selectButton);
        buttonPane.add(Box.createHorizontalStrut(25));
        buttonPane.add(loadButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(saveButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS: select quiz and delete quiz buttons can be clicked only if a quiz is selected
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
                //No selection, disable quiz selection buttons
                selectButton.setEnabled(false);
                deleteButton.setEnabled(false);

            } else {
                //Selection, enable quiz selection buttons
                selectButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        }
    }

    // Represents the action to be taken when the user selects a quiz
    private class SelectListener implements ActionListener {

        // MODIFIES: ListPanel.this
        // EFFECTS: responds to button click by displaying options for selected quiz;
        //          allows user to play quiz or add question
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            int quizNum = ++index;
            Quiz selectedQuiz = quizList.getQuiz(quizNum);
            String title = selectedQuiz.getTitle();
            String creator = selectedQuiz.getCreator();
            int numOfQuestions = selectedQuiz.numOfQuestions();
            Object[] options = { "Play Quiz", "Add Question" };
            int n = JOptionPane.showOptionDialog(null,
                    "This quiz was created by " + creator + " and has " + numOfQuestions + " questions.",
                    "Quiz " + quizNum + ": " + title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (n == 0) {  // Play quiz selected
                playQuiz(selectedQuiz);
            } else if (n == 1) { // Add question selected
                addQuestion(selectedQuiz);
            }
        }
    }

    // Represents the action to be taken when the user wants to add a new quiz
    private class AddListener implements ActionListener {

        // MODIFIES: ListPanel.this
        // EFFECTS: responds to button click by taking user input and creating a new quiz
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog("What is the title of your new quiz?");
            if (title == null) {
                return;
            }
            String creator = JOptionPane.showInputDialog("Who is creating this quiz?");
            if (creator == null) {
                return;
            }
            Quiz newQuiz = new Quiz(title, creator);
            quizList.addQuiz(newQuiz);
            int index = listModel.getSize();
            listModel.addElement("\tQuiz " + (index + 1) + ": " + title);

            JOptionPane.showMessageDialog(null, "Quiz " + (index + 1) + " created!");
        }
    }

    // Represents the action to be taken when the user wants to delete a quiz
    private class DeleteListener implements ActionListener {

        // MODIFIES: ListPanel.this
        // EFFECTS: responds to button click by deleting the selected quiz
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            quizList.removeQuiz(index);
            listModel.removeAllElements();
            displayQuizzes();

            int size = listModel.getSize();
            if (size == 0) { // all quizzes removed
                deleteButton.setEnabled(false);
            } else {
                if (index == listModel.getSize()) {
                    index--;
                }
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // Represents the action to be taken when the user wants to load list of saved quizzes
    private class LoadListener implements ActionListener {

        // MODIFIES: ListPanel.this
        // EFFECTS: responds to button click by loading saved quizzes
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.removeAllElements();
            loadQuizList(jsonReader);
            displayQuizzes();
            JOptionPane.showMessageDialog(null, "Quizzes successfully loaded from " + JSON_STORE);
        }
    }

    // Represents the action to be taken when the user wants to save current list of quizzes
    private class SaveListener implements ActionListener {

        // MODIFIES: ListPanel.this
        // EFFECTS: responds to button click by saving current quizzes
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(quizList);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Saved quizzes to " + JSON_STORE);
            } catch (FileNotFoundException f) {
                JOptionPane.showMessageDialog(null, "Unable to write to " + JSON_STORE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a question to the selected quiz
    private void addQuestion(Quiz selectedQuiz) {
        String question = JOptionPane.showInputDialog("Please enter a true/false quiz question.");
        if (question == null) {
            return;
        }
        Object[] options = { "True", "False" };
        int option = JOptionPane.showOptionDialog(this,
                "Please select the answer for your quiz question.",
                "Quiz Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        String answer = null;
        if (option == JOptionPane.YES_OPTION) {
            answer = "Y";
        } else if (option == JOptionPane.NO_OPTION) {
            answer = "N";
        }
        if (answer != null) {
            selectedQuiz.addQuestion(question, answer);
            String title = selectedQuiz.getTitle();
            int numQuestions = selectedQuiz.numOfQuestions();
            JOptionPane.showMessageDialog(this, title + " quiz now has " + numQuestions + " questions");
        }
    }

    // EFFECTS: starts the quiz if there is at least 1 question; displays error msg otherwise
    private void playQuiz(Quiz selectedQuiz) {
        if (selectedQuiz.numOfQuestions() == 0) {
            JOptionPane.showMessageDialog(this, "No quiz questions available!");
        } else {
            startQuiz(selectedQuiz);
        }
    }

    // EFFECTS: quiz starts and questions are asked one-by-one;
    //          prompts user for true/false answers and gives a score at the end
    private void startQuiz(Quiz quiz) {
        int rightAns = 0;
        int questionNum = 1;

        JOptionPane.showMessageDialog(this, "Quiz start!");
        ArrayList<Question> questions = quiz.getQuestions();
        for (Question q : questions) {
            String questionString = "Question " + questionNum + ": " + q.getQuestion();
            Object[] options = { "True", "False" };
            int n = JOptionPane.showOptionDialog(this, questionString, "Question " + questionNum,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            questionNum++;
            String answer = convertAnswer(n);
            if (answer == null) { // ends the quiz if a question is not answered
                return;
            }
            if (q.checkAnswer(answer)) {
                rightAns++;
                JOptionPane.showMessageDialog(this, "Correct!");
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Answer!");
            }
        }
        JOptionPane.showMessageDialog(this, "Quiz Finished! Score: " + rightAns + "/" + quiz.numOfQuestions());
    }

    // EFFECTS: returns "Y" if n is 0 or "N" if n is 1; returns null otherwise
    private String convertAnswer(int n) {   // helper function
        String answer = null;
        if (n == 0) {
            answer = "Y";
        } else if (n == 1) {
            answer = "N";
        }
        return answer;
    }

    // MODIFIES: this
    // EFFECTS: loads quiz list from file
    private void loadQuizList(JsonReader reader) {
        try {
            quizList = reader.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file");
        }
    }

}
