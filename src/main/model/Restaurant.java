package model;

//Represents a restaurant's description
public class Restaurant implements Description {
    private String name;
    private String cuisineType;

    public Restaurant(String name, String cuisineType) {
        this.name = name;
        this.cuisineType = cuisineType;
    }

    public String getName() {
        return name;
    }

    //EFFECTS: returns a description of the restaurant with a name and cuisine type
    public String getDescription() {
        return name + " With Cuisine Type: " + cuisineType;
    }

    @Override
    public String getDescriptionType() {
        return "Restaurant";
    }


    public String getCuisineType() {
        return cuisineType;
    }
}
