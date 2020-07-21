package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;
import persistence.Reader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.scene.paint.Color.*;


//REFERENCES:
//https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
//https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm
//https://stackoverflow.com/questions/24441175/how-detect-which-column-selected-in-javafx-tableview
//https://www.dreamincode.net/forums/topic/226315-wav-file-to-play-when-button-pressed/

//Displays the portion of the application which can alter FoodItems
public class InteractWithFoodItemsScene extends Application {
    private static final String FOOD_HISTORY_FILE = "./data/foodHistory.txt";
    
    private FoodItem foodItem;
    private GUI gui;

    private Button addFoodItemButton;
    private Button showAllItemsButton;
    private Button showFromCuisineType;
    private Button saveData;

    private GridPane grid;
    private Scene scene;

    private Stage primaryStage;

    private CheckBox checkBoxYes;
    private CheckBox checkBoxNo;

    private String foodName;
    private String restaurantName;
    private String cuisineType;
    private String brandName;

    private TextField foodNameField;
    private TextField brandNameField;
    private TextField restaurantNameField;
    private TextField cuisineTypeField;

    private TextField effectNameField;
    private ComboBox effectTypeField;

    private int width = 400;
    private int height = 300;

    private TableView table;
    private TableColumn<Map, String> foodNameCol;
    private TableColumn<Map, String> foodDescriptionCol;
    private String tableSelectedCellData;

    private boolean firstStart;

    //EFFECTS: Initializes fields
    public InteractWithFoodItemsScene(GUI gui) {
        this.gui = gui;
        primaryStage = new Stage();

        foodNameField = new TextField();
        brandNameField = new TextField();
        restaurantNameField = new TextField();
        cuisineTypeField = new TextField();

        effectNameField = new TextField();
        effectTypeField = new ComboBox();

    }

    //MODIFIES: this
    //EFFECTS: initialises buttons on main menu
    public void makeButtons() {
        showAllItemsButton = new Button();
        showAllItemsButton.setText("My food history");
        showAllItemsButton.setOnAction(e -> showAllItems());

        showAllItemsButton.setMinSize(80, 40);
        showAllItemsButton.setTextFill(ORANGE);
        showAllItemsButton.setFont(Font.font("System", FontWeight.BOLD, 15));

        addFoodItemButton = new Button();
        addFoodItemButton.setText("Add a food item");
        addFoodItemButton.setOnAction(e -> addNewFoodItem());

        showFromCuisineType = new Button();
        showFromCuisineType.setText("Search my effects");
        showFromCuisineType.setOnAction(e -> new SearchEffectsScene(primaryStage, gui));

        saveData = new Button();
        saveData.setText("Save current food history");
        saveData.setOnAction(e -> new SaveScene(gui));

    }

    //MODIFIES: this
    //EFFECTS: initialises the main grid for the application
    public void makeGrid() {
        grid = new GridPane();
        scene = new Scene(grid, width, height);

        grid.setAlignment(Pos.BASELINE_LEFT);

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        BackgroundFill backgroundFill = new BackgroundFill((new Color(.16, .24, .52, .2)),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        grid.setBackground(background);

    }


    //MODIFIES: this
    //EFFECTS: creates main menu stage of the application and loads saved data if it is
    //         the first time this method is called
    public void start(Stage primaryStage) throws Exception {
        firstStart();
        makeButtons();
        makeGrid();

        ImageView img = new ImageView("ui/Foodimize.png");
        GridPane gridForButtons = new GridPane();
        BackgroundFill backgroundFill = new BackgroundFill((new Color(.16, .24, .52, .0)),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        gridForButtons.setBackground(background);
        gridForButtons.setHgap(2);
        gridForButtons.setVgap(4);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Foodimize");

        gridForButtons.add(showAllItemsButton, 0, 0);
        gridForButtons.add(addFoodItemButton, 1, 1);
        gridForButtons.add(showFromCuisineType, 0, 1);
        gridForButtons.add(saveData, 2, 1);

        grid.add(img, 0, 0);
        grid.add(gridForButtons, 0, 1);

        firstStart = false;
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    //EFFECTS: sets first start to true
    public void setFirstStart(Boolean firstStart) {
        this.firstStart = firstStart;
    }


    //EFFECTS: if firstStart is true the data is loaded from file
    public void firstStart() {
        if (firstStart) {
            loadFromFile();
            firstStart = false;
        }
    }

    //EFFECTS: plays button press sound and starts addNewFood item stage, if an exception is caught
    //         an error message is displayed
    public void addNewFoodItem() {
        gui.getSoundPlayer().playButtonPressAudio();
        try {
            startNewFoodItem(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }


    //MODIFIES: this
    //EFFECTS: creates addNewFoodItem stage
    public void startNewFoodItem(Stage primaryStage) throws Exception {

        makeGrid();
        foodNameField = new TextField();

        GridPane gridForCheckBox = new GridPane();
        gridForCheckBox.setAlignment(Pos.BASELINE_LEFT);
        gridForCheckBox.setHgap(10);
        gridForCheckBox.setBackground(new Background(new BackgroundFill((new Color(.16, .24, .52, 0)),
                CornerRadii.EMPTY, Insets.EMPTY)));

        checkBoxYes = new CheckBox("Yes");
        checkBoxNo = new CheckBox("No");

        gridForCheckBox.add(checkBoxNo, 1, 0);
        gridForCheckBox.add(checkBoxYes, 0, 0);

        Text whatDidYouEatText = new Text();
        whatDidYouEatText.setFont(new Font(15));
        whatDidYouEatText.setText("What did you eat?");

        Text fromRestaurantText = new Text();
        fromRestaurantText.setFont(new Font(15));
        fromRestaurantText.setTextAlignment(TextAlignment.LEFT);
        fromRestaurantText.setText("Is it from a restaurant?");

        checkBoxYes.setOnAction(e -> restaurantInputs());
        checkBoxNo.setOnAction(e -> brandInputs());

        addToStartNewFoodItemStage(whatDidYouEatText, fromRestaurantText, foodNameField, gridForCheckBox);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    //MODIFIES: this
    //EFFECTS: adds texts, text field and GridPane to grid
    public void addToStartNewFoodItemStage(Text whatDidYouEatText,
                                           Text fromRestaurantText, TextField foodNameField, GridPane gridForCheckBox) {
        grid.add(whatDidYouEatText, 0, 0);
        grid.add(foodNameField, 1, 0);
        grid.add(gridForCheckBox, 1, 2);
        grid.add(fromRestaurantText, 0, 2);

    }

    //MODIFIES: this
    //EFFECTS: creates fields to input restaurant info once checkBoxYes is clicked
    public void restaurantInputs() {
        gui.getSoundPlayer().playButtonPressAudio();
        if (!checkBoxNo.isSelected()) {
            restaurantNameField = new TextField();
            restaurantNameField.setPromptText("Enter restaurant name");
            grid.add(restaurantNameField, 0, 6);

            cuisineTypeField = new TextField();
            cuisineTypeField.setPromptText("Enter cuisine type");

            grid.add(cuisineTypeField, 0, 7);

            Button addFoodButton = new Button("Add food!");
            addFoodButton.setOnAction(e ->
                    getInfoFromBoxes()
            );
            grid.add(addFoodButton, 1, 11);
        }

    }

    //MODIFIES: this
    //EFFECTS: creates fields to input brand info once checkBoxNo is clicked
    public void brandInputs() {
        gui.getSoundPlayer().playButtonPressAudio();
        if (!checkBoxYes.isSelected()) {
            brandNameField = new TextField();
            brandNameField.setPromptText("Enter brand name");
            grid.add(brandNameField, 0, 6);


            Button addFoodButton = new Button("Add food!");
            addFoodButton.setOnAction(e ->
                    getInfoFromBoxes()
            );
            grid.add(addFoodButton, 1, 11);
        }

    }

    //MODIFIES: this
    //EFFECTS: calls methods to get info from restaurant or brand fields, if an exception is caught an error message
    //         is displayed
    public void getInfoFromBoxes() {
        gui.getSoundPlayer().playButtonPressAudio();
        if (checkBoxYes.isSelected()) {
            getTextFromBoxesRestaurant();
        } else {
            getTextFromBoxesBrand();
        }
        try {
            startAddEffect(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

    //MODIFIES: this
    //EFFECTS: gets info from brand fields
    public void getTextFromBoxesBrand() {
        foodName = foodNameField.getText();
        brandName = brandNameField.getText();
        foodItem = new FoodItem(foodName, new Brand(brandName));
    }

    //MODIFIES: this
    //EFFECTS: gets info from restaurant fields
    public void getTextFromBoxesRestaurant() {
        foodName = foodNameField.getText();
        restaurantName = restaurantNameField.getText();
        cuisineType = cuisineTypeField.getText();
        foodItem = new FoodItem(foodName, new Restaurant(restaurantName, cuisineType));
    }

    //MODIFIES: this
    //EFFECTS: creates stage for adding an effect to a newly created food item
    public void startAddEffect(Stage primaryStage) throws Exception {
        makeGrid();

        Text enterEffectNameText = new Text();
        enterEffectNameText.setFont(new Font(15));
        enterEffectNameText.setTextAlignment(TextAlignment.LEFT);
        enterEffectNameText.setText("How did " + foodItem.getName() + " make you feel?");

        effectNameField = new TextField();
        effectNameField.setMinWidth(4);

        Text enterSensationTypeText = new Text();
        enterSensationTypeText.setFont(new Font(15));
        enterSensationTypeText.setTextAlignment(TextAlignment.LEFT);
        enterSensationTypeText.setText("What type of sensation is this");

        ObservableList<String> options = FXCollections.observableArrayList("Physical", "Emotional");
        effectTypeField = new ComboBox(options);

        Button okayButton = new Button("Add Effect!");
        okayButton.setOnAction(e ->
                getInfoFromBoxesEffect());

        grid.add(enterEffectNameText, 0, 1);
        grid.add(effectNameField, 2, 1);
        grid.add(okayButton, 2, 11);
        grid.add(enterSensationTypeText, 0, 3);
        grid.add(effectTypeField, 2, 3);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    //MODIFIES: this
    //EFFECTS: gets info from fields that pertain to adding an effect, if an exception is caught an error message
    //         is displayed
    public void getInfoFromBoxesEffect() {
        gui.getSoundPlayer().playButtonPressAudio();
        foodItem.addEffect(new Effect(effectNameField.getText(), effectTypeField.getValue().toString()));
        try {
            gui.getFoodHistory().addFoodItem(foodItem);
            start(primaryStage);
        } catch (Exception e) {
            try {
                errorAlreadyAddedFoodItemStage(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    //EFFECTS: creates the error stage which functions as a pop up and tells the user an error has be made
    public void errorAlreadyAddedFoodItemStage(Stage primaryStage) throws Exception {

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
        popUpMessage.setText("This food item has already been added");
        popUpMessage.setFill(RED);

        Button okayButton = new Button("Okay");
        okayButton.setOnAction(e ->
                mainMenu()
        );

        popUp.add(popUpMessage, 0, 0);
        popUp.add(okayButton, 1, 0);
        primaryStage.setScene(popUpScene);
        primaryStage.show();

    }


    //EFFECTS: calls the add new effect stage, if an exception is caught an error message is displayed
    public void addNewEffect() {
        gui.getSoundPlayer().playButtonPressAudio();
        try {
            startAddNewEffect(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

    //MODIFIES: this
    //EFFECTS: returns a foodItem with the name from tableSelectedCellData
    public FoodItem setFoodItem() {
        FoodItem returnVal = foodItem;
        for (FoodItem f : gui.getFoodHistory().getFoodHistory()) {
            if (f.getName().equals(tableSelectedCellData)) {
                returnVal = f;
            }
        }
        return returnVal;
    }

    //MODIFIES: this
    //EFFECTS: gets the information from TextFields that correspond to a new effect, if an exception is caught
    //         an error message is displayed
    public void getInfoFromBoxesNewEffect() {
        gui.getSoundPlayer().playButtonPressAudio();
        foodItem = setFoodItem();
        int pos = gui.getFoodHistory().findFoodItem(foodItem.getName());
        gui.getFoodHistory().getFoodHistory().get(pos).addEffect(new Effect(effectNameField.getText(),
                effectTypeField.getValue().toString()));

        try {
            start(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

    //EFFECTS: calls method to create stage to display all FoodItems, if an exception is caught an error message
    //         is displayed
    public void showAllItems() {
        gui.getSoundPlayer().playButtonPressAudio();
        startTable(primaryStage);
    }

    //MODIFIES: this
    //EFFECTS: creates a table which shows all saved FoodItems; their names and brand or restaurant description
    public void startTable(Stage stage) {
        makeGrid();
        table = new TableView(generateDataInMap());
        grid.setAlignment(CENTER);

        foodNameCol = new TableColumn("Food Item Name");
        foodDescriptionCol = new TableColumn("Brand or Restaurant");

        table.getColumns().add(foodNameCol);
        table.getColumns().add(foodDescriptionCol);

        foodNameCol.setCellValueFactory(new MapValueFactory("Food item name"));
        foodNameCol.setMinWidth(120);

        foodDescriptionCol.setCellValueFactory(new MapValueFactory("Brand or restaurant"));
        foodDescriptionCol.setMinWidth(130);
        grid.add(table, 0, 0);

        Button effectsButton = new Button("See all effects of this item");
        effectsButton.setOnAction(e -> seeAllEffects());

        Button addEffectButton = new Button("Add a new effect to this item");
        addEffectButton.setOnAction(e -> addNewEffect());

        Button okayButton = new Button("Main Menu");
        okayButton.setOnAction(e -> mainMenu());


        grid.add(effectsButton, 0, 1);
        grid.add(addEffectButton, 0, 2);
        grid.add(okayButton, 0, 3);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    //MODIFIES: this
    //EFFECTS: sets tableSelectedCellData to the string from the left most column from a selected row
    public void getInfoFromTable() {
        tableSelectedCellData =
                getFoodItemNameFromColumn(
                        (table.getItems().get(table.getSelectionModel().getSelectedIndex())).toString());
    }

    //REQUIRES: input string has a "=" followed by a ","
    //EFFECTS: parses through a string to get the information between the first "=" and ","
    private String getFoodItemNameFromColumn(String cellData) {
        boolean equalsFound = false;
        boolean commaFound = false;
        char equals = "=".charAt(0);
        char comma = ",".charAt(0);
        String returnVal = "";

        for (int i = 0; !(equalsFound && commaFound); i++) {
            if (cellData.charAt(i) == equals) {
                equalsFound = true;
                returnVal = "";
            } else {
                if (cellData.charAt(i) == comma) {
                    commaFound = true;
                } else {
                    returnVal = returnVal + cellData.charAt(i);
                }
            }
        }
        return returnVal;
    }

    //EFFECTS: places the FoodItem name and description in two separate columns for all FoodItems in FoodHistory
    private ObservableList<Map> generateDataInMap() {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        if (gui.getFoodHistory().getFoodHistory() != null) {
            for (FoodItem f : gui.getFoodHistory().getFoodHistory()) {
                Map<String, String> dataRow = new HashMap<>();

                String value1 = f.getName();
                String value2 = "";
                if (f.getDescriptionField().getDescriptionType() == "Brand") {
                    value2 = f.getDescriptionName();
                }
                if (f.getDescriptionField().getDescriptionType() == "Restaurant") {
                    value2 = f.getDescriptionName();
                }
                dataRow.put("Food item name", value1);
                dataRow.put("Brand or restaurant", value2);
                allData.add(dataRow);
            }
        }
        return allData;
    }

    //MODIFIES: this
    //EFFECTS: creates stage to add a new effect to an existing FoodItem
    public void startAddNewEffect(Stage primaryStage) throws Exception {
        getInfoFromTable();
        makeGrid();
        effectNameField.setText("");

        Text effectNameText = new Text();
        effectNameText.setFont(new Font(15));
        effectNameText.setTextAlignment(TextAlignment.LEFT);
        effectNameText.setText("What effect would you like to add\nto " + tableSelectedCellData + "?");

        Text sensationTypeText = new Text();
        sensationTypeText.setFont(new Font(15));
        sensationTypeText.setTextAlignment(TextAlignment.LEFT);
        sensationTypeText.setText("What type of sensation is this");

        ObservableList<String> options = FXCollections.observableArrayList("Physical", "Emotional");
        effectTypeField = new ComboBox(options);

        Button okayButton = new Button("Add Effect!");
        okayButton.setOnAction(e -> getInfoFromBoxesNewEffect());

        grid.add(effectNameText, 0, 2);
        grid.add(effectNameField, 1, 2);
        grid.add(okayButton, 1, 11);
        grid.add(sensationTypeText, 0, 4);
        grid.add(effectTypeField, 1, 4);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    //MODIFIES: this
    //EFFECTS: sets FoodItem to match FoodItem name from tableSelectedCellData and calls method to create a stage for
    //         all saved effects, if an exception is caught an error message is displayed
    public void seeAllEffects() {
        gui.getSoundPlayer().playButtonPressAudio();
        getInfoFromTable();
        for (FoodItem f : gui.getFoodHistory().getFoodHistory()) {
            if (f.getName().equals(tableSelectedCellData)) {
                this.foodItem = f;
            }
        }
        try {
            startSeeAllEffects(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

    //MODIFIES: this
    //EFFECTS: creates a stage with all the physical and emotional effects of a FoodItem
    public void startSeeAllEffects(Stage primaryStage) throws Exception {

        makeGrid();
        String emotionalEffectsString = "";
        String physicalEffectsString = "";

        for (Effect e : foodItem.getEffectsList()) {
            if (e.getSensationType().equals("Emotional")) {
                emotionalEffectsString = emotionalEffectsString + "- " + e.getEffectDescription() + "\n";
            } else {
                physicalEffectsString = physicalEffectsString + "- " + e.getEffectDescription() + "\n";
            }
        }

        ArrayList<Text> listOfTextForStage =
                createTextForAllEffectsStage(physicalEffectsString, emotionalEffectsString);


        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> mainMenu());
        grid.add(listOfTextForStage.get(0), 0, 0);
        grid.add(listOfTextForStage.get(1), 0, 1);
        grid.add(listOfTextForStage.get(2), 0, 2);
        grid.add(listOfTextForStage.get(3), 0, 3);
        grid.add(mainMenuButton, 1, 4);


        this.primaryStage.setScene(scene);
        this.primaryStage.show();


    }

    //EFFECTS: creates Text for the seeAllEffects stage
    public ArrayList<Text> createTextForAllEffectsStage(String physicalEffectsString, String emotionalEffectsString) {
        ArrayList<Text> listOfTextForStage = new ArrayList<>();

        Text physicalEffects = new Text();
        physicalEffects.setFont(new Font(15));
        physicalEffects.setText("Physical Effects");
        physicalEffects.setFill(PURPLE);
        listOfTextForStage.add(physicalEffects);

        Text physicalEffectsEntries = new Text();
        physicalEffectsEntries.setFont(new Font(11));
        physicalEffectsEntries.setText(physicalEffectsString);
        listOfTextForStage.add(physicalEffectsEntries);

        Text emotionalEffects = new Text();
        emotionalEffects.setFont(new Font(15));
        emotionalEffects.setText("Emotional Effects");
        emotionalEffects.setFill(BLUE);
        listOfTextForStage.add(emotionalEffects);

        Text emotionalEffectsEntries = new Text();
        emotionalEffectsEntries.setFont(new Font(11));
        emotionalEffectsEntries.setText(emotionalEffectsString);
        listOfTextForStage.add(emotionalEffectsEntries);

        return listOfTextForStage;
    }

    //MODIFIES: this
    //EFFECTS: load FoodHistory from file
    private void loadFromFile() {
        try {
            List<FoodItem> foodItems = Reader.readFoodItems(new File(FOOD_HISTORY_FILE));
            for (FoodItem e : foodItems) {
                gui.getFoodHistory().addFoodItem(e);
            }
        } catch (Exception e) {
            displayErrorMessage();
        }

    }

    //EFFECTS: calls errorStage to construct a Error pop-up, if exception is caught stack trace is printed
    public void displayErrorMessage() {
        gui.getSoundPlayer().playButtonPressAudio();
        Stage newStage = new Stage();
        try {
            errorStage(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EFFECTS: creates the error stage which functions as a pop up and tells the user an error has be made
    public void errorStage(Stage primaryStage) throws Exception {

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
        popUpMessage.setText("An error has been made. Please try again.");

        Button okayButton = new Button("Okay");
        okayButton.setOnAction(e ->
                closeStage(primaryStage)
        );

        popUp.add(popUpMessage, 0, 0);
        popUp.add(okayButton, 1, 0);
        primaryStage.setScene(popUpScene);
        primaryStage.show();

    }

    //EFFECTS: close the input stage and plays sound
    private void closeStage(Stage primaryStage) {
        gui.getSoundPlayer().playButtonPressAudio();
        primaryStage.close();
    }

    //EFFECTS: calls method to create main menu stage, if an exception is caught an error message is displayed
    public void mainMenu() {
        gui.getSoundPlayer().playButtonPressAudio();
        try {
            start(primaryStage);
        } catch (Exception e) {
            displayErrorMessage();
        }
    }

}