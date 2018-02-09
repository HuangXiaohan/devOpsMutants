package fr.unice.polytech.qgl.qda.Island;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Direction Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class DirectionTest {
    Direction direction;

    @Before
    public void before() throws Exception {
        direction = Direction.N;
    }

    @After
    public void after() throws Exception {
        direction = Direction.N;
    }

    /**
     * Method: getDescription()
     */
    @Test
    public void testGetDescription() throws Exception {
        assertEquals("N", direction.getDescription());
    }

    /**
     * Method: getMovX()
     */
    @Test
    public void testGetMovX() throws Exception {
        assertEquals(0, direction.getMovX());
    }

    /**
     * Method: getMovY()
     */
    @Test
    public void testGetMovY() throws Exception {
        assertEquals(1, direction.getMovY());
    }

    /**
     * Method: getReverse()
     */
    @Test
    public void testGetReverse() throws Exception {
        assertEquals(Direction.S, direction.getReverse());
        assertEquals(Direction.S.getLeftDirection(), direction.getReverse().getLeftDirection());
    }

    /**
     * Method: getRightDirection()
     */
    @Test
    public void testGetRightDirection() throws Exception {
        assertEquals(Direction.E, direction.getRightDirection());
        assertEquals(Direction.N, direction.getRightDirection().getLeftDirection());
    }

    /**
     * Method: getLeftDirection()
     */
    @Test
    public void testGetLeftDirection() throws Exception {
        assertEquals(Direction.W, direction.getLeftDirection());
    }

    /**
     * Method: getHeading(String heading)
     */
    @Test
    public void testGetHeading() throws Exception {
        assertEquals(Direction.E, Direction.getHeading("E"));
    }

    /**
     * Method: isReverse(Direction direction2)
     */
    @Test
    public void testIsReverse() throws Exception {
        assertEquals(false, direction.getReverse().isReverse(Direction.W));
    }


} 
