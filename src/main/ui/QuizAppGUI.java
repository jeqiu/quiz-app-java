package ui;


import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

// Represents the quiz app main window frame
public class QuizAppGUI extends JFrame {
    public static final String SPLASHSCREEN_IMAGE = "./data/quizTime.png";
    private ListPanel quizzesPanel;
    private JLabel header;
    private JPanel headerPanel;

    // MODIFIES: this
    // EFFECTS: initializes splashscreen and main panels of the application
    QuizAppGUI() {
        new SplashScreen(SPLASHSCREEN_IMAGE, this);

        setTitle("QuizAppGUI");
        quizzesPanel = new ListPanel();
        add(quizzesPanel);
        quizzesPanel.setOpaque(true);

        createHeader();
        pack();
        centre();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null,
                        "Save before exiting the program?", "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    saveQuizzes();
                }
                printLog();
                dispose();
            }
        });
    }

    private void createHeader() {
        header = new JLabel("Quizzes:");
        headerPanel = new JPanel();
        header.setFont(new Font("Tacoma", Font.BOLD, 14));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);
    }

    // EFFECTS: prints the event log to console
    private void printLog() {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString() + "\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: centres the application window on the screen
    private void centre() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getPreferredSize();
        setLocation(screenSize.width / 2 - (frameSize.width / 2),
                screenSize.height / 2 - (frameSize.height / 2));
    }

    // MODIFIES: this
    // EFFECTS: saves current list of quizzes
    private void saveQuizzes() {
        try {
            quizzesPanel.jsonWriter.open();
            quizzesPanel.jsonWriter.write(quizzesPanel.quizList);
            quizzesPanel.jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved quizzes to " + ListPanel.JSON_STORE);
        } catch (FileNotFoundException f) {
            JOptionPane.showMessageDialog(null, "Unable to write to " + ListPanel.JSON_STORE);
        }
    }

    // EFFECTS: starts the application
    public static void main(String[] args) {
        new QuizAppGUI();
    }
}
