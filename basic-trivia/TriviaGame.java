

          

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class TriviaGame extends JFrame {

   
	private static final long serialVersionUID = 1L;
	private int score = 0;
    private int questionIndex = 0;

    // Questions and Answers
    private String[][] questions = {
            {"What is the capital of France?", "Paris", "Berlin", "London", "Madrid", "Paris"},
            {"Which planet is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", "Mars"},
            {"What is the largest mammal in the world?", "Elephant", "Blue Whale", "Giraffe", "Panda", "Blue Whale"},
            {"Who wrote 'Romeo and Juliet'?", "Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain", "William Shakespeare"},
            {"What is the powerhouse of the cell?", "Nucleus", "Mitochondria", "Endoplasmic Reticulum", "Golgi Apparatus", "Mitochondria"},
            {"In what year did Christopher Columbus reach the Americas?", "1492", "1520", "1607", "1776", "1492"},
            {"Which element has the chemical symbol 'O'?", "Oxygen", "Gold", "Iron", "Silver", "Oxygen"},
            {"Who painted the Mona Lisa?", "Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Claude Monet", "Leonardo da Vinci"},
            {"What is the capital of Japan?", "Beijing", "Seoul", "Tokyo", "Bangkok", "Tokyo"},
            {"Which programming language is known as the 'coffee cup'?", "Java", "Python", "C++", "Ruby", "Java"}
            
    };
    private String[][] additionalQuestions = {
    		{"What is the largest ocean on Earth?", "Atlantic", "Indian", "Southern", "Pacific", "Pacific"},
            {"Who wrote 'To Kill a Mockingbird'?", "Harper Lee", "J.K. Rowling", "George Orwell", "F. Scott Fitzgerald", "Harper Lee"},
            {"Which country is known as the 'Land of the Rising Sun'?", "China", "South Korea", "Japan", "Vietnam", "Japan"},
            {"In what year did the Berlin Wall fall?", "1985", "1989", "1991", "1995", "1989"},
            {"What is the currency of Brazil?", "Peso", "Dollar", "Real", "Euro", "Real"},
            {"Who painted 'Starry Night'?", "Vincent van Gogh", "Claude Monet", "Pablo Picasso", "Leonardo da Vinci", "Vincent van Gogh"},
            {"Which planet is known as the 'Blue Planet'?", "Venus", "Mars", "Earth", "Jupiter", "Earth"},
            {"Who is the author of 'The Great Gatsby'?", "F. Scott Fitzgerald", "Ernest Hemingway", "Mark Twain", "George Orwell", "F. Scott Fitzgerald"},
            {"What is the largest desert in the world?", "Sahara", "Arctic", "Gobi", "Antarctic", "Antarctic"},
            {"In what year did the Titanic sink?", "1905", "1912", "1920", "1931", "1912"}
    };
    
    private ArrayList<String[]> allQuestions;

    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JButton nextButton;
    private JLabel resultLabel;

    public TriviaGame() {
        super("Trivia Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        resetGame(); 
        updateQuestion();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        questionLabel = new JLabel("", JLabel.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        answerGroup = new ButtonGroup();

        JPanel choicesPanel = new JPanel(new GridLayout(0, 1));
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioButton = new JRadioButton();
            radioButton.setActionCommand("Choice" + i);  
            answerGroup.add(radioButton);
            choicesPanel.add(radioButton);
        }
        add(choicesPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        resultLabel = new JLabel("", JLabel.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(nextButton);
        buttonPanel.add(resultLabel);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void updateQuestion() {
        if (questionIndex < questions.length) {
            String[] currentQuestion = allQuestions.get(questionIndex);

            questionLabel.setText(currentQuestion[0]);

            Enumeration<AbstractButton> radioButtons = answerGroup.getElements();
            ArrayList<String> choices = new ArrayList<>(Arrays.asList(currentQuestion).subList(1, currentQuestion.length - 1));
            Collections.shuffle(choices);

            for (int i = 0; i < 4 && radioButtons.hasMoreElements(); i++) {
                JRadioButton radioButton = (JRadioButton) radioButtons.nextElement();
                radioButton.setText(choices.get(i));
                radioButton.setSelected(false);
            }

            nextButton.setEnabled(true);
        } else {
            showResult();
        }
    }






    private void checkAnswer() {
        ButtonModel selectedButton = answerGroup.getSelection();
        if (selectedButton != null) {
            int selectedIndex = -1;

            // Find the index of the selected radio button
            Enumeration<AbstractButton> radioButtons = answerGroup.getElements();
            for (int i = 0; i < 4 && radioButtons.hasMoreElements(); i++) {
                JRadioButton radioButton = (JRadioButton) radioButtons.nextElement();
                if (radioButton.isSelected()) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                int correctIndex = -1;

                // Find the index of the correct answer in the current question
                for (int i = 1; i <= 4; i++) {
                    if (allQuestions.get(questionIndex)[i].equals(allQuestions.get(questionIndex)[5])) {
                        correctIndex = i - 1;
                        break;
                    }
                }

                System.out.println("User Answer Index: " + selectedIndex + ", Correct Answer Index: " + correctIndex);

                if (selectedIndex == correctIndex) {
                    score += 10;
                }
            }
        }

        // Check if the game is over before updating the question
        if (questionIndex >= allQuestions.size() - 1) {
            showResult();
        } else {
            questionIndex++;
            updateQuestion();
        }
    }

  
   

    private void resetGame() {
        score = 0;
        questionIndex = 0;

        // Combine the original and additional questions
        allQuestions = new ArrayList<>();
        Collections.addAll(allQuestions, questions);
        Collections.addAll(allQuestions, additionalQuestions);

        // Shuffle the combined questions
        Collections.shuffle(allQuestions);

        // Update the question with the first question
        updateQuestion();
    }





    private void showResult() {
        resultLabel.setText("Your Score: " + score);

        // Display an appropriate comment based on the score
        String comment;
        if (score == 100) {
            comment = "Perfect! You are a trivia master!";
        } else if (score >= 70) {
            comment = "Great job! You know your stuff!";
        } else if (score >= 40) {
            comment = "Not bad. Keep it up!";
        } else {
            comment = "Better luck next time. Study more!";
        }

        JOptionPane.showMessageDialog(this, comment, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        // Offer to play again
        int option = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }
    public void startGame() {
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TriviaGame().startGame();
            }
        });
    }
}
