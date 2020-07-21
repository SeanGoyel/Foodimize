package model;

import model.exceptions.AlreadyAddedFoodItemException;

import java.util.ArrayList;

// Represents all recorded food items
public class FoodHistory {
    private int numberOfFoodItems;             // The number of food items recorded
    private ArrayList<FoodItem> foodHistory;   // The list of food items

    public FoodHistory() {
        foodHistory = new ArrayList<FoodItem>();
        numberOfFoodItems = 0;
    }


    //MODIFIES: this
    //EFFECTS: adds a FoodItem object to FoodHistory if a FoodItem with the same name and a matching restaurant or brand
    //         name has already been added then AlreadyAddedFoodItemException is thrown
    public void addFoodItem(FoodItem foodItem) throws AlreadyAddedFoodItemException {

        for (int i = 0; i < foodHistory.size(); i++) {
            FoodItem currentFoodItem = foodHistory.get(i);
            if (currentFoodItem.getName().equals(foodItem.getName())
                    && currentFoodItem.getDescriptionName().equals(foodItem.getDescriptionName())) {
                throw new AlreadyAddedFoodItemException();
            }
        }
        foodHistory.add(foodItem);
        numberOfFoodItems++;
    }


    //EFFECTS: produces a string of all the food in the FoodHistory, if no FoodItems have been recorded then
    //         "No food records have been added" is returned
    public String getFoodHistoryString() {
        String foodHistoryList = "";
        if (!(foodHistory.size() == 0)) {
            for (FoodItem foodItem : foodHistory) {
                foodHistoryList = foodHistoryList + foodItem.getName() + "\n";
            }
            foodHistoryList = "The food you have recorded:\n" + foodHistoryList;
        } else {
            foodHistoryList = "No food records have been added";
        }
        return foodHistoryList;
    }


    //EFFECTS: finds the position of a FoodItem and if food item isn't found it returns 404
    public int findFoodItem(String foodItem) {
        int pos = 404;
        for (int i = 0; i < foodHistory.size(); i++) {
            if (foodHistory.get(i).getName().equals(foodItem)) {
                pos = i;
            }

        }
        return pos;
    }

    //EFFECTS: produces String of all food items form a chosen cuisine type, if no food item has recorded that cuisine
    //         type them "No foods match this type" is returned
    public String foodsFromCuisine(String cuisineName) {
        String log = "";
        String isEmpty = "No foods match this type";

        for (FoodItem f : foodHistory) {
            if (f.getDescriptionField().getDescriptionType() == "Restaurant") {
                if (f.getCuisineType() == cuisineName) {
                    isEmpty = "";
                    log = log + f.getName() + "\n";
                }
            }
        }

        return isEmpty + log;
    }

    public int getNumberOfFoodItems() {
        return numberOfFoodItems;
    }

    public ArrayList<FoodItem> getFoodHistory() {
        return foodHistory;
    }
}
