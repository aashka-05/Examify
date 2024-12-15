import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Exam {
    private JFrame examFrame;
    private Database db;
    private String username;
    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private String[] userAnswers;
    private ResultSet questions;

    public Exam(String username, Database db) {
        this.username = username;
        this.db = db;
        userAnswers = new String[10]; // Assume 10 questions for simplicity
        questions = db.fetchQuestions();

        examFrame = new JFrame("Take Exam");
        showNextQuestion();
    }

    public void showNextQuestion() {
        try {
            if (questions.next() && currentQuestion < userAnswers.length) {  // Ensure currentQuestion is within bounds
                // Setup GUI for displaying a question
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(6, 1));

                JLabel questionLabel = new JLabel(questions.getString("question"));
                JRadioButton option1 = new JRadioButton(questions.getString("option1"));
                JRadioButton option2 = new JRadioButton(questions.getString("option2"));
                JRadioButton option3 = new JRadioButton(questions.getString("option3"));
                JRadioButton option4 = new JRadioButton(questions.getString("option4"));

                ButtonGroup optionsGroup = new ButtonGroup();
                optionsGroup.add(option1);
                optionsGroup.add(option2);
                optionsGroup.add(option3);
                optionsGroup.add(option4);

                JButton nextBtn = new JButton("Next");

                nextBtn.addActionListener(e -> {
                    if (option1.isSelected()) userAnswers[currentQuestion] = "1";
                    else if (option2.isSelected()) userAnswers[currentQuestion] = "2";
                    else if (option3.isSelected()) userAnswers[currentQuestion] = "3";
                    else if (option4.isSelected()) userAnswers[currentQuestion] = "4";

                    int correctOption = 0;
                    try {
                        correctOption = questions.getInt("correct_option");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Integer.parseInt(userAnswers[currentQuestion]) == correctOption) {
                        correctAnswers++;
                    }

                    currentQuestion++;
                    panel.removeAll();
                    showNextQuestion();  // Recursive call to show next question
                });

                panel.add(questionLabel);
                panel.add(option1);
                panel.add(option2);
                panel.add(option3);
                panel.add(option4);
                panel.add(nextBtn);

                examFrame.add(panel);
                examFrame.setSize(400, 400);
                examFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                examFrame.setVisible(true);
            } else {
                // When no more questions are available or currentQuestion exceeds bounds
                JOptionPane.showMessageDialog(null, "Exam completed! Correct answers: " + correctAnswers);
                db.updateResult(username, Integer.toString(correctAnswers));
                examFrame.dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

