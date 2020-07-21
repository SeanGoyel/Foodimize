package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import model.FoodHistory;


//Main GUI class
public class GUI extends Application {
    private FoodHistory foodHistory;
    private SoundPlayer soundPlayer;

    //MODIFIES: this
    //EFFECTS: Starts the main menu of the application
    public void start(Stage primaryStage) throws Exception {
        soundPlayer = new SoundPlayer();
        foodHistory = new FoodHistory();
        InteractWithFoodItemsScene interactWithFoodItemsScene = new InteractWithFoodItemsScene(this);
        interactWithFoodItemsScene.setFirstStart(Boolean.TRUE);
        interactWithFoodItemsScene.start(new Stage());
    }

    public FoodHistory getFoodHistory() {
        return foodHistory;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}
