

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RoomTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RoomTest
{
    /**
     * Default constructor for test class RoomTest
     */
    public RoomTest()
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
    public void testExits()
    {
        Room Lobby = new Room("Lobby", "a waiting room");
        assertNotNull(Lobby.getExitDirections());
        assertEquals(null, Lobby.getExit("west"));
        Room Office = new Room("Office", "a large office");
        Lobby.setExit("west", Office);
        assertEquals(Office, Lobby.getExit("west"));
    }
}

