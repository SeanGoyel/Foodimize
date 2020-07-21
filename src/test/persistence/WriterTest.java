package persistence;

import model.Brand;
import model.Effect;
import model.FoodItem;
import model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WriterTest {
    private static final String TEST_FILE1 = "./data/foodItemsWriterTest1.txt";
    private static final String TEST_FILE2 = "./data/foodItemsWriterTest2.txt";
    private Writer testWriter1;
    private Writer testWriter2;
    Brand b1;
    Brand b2;
    Restaurant r1;

    FoodItem fi1;
    FoodItem fi2;
    FoodItem fi3;

    Effect e1;
    Effect e2;
    Effect e3;
    Effect e4;
    Effect e5;
    Effect e6;



    @BeforeEach
    public void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter1 = new Writer(new File(TEST_FILE1));
        testWriter2 = new Writer(new File(TEST_FILE2));
        b1 = new Brand("nestle");
        b2 = new Brand("lays");
        r1 = new Restaurant("pizza hut", "italian");

        e1 = new Effect("happy","Emotional");
        e2 = new Effect("heavy","Physical");
        e3 = new Effect("bloated","Physical");

        e4 = new Effect("sad","Emotional");
        e5 = new Effect("fast","Physical");
        e6 = new Effect("tired","Physical");

        fi1 = new FoodItem("pizza", r1);
        fi1.addEffect(e1);
        fi1.addEffect(e2);
        fi1.addEffect(e3);

        fi2 = new FoodItem("egg", b1);
        fi2.addEffect(e4);
        fi2.addEffect(e5);
        fi2.addEffect(e6);

        fi3 = new FoodItem("chips",b2);
        fi3.addEffect(e1);

    }

    @Test
    public void testWriteFoodItem() {
        testWriter2.write(fi3);
        testWriter2.close();

        try {
            List<FoodItem> foodItems = Reader.readFoodItems(new File(TEST_FILE2));
            FoodItem fi1 = foodItems.get(0);

            assertEquals("chips", fi1.getName());
            assertEquals("The effects that ingesting a chips has on you:\n"
                    + "happy\n", fi1.getEffects());
            assertEquals("lays", fi1.getDescription());

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }

    }

    @Test
    public void testWriteFoodItems() {
        testWriter1.write(fi1);
        testWriter1.write(fi2);
        testWriter1.close();

        try {
            List<FoodItem> foodItems = Reader.readFoodItems(new File(TEST_FILE1));
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
}
