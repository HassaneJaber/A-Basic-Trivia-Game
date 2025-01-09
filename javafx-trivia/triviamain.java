package mypackage;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

public class TriviaGameFX extends Application implements Initializable {

    private int score = 0;
    private int highestScore = 0;
    private int questionIndex = 0;

    private ArrayList<String[]> allQuestions;

    private String[][] questions = {
            {"What is the capital of France?", "Paris", "Berlin", "London", "Madrid", "Paris"},
            {"Which planet is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", "Mars"},
            {"What is the largest mammal in the world?", "Elephant", "Blue Whale", "Giraffe", "Panda", "Blue Whale"},
            {"Who wrote 'Romeo and Juliet'?", "Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain", "William Shakespeare"},
            {"What is the powerhouse of the cell?", "Nucleus", "Mitochondria", "Endoplasmic Reticulum", "Golgi Apparatus", "Mitochondria"}
    };

    private String[][] additionalQuestions = {
            {"What is the largest ocean on Earth?", "Atlantic", "Indian", "Southern", "Pacific", "Pacific"},
            {"Who wrote 'To Kill a Mockingbird'?", "Harper Lee", "J.K. Rowling", "George Orwell", "F. Scott Fitzgerald", "Harper Lee"},
            {"Where is Mount Fuji located?", "China", "South Korea", "Japan", "Vietnam", "Japan"}
    };

    @FXML
    private Label questionLabel;

    @FXML
    private VBox answerBox;

    @FXML
    private Button nextButton;

    @FXML
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quizgameview.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Trivia Game");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allQuestions = new ArrayList<>();
        Collections.addAll(allQuestions, questions);
        Collections.addAll(allQuestions, additionalQuestions);
        Collections.shuffle(allQuestions);

        updateQuestion();
    }

    private void updateQuestion() {
        if (questionIndex < allQuestions.size()) {
            String[] currentQuestion = allQuestions.get(questionIndex);

            questionLabel.setText(currentQuestion[0]);
            questionLabel.setWrapText(true); // Ensure text wraps
            questionLabel.setPrefWidth(450); // Adjust width to fit longer questions
            questionLabel.setPrefHeight(Region.USE_COMPUTED_SIZE); // Ensure dynamic height

            List<String> choices = new ArrayList<>(Arrays.asList(currentQuestion).subList(1, 5));
            Collections.shuffle(choices);

            answerBox.getChildren().clear();
            ToggleGroup group = new ToggleGroup();
            for (String choice : choices) {
                RadioButton rb = new RadioButton(choice);
                rb.setToggleGroup(group);
                rb.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 300px; -fx-background-color: #f0f0f0; -fx-border-color: #d3d3d3; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                rb.setMaxWidth(Double.MAX_VALUE);
                answerBox.getChildren().add(rb);
            }

            nextButton.setDisable(false);
        } else {
            showResult();
        }
    }


    private void checkAnswer() {
        RadioButton selected = (RadioButton) answerBox.getChildren().stream()
                .filter(node -> node instanceof RadioButton && ((RadioButton) node).isSelected())
                .map(node -> (RadioButton) node)
                .findFirst()
                .orElse(null);

        if (selected != null) {
            String selectedText = selected.getText();
            String correctAnswer = allQuestions.get(questionIndex)[5];

            if (selectedText.equals(correctAnswer)) {
                score += 10;
            }
            questionIndex++;
            updateQuestion();
        }
    }

    private void showResult() {
        if (score > highestScore) {
            highestScore = score;
        }

        resultLabel.setText("Your Highest Score: " + highestScore);

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        alert.setContentText("Your score: " + score + "\nYour highest score: " + highestScore);

        ButtonType resetButtonType = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(resetButtonType, closeButtonType);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-size: 14px; -fx-background-color: #f9f9f9; -fx-border-color: #d3d3d3; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resetButtonType) {
            resetGame();
        }
    }

    private void resetGame() {
        score = 0;
        questionIndex = 0;
        Collections.shuffle(allQuestions);
        updateQuestion();
    }

    @FXML
    private void onNextButtonClicked() {
        checkAnswer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
