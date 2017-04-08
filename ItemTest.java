

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ItemTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ItemTest
{
    /**
     * Default constructor for test class ItemTest
     */
    public ItemTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testEquals()
    {
        Item apple1 = new Item("apple", 50, true);
        Item[] bag = new Item[2];
        bag[0] = apple1;
        bag[1] = apple1;
        assertEquals(true, bag[0].equals(bag[1]));
        assertEquals(true, bag[0].equals(bag[0]));
        Item apple2 = new Item("apple", 50, true);
        bag[1] = apple2;
        assertEquals(false, bag[0].equals(bag[1]));
        assertEquals(true, bag[0].equals(bag[0]));
    }

    @Test
    public void testItemStatus()
    {
        Item item = new Item("apple", 50, true);
        Item item2 = new Item("rock", 100, false);
        assertEquals("apple", item.getName());
        assertEquals("rock", item2.getName());
        assertEquals(50, item.getWeight(), 0.1);
        assertEquals(100, item2.getWeight(), 0.1);
        item.setWeight(100.0);
        item.setName("rock");
        assertEquals("rock", item.getName());
        assertEquals(100, item.getWeight(), 0.1);
        assertEquals(false, item2.canTake());
        assertEquals(true, item.canTake());
        assertEquals(false, item.equals(item2));
    }
}


