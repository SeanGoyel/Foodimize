package model;

//Represents a interface which is used to describe a food item
public interface Description {

    //EFFECTS: gets a description of a food item
    String getDescription();

    //EFFECTS: returns the type of description
    String getDescriptionType();

    //EFFECTS: returns name of restaurant or brand
    String getName();

}
