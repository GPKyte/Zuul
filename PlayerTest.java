

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PlayerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class PlayerTest
{
    /**
     * Default constructor for test class PlayerTest
     */
    public PlayerTest()
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
    public void testHealth()
    {
        Player player1 = new Player("TestBot", "TestRoom");
        assertEquals(100.0, player1.getHealth(), 0.1);
        player1.takeDamage(50);
        assertEquals(50, player1.getHealth(), 0.1);
        player1.setHealth(120);
        assertEquals(120, player1.getHealth(), 0.1);
    }
}

