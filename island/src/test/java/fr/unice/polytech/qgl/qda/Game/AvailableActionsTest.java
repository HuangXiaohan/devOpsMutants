package fr.unice.polytech.qgl.qda.Game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * AvailableActions Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class AvailableActionsTest {
    private AvailableActions availableActions;

    @Before
    public void before() throws Exception {
        availableActions = AvailableActions.ECHO;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCost()
     */
    @Test
    public void testGetCost() throws Exception {
        assertEquals(AvailableActions.RangePrice.CHEAP, availableActions.getCost());
    }

    /**
     * Method: getInfluenceFactor()
     */
    @Test
    public void testGetInfluenceFactor() throws Exception {
        assertEquals("the cost is almost constant", availableActions.getInfluenceFactor());
    }

    /**
     * Method: getPhase()
     */
    @Test
    public void testGetPhase() throws Exception {
        assertEquals(AvailableActions.Phase.PHASE1, availableActions.getPhase());
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        assertEquals("ECHO", availableActions.toString());
    }
} 
