package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;

import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.scene.paint.Color.*;


//Creates a scene to search through all of the existing effects from all foods
public class SearchEffectsScene extends Application {
    private GUI gui;
    private GridPane grid;
    private Scene scene;
    private String effectsThatMatch;
    private String userSearch;
    private Stage primaryStage;
    private TextField userSearchTextField;

    public SearchEffectsScene(Stage primaryStage, GUI gui) {
        this.gui = gui;
        this.primaryStage = primaryStage;

        try {
            start(this.primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //MODIFIES: this
    //EFFECTS: initialises the main grid for the application
    public void makeGrid() {
        grid = new GridPane();
        scene = new Scene(grid, 400, 300);

        grid.setAlignment(Pos.BASELINE_LEFT);

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        BackgroundFill backgroundFill = new BackgroundFill((new Color(.16, .24, .52, .2)),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        grid.setBackground(background);

    }

    @Override
    //EFFECTS: calls method to create effectsThatMatch stage, if an exception is caught an error message is displayed
    public void start(Stage primaryStage) throws Exception {
        makeGrid();
        gui.getSoundPlayer().playButtonPressAudio();
        grid.setAlignment(TOP_CENTER);

        Text whatAreYouSearchingFor = new Text();
        whatAreYouSearchingFor.setFont(new Font(13));
        whatAreYouSearchingFor.setTextAlignment(TextAlignment.CENTER);
        whatAreYouSearchingFor.setText("What did I eat that made me feel...");

        userSearchTextField = new TextField();

        Button searchButton = new Button("Search food history");
        searchButton.setOnAction(e ->
                searchEffectsInFoodHistory()
        );

        grid.add(whatAreYouSearchingFor, 0, 0);
        grid.add(userSearchTextField, 0, 1);
        grid.add(searchButton, 0, 2);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }


    //MODIFIES: this
    //EFFECTS: sets userSearch to userSearchTextField and creates stage to display the effects that match, if an
    //         exception is caught an error message is displayed
    public void searchEffectsInFoodHistory() {
        gui.getSoundPlayer().playButtonPressAudio();
        userSearch = userSearchTextField.getText();
        searchEffectsThatMatch();
        try {
            effectsThatMatch(primaryStage);
        } catch (Exception e) {
           //
        }

        Text searchResults = new Text();
        searchResults.setFont(new Font(11));
        searchResults.setTextAlignment(TextAlignment.LEFT);
        searchResults.setText(effectsThatMatch);
        searchResults.setFill(GREEN);

        grid.add(searchResults, 0, 3);

        Button okayButton = new Button("Main Menu");
        okayButton.setOnAction(e ->
                mainMenu());
        grid.add(okayButton, 1, 4);

    }


    //MODIFIES: this
    //EFFECTS: gets all effects from the FoodItem with the name that matches userSearch
    public void searchEffectsThatMatch() {
        effectsThatMatch = "";

        for (FoodItem f : gui.getFoodHistory().getFoodHistory()) {
            for (Effect e : f.getEffectsList()) {
                if (e.getEffectDescription().equals(userSearch)) {
                    effectsThatMatch = effectsThatMatch + "- " + f.getName() + "\n";
                }
            }
        }

        if (effectsThatMatch == "") {
            effectsThatMatch = "You have not consumed any food items that\nmatch this description";
        }
    }



    //MODIFIES: this
    //EFFECTS: creates initial stage for effectsThatMatch and asks user to input the effect they are wanting to
    //         search for
    public void effectsThatMatch(Stage primaryStage) throws Exception {

        makeGrid();
        grid.setAlignment(TOP_CENTER);

        Text whatAreYouSearchingFor = new Text();
        whatAreYouSearchingFor.setFont(new Font(13));
        whatAreYouSearchingFor.setTextAlignment(TextAlignment.CENTER);
        whatAreYouSearchingFor.setText("What did I eat that made me feel...");

        userSearchTextField = new TextField();

        Button searchButton = new Button("Search food history");
        searchButton.setOnAction(e ->
                searchEffectsInFoodHistory()
        );

        grid.add(whatAreYouSearchingFor, 0, 0);
        grid.add(userSearchTextField, 0, 1);
        grid.add(searchButton, 0, 2);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    //EFFECTS: goes back to the main menu
    public void mainMenu() {
        gui.getSoundPlayer().playButtonPressAudio();
        try {
            new InteractWithFoodItemsScene(gui).start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}