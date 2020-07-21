package model;

import persistence.Reader;
import persistence.Saveable;

import java.io.PrintWriter;
import java.util.ArrayList;

//Represents a food item which has been ingested along with the effects it has had on the individual
public class FoodItem implements Saveable {
    private String name;               // The name of the food item
    private Description description;   // The brand or restaurant of the specific food item
    private ArrayList<Effect> effects; // The list of effects associated with the food item


    //REQUIRES: name has a non-zero length
    //EFFECTS: sets the food item name, brand name or restaurant name
    public FoodItem(String name, Description description) {
        this.name = name;
        effects = new ArrayList<Effect>();
        this.description = description;
    }


    //MODIFIES: this
    //EFFECTS: adds an effect to the FoodItem object if that effect does not already exist
    public void addEffect(Effect effect) {
        boolean found = false;

        for (int i = 0; i < effects.size(); i++) {
            if (effects.get(i).getEffectDescription().equals(effect.getEffectDescription())) {
                found = true;
            }
        }
        if (!found) {
            effects.add(effect);
        }
    }

    //REQUIRES: non-empty string
    //MODIFIES: this
    //EFFECTS: removes an effect from the FoodItem object if it is there
    public void removeEffect(Effect effect) {
        boolean found = false;
        int index = 0;

        for (int i = 0; i < effects.size(); i++) {
            if (effects.get(i).getEffectDescription().equals(effect.getEffectDescription())) {
                found = true;
                index = i;
            }
        }
        if (found) {
            effects.remove(index);
        }
    }

    //EFFECTS: creates a string with all the effects recorded
    public String getEffects() {
        String effectsList = "";
        if (!(effects.size() == 0)) {
            for (Effect e : effects) {
                effectsList = effectsList + e.getEffectDescription() + "\n";
            }
            effectsList = "The effects that ingesting a " + name + " has on you:\n" + effectsList;
        } else {
            effectsList = "No effects have been recorded";
        }
        return effectsList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Effect> getEffectsList() {
        return effects;
    }

    //REQUIRES: FoodItem has a description with type Restaurant
    //EFFECTS: gets the cuisine type of a restaurant
    public String getCuisineType() {
        return ((Restaurant) description).getCuisineType();
    }

    public Description getDescriptionField() {
        return description;
    }

    public String getDescription() {
        return description.getDescription();
    }

    public String getDescriptionName() {
        return description.getName();
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print(name);
        printWriter.print(Reader.DELIMITER);

        if (description.getDescriptionType().equals("Brand")) {
            saveSpecificationsForBrand(printWriter);
        }

        if (description.getDescriptionType().equals("Restaurant")) {
            saveSpecificationsForRestaurant(printWriter);
        }

        boolean stop = effects.size() == 1;

        for (int i = 0; !stop; i++) {
            printWriter.print(Reader.DELIMITER);
            printWriter.print(effects.get(i).getEffectDescription());
            printWriter.print(Reader.DELIMITER);
            printWriter.print(effects.get(i).getSensationType());
            stop = (effects.size() - 2) == i;
        }
        printWriter.print(Reader.DELIMITER);
        printWriter.print(effects.get(effects.size() - 1).getEffectDescription());
        printWriter.print(Reader.DELIMITER);
        printWriter.println(effects.get(effects.size() - 1).getSensationType());

    }

    //EFFECTS: writes the specifications for a FoodItem with type Brand, without the effects to Printer
    public void saveSpecificationsForBrand(PrintWriter printWriter) {
        printWriter.print("Brand");
        printWriter.print(Reader.DELIMITER);
        printWriter.print(description.getName());
    }

    //EFFECTS: writes the specifications for a FoodItem with type Restaurant, without the effects to Printer
    public void saveSpecificationsForRestaurant(PrintWriter printWriter) {
        printWriter.print("Restaurant");
        printWriter.print(Reader.DELIMITER);
        printWriter.print(description.getName());
        printWriter.print(Reader.DELIMITER);
        printWriter.print(((Restaurant) description).getCuisineType());
    }
}
