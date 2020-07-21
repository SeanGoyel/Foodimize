package model;

//Represents a brand's description
public class Brand implements Description {
    String brand;


    public Brand(String brand) {
        this.brand = brand;
    }

    //EFFECTS: returns the name of the brand
    public String getDescription() {
        return brand;
    }


    public String getDescriptionType() {
        return "Brand";
    }


    public String getName() {
        return brand;
    }
}
