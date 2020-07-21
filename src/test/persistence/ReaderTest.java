package persistence;

import model.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {
    Reader reader;

    @BeforeEach
    public void setUp() {
        reader = new Reader();
    }


    @Test
    public void testParseFoodItemFile() {
        try {
            List<FoodItem> foodItems = reader.readFoodItems(new File("./data/foodItemsReaderTest.txt"));
            FoodItem fi1 = foodItems.get(0);
            FoodItem fi2 = foodItems.get(1);

            assertEquals("pizza", fi1.getName());
            assertEquals("The effects that ingesting a pizza has on you:\n"
                    + "happy\n" + "heavy\n" + "bloated\n", fi1.getEffects());
            assertEquals("pizza hut With Cuisine Type: italian", fi1.getDescription());

            assertEquals("egg", fi2.getName());
            assertEquals("The effects that ingesting a egg has on you:\n"
                    + "sad\n" + "fast\n" + "tired\n", fi2.getEffects());
            assertEquals("nestle", fi2.getDescription());

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }


    @Test
    public void testIOException() {
        try {
            reader.readFoodItems(new File("./path/hungry hungry hippo"));
        } catch (IOException e) {
            //expected
        }
    }
}
