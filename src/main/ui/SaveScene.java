package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;
import persistence.Writer;

import java.io.*;

import static javafx.geometry.Pos.TOP_CENTER;


//Saves the current FoodHistory and notifies the user through a pop up
public class SaveScene extends Application {
    private static final String FOOD_HISTORY_FILE = "./data/foodHistory.txt";
    private GUI gui;
    private Stage primaryStage;

    public SaveScene(GUI gui) {
        this.gui = gui;
        primaryStage = new Stage();
        gui.getSoundPlayer().playButtonPressAudio();
        saveToFile();
        try {
            start(primaryStage);
        } catch (Exception e) {
          //
        }
    }


    @Override
    //EFFECTS: creates stage that notifies user that the FoodHistory has been saved
    public void start(Stage primaryStage) throws Exception {
        GridPane popUp = new GridPane();
        Scene popUpScene = new Scene(popUp, 400, 50);

        popUp.setAlignment(Pos.BASELINE_LEFT);

        popUp.setPadding(new Insets(10, 10, 10, 10));
        popUp.setVgap(10);
        popUp.setHgap(10);
        popUp.setAlignment(TOP_CENTER);


        Text popUpMessage = new Text();
        popUpMessage.setFont(new Font(14));
        popUpMessage.setTextAlignment(TextAlignment.CENTER);
        popUpMessage.setText("You food history has been saved!");

        Button okayButton = new Button("Main Menu");
        okayButton.setOnAction(e ->
                mainMenuAndClose(primaryStage)
        );


        popUp.add(popUpMessage, 0, 0);
        popUp.add(okayButton, 1, 0);
        primaryStage.setScene(popUpScene);
        primaryStage.show();


    }


    //EFFECTS: save FoodHistory data to file
    private void saveToFile() {
        try {
            Writer writer = new Writer(new File(FOOD_HISTORY_FILE));
            for (FoodItem f : gui.getFoodHistory().getFoodHistory()) {
                writer.write(f);
            }
            writer.close();
            System.out.println("Food items saved to file " + FOOD_HISTORY_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save food items to " + FOOD_HISTORY_FILE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
    }


    //EFFECTS: calls method to create main menu stage and close current stage, if an exception is caught an error
    //         message is displayed
    private void mainMenuAndClose(Stage stage) {
        new InteractWithFoodItemsScene(gui);
        gui.getSoundPlayer().playButtonPressAudio();
        stage.close();
    }
}
