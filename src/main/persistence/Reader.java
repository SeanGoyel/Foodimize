package persistence;

// A reader that can read food history data from a file

import model.Brand;
import model.Effect;
import model.FoodItem;
import model.Restaurant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//Reference sources: TellerApp

public class Reader {
    public static final String DELIMITER = ",";

    // EFFECTS: returns a list of food items parsed from file; throws
    // IOException if an exception is raised when opening / reading from file
    public static List<FoodItem> readFoodItems(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(fileContent);
    }

    // EFFECTS: returns content of each row in file as a list of strings
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns a list of food items parsed from list of strings
    // where each string contains data for one food item
    private static List<FoodItem> parseContent(List<String> fileContent) {
        List<FoodItem> foodItems = new ArrayList<>();

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitString(line);
            foodItems.add(parseFoodItems(lineComponents));
        }

        return foodItems;
    }

    // EFFECTS: returns a list of strings obtained by splitting line on DELIMITER
    private static ArrayList<String> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }

    // REQUIRES: component has size 5 or more where element 0 represents the
    // name of the food item for a FoodItem with type Brand. Component has size 6 or more where element 0 represents the
    // name of the food item for a FoodItem with type Restaurant.
    // EFFECTS: returns an foodItem constructed from components
    private static FoodItem parseFoodItems(List<String> components) {
        FoodItem foodItem = null;

        String name = components.get(0);
        if (components.get(1).equals("Brand")) {
            foodItem = parseBrand(components);
        }
        if (components.get(1).equals("Restaurant")) {
            foodItem = parseRestaurant(components);
        }

        return foodItem;
    }


    //REQUIRES: component 1 is "Restaurant", component 2 represents the restaurant name, component 3 represents the
    // cuisine type, component 4 represents first effect, component 5 represents the first effect type. Then the next
    // two components will be the effect name then the effect type, until all effects are recorded.
    //EFFECTS: returns a FoodItem with description type Restaurant
    private static FoodItem parseRestaurant(List<String> components) {
        String name = components.get(0);
        FoodItem newFoodItem = new FoodItem(name, new Restaurant(components.get(2), components.get(3)));

        for (int i = 4; i < components.size() - 1; i += 2) {
            int nextI = i + 1;
            String effectName = components.get(i);
            String effectType = components.get(nextI);
            newFoodItem.addEffect(new Effect(effectName, effectType));
        }

        return newFoodItem;
    }


    //REQUIRES: component 1 is "Brand", component 2 represents the brand name, component 3 represents first
    // effect, component 4 represents the first effect type. Then the next two components will be the effect name then
    // the effect type, until all effects are recorded.
    //EFFECTS: returns a FoodItem with description type Brand
    private static FoodItem parseBrand(List<String> components) {
        String name = components.get(0);
        FoodItem newFoodItem = new FoodItem(name, new Brand(components.get(2)));

        for (int i = 3; i < components.size() - 1; i += 2) {
            int nextI = i + 1;
            String effectName = components.get(i);
            String effectType = components.get(nextI);
            newFoodItem.addEffect(new Effect(effectName, effectType));
        }

        return newFoodItem;
    }

}
