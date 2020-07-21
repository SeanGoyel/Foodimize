package model;

import model.exceptions.AlreadyAddedFoodItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodimizeTest {
    FoodHistory fh1;

    FoodItem fi1;
    FoodItem fi2;
    FoodItem fi3;
    FoodItem fi4;
    FoodItem fi5;
    FoodItem fi6;
    FoodItem fi7;

    Effect e1;
    Effect e2;
    Effect e3;
    Effect e4;
    Effect e5;

    Brand b1;
    Brand b2;
    Brand b3;

    Restaurant r1;
    Restaurant r2;
    Restaurant r3;
    Restaurant r4;
    Restaurant r5;

    @BeforeEach
    void setUp(){
        fh1 = new FoodHistory();

        b1 = new Brand("Uid's");
        b2 = new Brand("Lays");
        b3 = new Brand("Kraft Dinner");

        r1 = new Restaurant("McDonalds", "Fast Food");
        r2 = new Restaurant("Panda Express", "Chines");
        r3 = new Restaurant("Cheesecake Factory", "American");
        r4 = new Restaurant("Swiss Chalet", "American");
        r5 = new Restaurant("Swiss Chalet", "American Bar");



        fi1 = new FoodItem("Cheese Pizza",b1);
        fi2 = new FoodItem("Jelly Beans",b2);
        fi3 = new FoodItem("Cake", r3);
        fi4 = new FoodItem("Cake",r3);
        fi5 = new FoodItem("Chicken Fingers", r4);
        fi6 = new FoodItem("Chicken Fingers", r3);
        fi7 = new FoodItem("Chicken Fingers", r5);

        e1 = new Effect("Bloated", "Physical");
        e2 = new Effect("Gassy", "Physical");
        e3 = new Effect("Tired", "Emotional");
        e4 = new Effect("Sad", "Emotional");
        e5 = new Effect("Energized", "Physical");
    }

    @Test
    void testAlreadyAddedFoodItemExceptionThrownDifferentObjects() {
        try {
            fh1.addFoodItem(fi5);
            assertEquals(1, fh1.getFoodHistory().size());
            fh1.addFoodItem(fi7);
            fail();
        } catch (AlreadyAddedFoodItemException e) {
            //Expected, same name and restaurant name
        }

    }

    @Test
    void testAlreadyAddedFoodItemExceptionThrownSameObjects() {
        try {
            fh1.addFoodItem(fi5);
            assertEquals(1, fh1.getFoodHistory().size());
            fh1.addFoodItem(fi5);
            fail();
        } catch (AlreadyAddedFoodItemException e) {
            //Expected, same object
        }

    }

    @Test
    void testAlreadyAddedFoodItemExceptionNotThrown() {
        try {
            fh1.addFoodItem(fi5);
            assertEquals(1, fh1.getFoodHistory().size());
            fh1.addFoodItem(fi6);
            assertEquals(2, fh1.getFoodHistory().size());
            fh1.addFoodItem(fi4);
            assertEquals(3, fh1.getFoodHistory().size());
        } catch (AlreadyAddedFoodItemException e) {
            fail();
        }

    }

    @Test
    void testFoodItemConstructor() {
        assertEquals("Cheese Pizza",fi1.getName());
        assertEquals("Uid's",fi1.getDescription());
        assertEquals("Lays",fi2.getDescription());
        assertEquals("Lays",fi2.getDescriptionName());
        assertEquals("Cheesecake Factory",fi3.getDescriptionName());
        assertEquals(0,fi1.getEffectsList().size());
    }

    @Test
    void testEffectConstructor() {
        assertEquals("Physical",e1.getSensationType());
        assertEquals("Bloated",e1.getEffectDescription());
    }


    @Test
    void testBrandAndRestaurantConstructor() {
        assertEquals("Uid's",b1.getDescription());
        assertEquals("Uid's",b1.getName());

        assertEquals("McDonalds",r1.getName());
        assertEquals("McDonalds With Cuisine Type: Fast Food",r1.getDescription());

    }


    @Test
    void testAddAndRemoveEffects() {
        fi1.addEffect(e1);
        assertEquals(1,fi1.getEffectsList().size());
        assertEquals("Physical",e1.getSensationType());
        fi1.addEffect(e2);
        fi1.addEffect(e3);
        assertEquals("The effects that ingesting a Cheese Pizza has on you:\n" +
                "Bloated\n" + "Gassy\n" + "Tired\n",fi1.getEffects());
        fi1.addEffect(e1);
        assertEquals("The effects that ingesting a Cheese Pizza has on you:\n" +
                "Bloated\n" + "Gassy\n" + "Tired\n",fi1.getEffects());
        fi1.removeEffect(e1);
        fi1.removeEffect(e5);
        assertEquals("The effects that ingesting a Cheese Pizza has on you:\n" +
                 "Gassy\n" + "Tired\n",fi1.getEffects());
        assertEquals("No effects have been recorded",fi2.getEffects());
    }

    @Test
    void testFoodHistoryConstructor() {
        assertEquals(0,fh1.getNumberOfFoodItems());
        assertTrue(fh1.getFoodHistory().isEmpty());

    }

    @Test
    void testAddFoodItems() {
        try {
            assertEquals("No food records have been added", fh1.getFoodHistoryString());
            fh1.addFoodItem(fi1);
            assertEquals(0, fh1.findFoodItem("Cheese Pizza"));
            assertEquals(404, fh1.findFoodItem("Pepperoni Pizza"));
            assertEquals(1, fh1.getNumberOfFoodItems());
            assertEquals("The food you have recorded:\n" +
                    "Cheese Pizza\n", fh1.getFoodHistoryString());
            fh1.addFoodItem(fi2);
            assertEquals(0, fh1.findFoodItem("Cheese Pizza"));
            assertEquals(2, fh1.getNumberOfFoodItems());
            assertEquals("The food you have recorded:\n" +
                    "Cheese Pizza\n" + "Jelly Beans\n", fh1.getFoodHistoryString());
            fh1.addFoodItem(fi3);
            assertEquals("Cake\n", fh1.foodsFromCuisine("American"));
            assertEquals("No foods match this type", fh1.foodsFromCuisine("Italian"));
            fh1.addFoodItem(fi5);
            assertEquals("Cake\n" + "Chicken Fingers\n", fh1.foodsFromCuisine("American"));
        } catch (AlreadyAddedFoodItemException A) {
            //Exception will not have been throw, no duplicated items
            fail();
        }

    }

    @Test
    void testGetCuisineType() {
        assertEquals("American",fi5.getCuisineType());
    }
}
